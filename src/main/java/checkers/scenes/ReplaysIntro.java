package checkers.scenes;

import checkers.gui.buttons.MenuButton;
import checkers.scenes.utils.SceneType;

public class ReplaysIntro extends  SceneBase
{
    public ReplaysIntro()
    {
        getStylesheets().add(getClass().getResource("/css/replays-intro.css").toExternalForm());
        layout.getStyleClass().add("replays-intro");

        initButtons();
    }

    private void initButtons()
    {
        MenuButton loadButton = new MenuButton("Load");
        MenuButton backButton = new MenuButton("Back");

        loadButton.setOnAction(e -> { });
        backButton.setOnAction(e -> sceneManager.setScene(SceneType.MAIN_MENU));

        layout.getChildren().addAll(loadButton, backButton);
    }
}
