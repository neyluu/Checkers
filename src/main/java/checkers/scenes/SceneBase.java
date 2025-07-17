package checkers.scenes;

import checkers.Settings;
import checkers.scenes.utils.SceneManager;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

abstract public class SceneBase extends Scene
{
    protected SceneManager sceneManager = SceneManager.getInstance();

    // container is used to store and position elements that will be positioned absolute
    protected StackPane container = null;

    // layout is used to store and position elements that have to follow layout
    protected VBox layout = new VBox();

    protected SceneBase()
    {
        super(new StackPane(), Settings.screenWidth, Settings.screenHeight);
        this.container = (StackPane) getRoot();
        this.container.getChildren().add(layout);
    }

    public StackPane getContainer()
    {
        return container;
    }
}
