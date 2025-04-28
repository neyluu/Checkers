package checkers.scenes;

import checkers.gui.buttons.MenuButton;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MainMenu extends SceneBase
{
    private final VBox layout = new VBox();

    public MainMenu()
    {
        type = SceneType.MAIN_MENU;

        layout.setStyle("-fx-background-color: rgb(25,25,25);");
        layout.setPadding(new Insets(50, 50, 50, 50));
        layout.setSpacing(30);
        layout.setAlignment(Pos.CENTER);
        initText();
        initButtons();

        setScene();
    }

    @Override
    protected void setScene()
    {
        scene = new Scene(layout, settings.screenWidth, settings.screenHeight);
    }

    private void initText()
    {
        Text title = new Text();
        {
            title.setText("CHECKERS");
            title.setFill(Color.rgb(180,180,180));
            title.setFont(Font.font("Impact", FontWeight.NORMAL, 100));
            double textWidth = title.getLayoutBounds().getWidth();
            double centerX = (double) settings.screenWidth / 2;
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
        exitButton.setOnAction(e -> Platform.exit());

        layout.getChildren().addAll(coopButton, singleplayerButton, multiplayerButton, exitButton);
    }
}
