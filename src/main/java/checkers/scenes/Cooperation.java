package checkers.scenes;

import checkers.game.CooperationGame;
import checkers.game.PieceType;
import checkers.gui.outputs.PlayerUI;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Cooperation extends SceneBase
{
    private String player1Username;
    private String player2Username;
    private String turnTime;

    private HBox layout = new HBox();
    private CooperationGame game;

    private PlayerUI player1UI = new PlayerUI();
    private PlayerUI player2UI = new PlayerUI();

    private final Object lock = new Object();

    private final double sizeMiddle = (settings.screenWidth / 2) + 100;
    private final double sizeSidePanel = (settings.screenWidth - sizeMiddle) / 2;

    public Cooperation(String username1, String username2, String turnTime)
    {
        this.player1Username = username1;
        this.player2Username = username2;
        this.turnTime = turnTime;

        type = SceneType.COOPERATION;
        layout.setStyle("-fx-background-color: rgb(25,25,25);");

        game = new CooperationGame(lock, player1UI, player2UI);
        
        initLayout();
        setScene();

        startGame();
        listenForMessage();
    }

    @Override
    protected void setScene()
    {
        scene = new Scene(layout, settings.screenWidth, settings.screenHeight);
    }

    private void startGame()
    {
        Thread thread = new Thread(() -> game.start());
        thread.setDaemon(true);
        thread.start();
    }

    private void listenForMessage()
    {
        Thread thread = new Thread(() ->
        {
            synchronized (lock)
            {

                try
                {
                    lock.wait();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            Platform.runLater(this::showWinAlert);
        });

        thread.setDaemon(true);
        thread.start();
    }

    public void showWinAlert()
    {
        ButtonType playAgain = new ButtonType("PlayAgain", ButtonBar.ButtonData.OK_DONE);
        ButtonType quit = new ButtonType("Quit", ButtonBar.ButtonData.OK_DONE);
        ButtonType quitMainMenu = new ButtonType("Quit to main menu", ButtonBar.ButtonData.OK_DONE);

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("");
        alert.setHeaderText("Game finished!");
        alert.setContentText((game.getCurrentTurn() == PieceType.WHITE ? player2Username : player1Username) + " win!");

        alert.getButtonTypes().setAll(playAgain, quit, quitMainMenu);

        alert.showAndWait();

        ButtonType alertResult = alert.getResult();

        if(alertResult.equals(quit))
        {
            Platform.exit();
        }
        if(alertResult.equals(quitMainMenu))
        {
            sceneManager.setScene(SceneType.MAIN_MENU);
        }
        if(alertResult.equals(playAgain))
        {
            game.reset();
            startGame();
            listenForMessage();
        }
    }

    private void initLayout()
    {
        initLeftPanel();
        initMiddlePanel();
        initRightPanel();
    }

    private void initLeftPanel()
    {
        VBox left = new VBox();
        left.setStyle("-fx-background-color: rgb(25,25,25);");
        left.setMinWidth(sizeSidePanel);

        layout.getChildren().add(left);
    }
    private void initMiddlePanel()
    {
        VBox middle = new VBox();

        StackPane boardContainer = new StackPane(game.getBoard());
        boardContainer.setAlignment(Pos.CENTER);

        middle.setStyle("-fx-background-color: rgb(25,25,25);");
        middle.setMinWidth(sizeMiddle);
        middle.setAlignment(Pos.CENTER);
        middle.getChildren().add(boardContainer);

        layout.getChildren().add(middle);
    }
    private void initRightPanel()
    {
        VBox right = new VBox();

        right.setStyle("-fx-background-color: rgb(25,25,25);");
        right.setMinHeight(300);
        right.setMinWidth(sizeSidePanel);

        player1UI.setUsername(player1Username);
        player1UI.setMinutes(parseTurnTime());
        player2UI.setUsername(player2Username);
        player2UI.setMinutes(parseTurnTime());

        right.setSpacing(350);
        right.setAlignment(Pos.CENTER);
        right.getChildren().addAll(player1UI, player2UI);

        layout.getChildren().add(right);
    }

    private int parseTurnTime()
    {
        String[] tokens = turnTime.split(" ");

        if(tokens[0].equals("unlimited")) return -1;
        else
        {
            try
            {
                return Integer.parseInt(tokens[0]);
            }
            catch (Exception e) { return 0; }
        }
    }
}
