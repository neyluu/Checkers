package checkers;

import checkers.scenes.Cooperation;
import checkers.scenes.CooperationIntro;
import checkers.scenes.MainMenu;
import checkers.scenes.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Checkers extends Application
{
    private final Settings settings = new Settings();
    private final SceneManager sceneManager = SceneManager.getInstance();

    @Override
    public void start(Stage stage) throws IOException
    {
        sceneManager.setStage(stage);
        createScenes();

        stage.setTitle("Checkers");
        stage.setWidth(settings.screenWidth);
        stage.setHeight(settings.screenHeight);
        stage.setResizable(false);
        stage.setScene(sceneManager.getCurrentScene());
        stage.show();
    }

    private void createScenes()
    {
        MainMenu mainMenu = new MainMenu();
        sceneManager.addScene(mainMenu);

        CooperationIntro cooperationIntro = new CooperationIntro();
        sceneManager.addScene(cooperationIntro);

        Cooperation cooperation = new Cooperation();
        sceneManager.addScene(cooperation);

        sceneManager.setScene(0);
    }

    public static void main(String[] args)
    {
        launch();
    }
}