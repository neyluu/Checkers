package checkers.scenes;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SceneManager
{
    private Stage stage = null;
    private static SceneManager instance = null;
    private List<Scene> scenes = new ArrayList<>();
    private int currentScene = 0;

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
        scenes.add(scene.getScene());
    }

    public Scene getCurrentScene()
    {
        return scenes.get(currentScene);
    }

    public void setScene(int sceneIndex)
    {
        if(sceneIndex < 0 || sceneIndex >= scenes.size())
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        currentScene = sceneIndex;
        stage.setScene(getCurrentScene());
    }
}
