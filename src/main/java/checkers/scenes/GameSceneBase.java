package checkers.scenes;

import checkers.Settings;
import checkers.game.GameStarter;
import checkers.game.board.Board;
import checkers.game.utils.GameSession;
import checkers.gui.buttons.MenuButton;
import checkers.gui.outputs.PlayerUI;
import checkers.logging.AppLogger;
import checkers.scenes.utils.SceneType;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GameSceneBase extends SceneBase
{
    private final AppLogger logger = new AppLogger(GameSceneBase.class);

    protected String player1Username;
    protected String player2Username;
    protected String turnTime;

    private HBox row = new HBox();
    protected StackPane boardContainer;

    protected GameStarter game;
    protected PlayerUI player1UI = new PlayerUI();
    protected PlayerUI player2UI = new PlayerUI();

    private final double sizeMiddlePanel = (Settings.screenWidth / 2);
    private final double sizeSidePanel = (Settings.screenWidth - sizeMiddlePanel) / 2;

    protected GameSceneBase()
    {
        GameSession session = GameSession.getInstance();
        this.player1Username = session.player1Username;
        this.player2Username = session.player2Username;
        this.turnTime = session.turnTime;

        layout.setStyle("-fx-background-color: rgb(25,25,25);");
        layout.setAlignment(Pos.CENTER);
        layout.setPrefHeight(Settings.screenHeight);
        layout.setPrefWidth(Settings.screenWidth);

        initLayout();
        layout.getChildren().add(row);
    }

    protected void startGame()
    {
        Thread thread = new Thread(() -> game.start());
        thread.setDaemon(true);
        thread.start();
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
        left.setMinWidth(sizeSidePanel);
        left.setAlignment(Pos.BOTTOM_CENTER);

        MenuButton quitMainMenuButton = new MenuButton("Main menu");
        quitMainMenuButton.setOnAction(e -> sceneManager.setScene(SceneType.MAIN_MENU));
        quitMainMenuButton.setMaxWidth(250);
        quitMainMenuButton.setMinWidth(250);
        left.getChildren().add(quitMainMenuButton);

        row.getChildren().add(left);
    }
    private void initMiddlePanel()
    {
        boardContainer = new StackPane();
        boardContainer.setAlignment(Pos.CENTER);

        VBox middle = new VBox();
        middle.setMinWidth(sizeMiddlePanel);
        middle.setAlignment(Pos.CENTER);
        middle.getChildren().add(boardContainer);

        row.getChildren().add(middle);
    }
    private void initRightPanel()
    {
        player1UI.setUsername(player1Username);
        player1UI.setMinutes(parseTurnTime());
        player2UI.setUsername(player2Username);
        player2UI.setMinutes(parseTurnTime());

        VBox right = new VBox();
        right.setMinWidth(sizeSidePanel);
        right.setSpacing(301);
        right.setAlignment(Pos.CENTER);
        right.getChildren().addAll(player1UI, player2UI);

        row.getChildren().add(right);
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
            catch (Exception e)
            {
                logger.error("Failed to parse turn time [{}] to int", turnTime);
                return 0;
            }
        }
    }

    protected void updateBoard(Board board)
    {
        boardContainer.getChildren().clear();
        boardContainer.getChildren().add(new StackPane(board));
    }
}
