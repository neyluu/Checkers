package checkers.scenes;

import checkers.gui.buttons.MenuButton;
import checkers.scenes.utils.SceneType;
import javafx.geometry.Pos;

public class MultiplayerCreateGame extends SceneBase
{
    public MultiplayerCreateGame()
    {
        layout.setStyle("-fx-background-color: rgb(25,25,25);");
        layout.setAlignment(Pos.CENTER);

        MenuButton back = new MenuButton("Back");
        back.setOnAction(e -> sceneManager.setScene(SceneType.MULTIPLAYER_INTRO));

        layout.getChildren().addAll(back);
    }
}
