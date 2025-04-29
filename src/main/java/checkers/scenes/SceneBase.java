package checkers.scenes;

import checkers.Settings;
import checkers.scenes.utils.SceneManager;
import javafx.scene.Scene;

abstract public class SceneBase
{
    protected Settings settings = new Settings();
    protected SceneManager sceneManager = SceneManager.getInstance();
    protected Scene scene = null;

    // Should be called at the end of constructor, after initializing all elements
    abstract protected void setScene();

    public Scene getScene()
    {
        return scene;
    }
}
