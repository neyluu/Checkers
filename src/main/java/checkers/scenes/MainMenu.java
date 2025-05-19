package checkers.scenes;

import checkers.Settings;
import checkers.gui.buttons.MenuButton;
import checkers.scenes.utils.SceneType;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MainMenu extends SceneBase
{
    public MainMenu()
    {
        layout.setStyle("-fx-background-color: rgb(25,25,25);");
        layout.setPadding(new Insets(50, 50, 50, 50));
        layout.setSpacing(30);
        layout.setAlignment(Pos.CENTER);
        initText();
        initButtons();
    }

    private void initText()
    {
        Text title = new Text();
        {
            title.setText("CHECKERS");
            title.setFill(Color.rgb(180,180,180));
            title.setFont(Font.font("Impact", FontWeight.NORMAL, 100));
            double textWidth = title.getLayoutBounds().getWidth();
            double centerX = (double) Settings.screenWidth / 2;
            title.setX(centerX - textWidth / 2);
        }
        layout.getChildren().add(title);
    }

    private void initButtons()
    {
        MenuButton coopButton         = new MenuButton("Cooperation", 250);
        MenuButton singleplayerButton = new MenuButton("Singleplayer", 350);
        MenuButton multiplayerButton  = new MenuButton("Multiplayer", 450);
        MenuButton exitButton         = new MenuButton("Exit", 550);

        coopButton.setOnAction(e -> sceneManager.setScene(SceneType.COOPERATION_INTRO));
        singleplayerButton.setOnAction(e -> sceneManager.setScene(SceneType.SINGLEPLAYER_INTRO));
        exitButton.setOnAction(e -> Platform.exit());
        multiplayerButton.setOnAction(e -> sceneManager.setScene(SceneType.MULTIPLAYER_INTRO));

        layout.getChildren().addAll(coopButton, singleplayerButton, multiplayerButton, exitButton);
    }
}
