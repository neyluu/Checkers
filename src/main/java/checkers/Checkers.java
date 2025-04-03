package checkers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Checkers extends Application
{
    final Settings settings = new Settings();

    @Override
    public void start(Stage stage) throws IOException
    {
        VBox layout1 = new VBox();
        layout1.setStyle("-fx-background-color: rgb(25,25,25);");
        layout1.setPadding(new Insets(50, 50, 50, 50));
        layout1.setSpacing(30);
        layout1.setAlignment(Pos.CENTER);


        final double centerX = (double) settings.screenWidth / 2;
        final int menuButtonWidth = 350;


        Text title = new Text();
        {
            title.setText("CHECKERS");
            title.setFill(Color.rgb(180,180,180));
            title.setFont(Font.font("Impact", FontWeight.NORMAL, 100));
            double textWidth = title.getLayoutBounds().getWidth();
            double textHeight = title.getLayoutBounds().getHeight();
            title.setX(centerX - textWidth / 2);
            title.setY(0);
        }
        layout1.getChildren().add(title);


        Button coopButton = new Button("Cooperation");
        {
            coopButton.setTextFill(Color.BLACK);
            coopButton.setFont(Font.font("Arial", FontWeight.NORMAL, 32));
            coopButton.setMinWidth(menuButtonWidth);
            coopButton.setMaxWidth(menuButtonWidth);
            coopButton.setLayoutX(centerX - menuButtonWidth / 2);
            coopButton.setLayoutY(250);
        }
        layout1.getChildren().add(coopButton);


        Button singleplayerButton = new Button("Singleplayer");
        {
            singleplayerButton.setTextFill(Color.BLACK);
            singleplayerButton.setFont(Font.font("Arial", FontWeight.NORMAL, 32));
            singleplayerButton.setMinWidth(menuButtonWidth);
            singleplayerButton.setMaxWidth(menuButtonWidth);
            singleplayerButton.setLayoutX(centerX - menuButtonWidth / 2);
            singleplayerButton.setLayoutY(350);
        }
        layout1.getChildren().add(singleplayerButton);


        Button multiplayerButton = new Button("Multiplayer");
        {
            multiplayerButton.setTextFill(Color.BLACK);
            multiplayerButton.setFont(Font.font("Arial", FontWeight.NORMAL, 32));
            multiplayerButton.setMinWidth(menuButtonWidth);
            multiplayerButton.setMaxWidth(menuButtonWidth);
            multiplayerButton.setLayoutX(centerX - menuButtonWidth / 2);
            multiplayerButton.setLayoutY(450);
        }
        layout1.getChildren().add(multiplayerButton);


        Button exitButton = new Button("Exit");
        {
            exitButton.setTextFill(Color.BLACK);
            exitButton.setFont(Font.font("Arial", FontWeight.NORMAL, 32));
            exitButton.setMinWidth(menuButtonWidth);
            exitButton.setMaxWidth(menuButtonWidth);
            exitButton.setLayoutX(centerX - menuButtonWidth / 2);
            exitButton.setLayoutY(550);
            exitButton.getOnMouseClicked();
            exitButton.setOnAction(e -> Platform.exit());
        }
        layout1.getChildren().add(exitButton);

        Scene scene1 = new Scene(layout1);



        Button button2 = new Button("Go to Scene 1");
        StackPane layout2 = new StackPane();
        layout2.getChildren().add(button2);
        Scene scene2 = new Scene(layout2, 300, 200);

        coopButton.setOnAction(e -> stage.setScene(scene2));

        stage.setTitle("Checkers");
        stage.setWidth(settings.screenWidth);
        stage.setHeight(settings.screenHeight);
        stage.setResizable(false);
        stage.setScene(scene1);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}