package checkers.scenes;

import javafx.stage.Stage;

public class SceneManager
{
    private static SceneManager instance = null;

    private Stage stage = null;
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

    public void setScene(SceneType newScene)
    {
        SceneBase scene = sceneFactory.createScene(newScene);
        stage.setScene(scene.getScene());
    }
}
