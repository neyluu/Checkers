package checkers.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

public class Cooperation extends SceneBase
{
    private VBox layout = new VBox();

    public Cooperation()
    {
        layout.setStyle("-fx-background-color: rgb(63,255,0);");

        VBox board = new VBox();
        board.setStyle("-fx-background-color: rgb(230, 20, 50);");
        board.setMinWidth(600);
        board.setMaxWidth(600);
        board.setMinHeight(600);
        board.setMaxHeight(600);

        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(board);

        setScene();
    }

    @Override
    protected void setScene()
    {
        scene = new Scene(layout);
    }
}
