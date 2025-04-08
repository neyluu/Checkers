package checkers.scenes;

import checkers.game.Game;
import checkers.gui.outputs.PlayerUI;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Cooperation extends SceneBase
{
    private String player1Username = "Player 1";
    private String player2Username = "Player 2";

    private HBox layout = new HBox();
    private Game game = new Game();

    private final double sizeMiddle = (settings.screenWidth / 2) + 100;
    private final double sizeSidePanel = (settings.screenWidth - sizeMiddle) / 2;

    public Cooperation(String username1, String username2)
    {
        player1Username = username1;
        player2Username = username2;

        type = SceneType.COOPERATION;

        layout.setStyle("-fx-background-color: rgb(63,255,0);");

        initLayout();

        setScene();
    }

    @Override
    protected void setScene()
    {
        scene = new Scene(layout, settings.screenWidth, settings.screenHeight);
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
        left.setStyle("-fx-background-color: rgb(255, 255, 0);");
        left.setMinWidth(sizeSidePanel);

        layout.getChildren().add(left);
    }
    private void initMiddlePanel()
    {
        VBox middle = new VBox();
        middle.setStyle("-fx-background-color: rgb(0, 255, 255);");
        middle.setMinWidth(sizeMiddle);
        middle.setAlignment(Pos.CENTER);

        // TODO TMP
        VBox board = new VBox();
        {
            board.setStyle("-fx-background-color: rgb(230, 20, 50);");
            board.setMinWidth(650);
            board.setMaxWidth(650);
            board.setMinHeight(650);
            board.setMaxHeight(650);
        }

        middle.getChildren().add(board);
        layout.getChildren().add(middle);
    }
    private void initRightPanel()
    {
        VBox right = new VBox();

        right.setStyle("-fx-background-color: rgb(255, 0, 255);");
        right.setMinHeight(300);
        right.setMinWidth(sizeSidePanel);

        PlayerUI player1UI = new PlayerUI(player1Username);
        PlayerUI player2UI = new PlayerUI(player2Username);

        right.setSpacing(350);
        right.setAlignment(Pos.CENTER);
        right.getChildren().addAll(player1UI, player2UI);

        layout.getChildren().add(right);
    }
}
