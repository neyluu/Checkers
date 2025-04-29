package checkers.scenes;

import checkers.Settings;
import checkers.scenes.utils.SceneManager;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

abstract public class SceneBase extends Scene
{
    protected SceneManager sceneManager = SceneManager.getInstance();
    protected VBox layout = null;

    protected SceneBase()
    {
        super(new VBox(), Settings.screenWidth, Settings.screenHeight);
        this.layout = (VBox) getRoot();
    }
}
