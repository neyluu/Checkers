package checkers.scenes.utils;

import checkers.scenes.SceneBase;
import javafx.stage.Stage;

public class SceneManager
{
    private static SceneManager instance = null;

    private Stage stage = null;
    private SceneBase currentScene = null;
    private final SceneFactory sceneFactory = new SceneFactory();

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
    public Stage getStage()
    {
        return stage;
    }
    public SceneBase getCurrentScene()
    {
        return currentScene;
    }

    public void setScene(SceneType newScene)
    {
        currentScene = sceneFactory.createScene(newScene);
        stage.setScene(currentScene);
    }
}
