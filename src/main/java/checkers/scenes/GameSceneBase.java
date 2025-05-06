package checkers.scenes;

import checkers.Settings;
import checkers.game.Board;
import checkers.game.GameSession;
import checkers.gui.outputs.PlayerUI;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GameSceneBase extends SceneBase
{
    protected String player1Username;
    protected String player2Username;
    protected String turnTime;

    private HBox row = new HBox();
    protected StackPane boardContainer;

    // TODO GAME INSTANCE

    protected PlayerUI player1UI = new PlayerUI();
    protected PlayerUI player2UI = new PlayerUI();

    private final double sizeMiddlePanel = (Settings.screenWidth / 2) + 100;
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

        // TODO GAME INITIALIZATION

        initLayout();
        layout.getChildren().add(row);
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
        right.setSpacing(350);
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
            catch (Exception e) { return 0; }
        }
    }

    protected void updateBoard(Board board)
    {
        boardContainer.getChildren().clear();
        boardContainer.getChildren().add(new StackPane(board));
    }
}
