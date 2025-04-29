package checkers.scenes;

import checkers.gui.buttons.MenuButton;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

public class MultiplayerCreateGame extends SceneBase
{
    private VBox layout = new VBox();

    public MultiplayerCreateGame()
    {
        type = SceneType.MULTIPLAYER_CREATE_GAME;

        layout.setStyle("-fx-background-color: rgb(25,25,25);");
        layout.setAlignment(Pos.CENTER);

        MenuButton back = new MenuButton("Back");
        back.setOnAction(e -> sceneManager.setScene(SceneType.MULTIPLAYER_INTRO));

        layout.getChildren().addAll(back);

        setScene();
    }

    @Override
    protected void setScene()
    {
        scene = new Scene(layout, settings.screenWidth, settings.screenHeight);
    }
}
