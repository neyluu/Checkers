package checkers.scenes;

import checkers.Settings;
import checkers.gui.buttons.MenuButton;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

public class MultiplayerIntro extends SceneBase
{
    private VBox layout = new VBox();

    public MultiplayerIntro()
    {
        layout.setStyle("-fx-background-color: rgb(25,25,25);");
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(50);

        MenuButton createGame = new MenuButton("Create game");
        MenuButton joinGame = new MenuButton("Join game");
        MenuButton back = new MenuButton("Back");


        MultiplayerCreateGame multiplayerCreateGame = new MultiplayerCreateGame();
//        sceneManager.addScene(multiplayerCreateGame);

        MultiplayerJoinGame multiplayerJoinGame = new MultiplayerJoinGame();
//        sceneManager.addScene(multiplayerJoinGame);

        createGame.setOnAction(e -> sceneManager.setScene(SceneType.MULTIPLAYER_CREATE_GAME));
        joinGame.setOnAction(e -> sceneManager.setScene(SceneType.MULTIPLAYER_JOIN_GAME));
        back.setOnAction(e -> sceneManager.setScene(SceneType.MAIN_MENU));


        layout.getChildren().addAll(createGame, joinGame, back);

        setScene();
    }

    @Override
    protected void setScene()
    {
        scene = new Scene(layout, Settings.screenWidth, Settings.screenHeight);
    }
}
