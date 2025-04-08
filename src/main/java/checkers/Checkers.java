package checkers;

import checkers.scenes.*;
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

        sceneManager.setScene(SceneType.MAIN_MENU);
    }

    public static void main(String[] args)
    {
        launch();
    }
}