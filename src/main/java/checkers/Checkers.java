package checkers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class Checkers extends Application
{
    @Override
    public void start(Stage stage) throws IOException
    {
//        FXMLLoader fxmlLoader = new FXMLLoader(Checkers.class.getResource("hello-view.fxml"));
        Group root = new Group();
        Scene scene = new Scene(root, Color.WHITE);

        stage.setTitle("Hello!");
        stage.setWidth(1280);
        stage.setHeight(720);
        stage.setResizable(false);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}