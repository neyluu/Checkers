package checkers.scenes;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class SceneManager
{
    private Stage stage = null;
    private static SceneManager instance = null;
    private Map<SceneType, SceneBase> scenes = new HashMap<>();
    private SceneType currentScene = SceneType.MAIN_MENU;

    private SceneManager() { }
    public static SceneManager getInstance()
    {
        if(instance == null) instance = new SceneManager();
        return instance;
    }

    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    public void addScene(SceneBase scene)
    {
        scenes.put(scene.getType(), scene);
    }

    public Scene getCurrentScene()
    {
        return scenes.get(currentScene).getScene();
    }

    public void setScene(SceneType newScene)
    {
        currentScene = newScene;
        stage.setScene(getCurrentScene());
    }
}
