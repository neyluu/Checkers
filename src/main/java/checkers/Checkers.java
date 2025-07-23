package checkers;

import checkers.logging.LogFormatter;
import checkers.scenes.utils.SceneManager;
import checkers.scenes.utils.SceneType;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Checkers extends Application
{
    private final SceneManager sceneManager = SceneManager.getInstance();

    @Override
    public void start(Stage stage) throws IOException
    {
        setupLogging();

        sceneManager.setStage(stage);
        sceneManager.setScene(SceneType.MAIN_MENU);

        stage.setTitle("Checkers");
        stage.setMinWidth(Settings.screenWidth);
        stage.setMinHeight(Settings.screenHeight);
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(Settings.iconPath)));
        stage.show();
    }

    public void setupLogging()
    {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        for(Handler handler : rootLogger.getHandlers())
        {
            handler.setFormatter(new LogFormatter());
        }
    }

    public static void main(String[] args)
    {
        launch();
    }
}