package checkers.scenes;

import checkers.gui.buttons.MenuButton;
import checkers.scenes.utils.SceneType;
import javafx.geometry.Pos;

public class MultiplayerIntro extends SceneBase
{
    public MultiplayerIntro()
    {
        layout.setStyle("-fx-background-color: rgb(25,25,25);");
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(50);

        MenuButton createGame = new MenuButton("Create game");
        MenuButton joinGame = new MenuButton("Join game");
        MenuButton back = new MenuButton("Back");

        createGame.setOnAction(e -> sceneManager.setScene(SceneType.MULTIPLAYER_CREATE_GAME));
        joinGame.setOnAction(e -> sceneManager.setScene(SceneType.MULTIPLAYER_JOIN_GAME));
        back.setOnAction(e -> sceneManager.setScene(SceneType.MAIN_MENU));

        layout.getChildren().addAll(createGame, joinGame, back);
    }
}
