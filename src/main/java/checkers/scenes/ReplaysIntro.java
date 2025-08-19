package checkers.scenes;

import checkers.gui.buttons.MenuButton;
import checkers.gui.outputs.replays.ReplaysChooser;
import checkers.scenes.utils.SceneType;

public class ReplaysIntro extends  SceneBase
{
    private ReplaysChooser replaysChooser = new ReplaysChooser();

    public ReplaysIntro()
    {
        getStylesheets().add(getClass().getResource("/css/replays-intro.css").toExternalForm());
        layout.getStyleClass().add("replays-intro");

        layout.getChildren().add(replaysChooser);

        initButtons();
    }

    private void initButtons()
    {
        MenuButton loadButton = new MenuButton("Load");
        MenuButton backButton = new MenuButton("Back");

        loadButton.setOnAction(e -> loadReplay());
        backButton.setOnAction(e -> sceneManager.setScene(SceneType.MAIN_MENU));

        layout.getChildren().addAll(loadButton, backButton);
    }

    private void loadReplay()
    {
        replaysChooser.load();
    }
}
