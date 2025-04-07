package checkers.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Cooperation extends SceneBase
{
    private HBox layout = new HBox();

    public Cooperation()
    {
        layout.setStyle("-fx-background-color: rgb(63,255,0);");

        VBox left = new VBox();
        VBox middle = new VBox();
        VBox right = new VBox();

        double half = (settings.screenWidth / 2) + 100;
        System.out.println(half);
        double halfHalf = (settings.screenWidth - half) / 2;

        left.setStyle("-fx-background-color: rgb(255, 255, 0);");
        left.setMinHeight(300);
        left.setMinWidth(halfHalf);

        middle.setStyle("-fx-background-color: rgb(0, 255, 255);");
        middle.setMinHeight(300);
        middle.setMinWidth(half);

        right.setStyle("-fx-background-color: rgb(255, 0, 255);");
        right.setMinHeight(300);
        right.setMinWidth(halfHalf);

        VBox UItop = new VBox();
        VBox UIbottom = new VBox();

        UItop.setMinWidth(halfHalf - 25);
        UItop.setMaxWidth(halfHalf - 25);
        UItop.setMinHeight(150);
        UItop.setMaxHeight(150);
        UItop.setStyle("-fx-background-color: rgb(0,0,0);");

        UIbottom.setMinWidth(halfHalf - 25);
        UIbottom.setMaxWidth(halfHalf - 25);
        UIbottom.setMinHeight(150);
        UIbottom.setMaxHeight(150);
        UIbottom.setStyle("-fx-background-color: rgb(255,255,255);");

        right.setSpacing(350);
        right.setAlignment(Pos.CENTER_RIGHT);
        right.getChildren().addAll(UItop, UIbottom);


        layout.getChildren().addAll(left, middle, right);

        VBox board = new VBox();
        board.setStyle("-fx-background-color: rgb(230, 20, 50);");
        board.setMinWidth(650);
        board.setMaxWidth(650);
        board.setMinHeight(650);
        board.setMaxHeight(650);
        middle.setAlignment(Pos.CENTER);
        middle.getChildren().add(board);
//
//        layout.setAlignment(Pos.CENTER);
//        layout.getChildren().add(board);

        setScene();
    }

    @Override
    protected void setScene()
    {
        scene = new Scene(layout);
    }
}
