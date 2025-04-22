package checkers.scenes;

import checkers.game.CooperationGame;
import checkers.gui.outputs.PlayerUI;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Cooperation extends SceneBase
{
    private String player1Username;
    private String player2Username;

    private HBox layout = new HBox();
    private CooperationGame game = new CooperationGame();

    private final double sizeMiddle = (settings.screenWidth / 2) + 100;
    private final double sizeSidePanel = (settings.screenWidth - sizeMiddle) / 2;

    public Cooperation(String username1, String username2)
    {
        player1Username = username1;
        player2Username = username2;

        type = SceneType.COOPERATION;

        layout.setStyle("-fx-background-color: rgb(25,25,25);");

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

        // TODO TMP
//        VBox board = new VBox();
//        {
//            board.setStyle("-fx-background-color: rgb(230, 20, 50);");
//            board.setMinWidth(650);
//            board.setMaxWidth(650);
//            board.setMinHeight(650);
//            board.setMaxHeight(650);
//        }

        layout.getChildren().add(middle);
    }
    private void initRightPanel()
    {
        VBox right = new VBox();

        right.setStyle("-fx-background-color: rgb(25,25,25);");
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
