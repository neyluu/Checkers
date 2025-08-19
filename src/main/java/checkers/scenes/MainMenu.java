package checkers.scenes;

import checkers.gui.buttons.MenuButton;
import checkers.gui.outputs.GameTitle;
import checkers.scenes.utils.SceneType;
import javafx.application.Platform;

public class MainMenu extends SceneBase
{
    public MainMenu()
    {
        getStylesheets().add(getClass().getResource("/css/main-menu.css").toExternalForm());
        layout.getStyleClass().add("main-menu");

        initText();
        initButtons();
    }

    private void initText()
    {
        GameTitle gameTitle = new GameTitle();
        layout.getChildren().add(gameTitle);
    }

    private void initButtons()
    {
        MenuButton coopButton         = new MenuButton("Cooperation");
        MenuButton singleplayerButton = new MenuButton("Singleplayer");
        MenuButton multiplayerButton  = new MenuButton("Multiplayer");
        MenuButton exitButton         = new MenuButton("Exit");
        MenuButton replaysButton      = new MenuButton("Replays");

        coopButton.setOnAction(e -> sceneManager.setScene(SceneType.COOPERATION_INTRO));
        singleplayerButton.setOnAction(e -> sceneManager.setScene(SceneType.SINGLEPLAYER_INTRO));
        exitButton.setOnAction(e -> Platform.exit());
        multiplayerButton.setOnAction(e -> sceneManager.setScene(SceneType.MULTIPLAYER_INTRO));
        replaysButton.setOnAction(e -> sceneManager.setScene(SceneType.REPLAYS_INTRO));

        layout.getChildren().addAll(coopButton, singleplayerButton, multiplayerButton, replaysButton, exitButton);
    }
}
