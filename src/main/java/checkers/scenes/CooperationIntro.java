package checkers.scenes;

import checkers.Settings;
import checkers.gui.buttons.MenuButton;
import checkers.gui.inputs.LabeledTextField;
import checkers.gui.inputs.LabeledTimeComboBox;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CooperationIntro extends SceneBase
{
    private VBox layout = new VBox();
    private TextField player1;
    private TextField player2;
    ComboBox<String> turnTime;


    public CooperationIntro()
    {
        layout.setStyle("-fx-background-color: rgb(25,25,25);");
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(100);


        initPlayerUsernameInputs();
        initButtons();


        setScene();
    }

    @Override
    protected void setScene()
    {
        scene = new Scene(layout, Settings.screenWidth, Settings.screenHeight);
    }

    private void initButtons()
    {
        MenuButton playButton = new MenuButton("Play");
        MenuButton backButton = new MenuButton("Back");
        addEventsToButtons(playButton, backButton);

        LabeledTimeComboBox comboBox = new LabeledTimeComboBox();
        turnTime = comboBox.getComboBox();

        layout.getChildren().addAll(comboBox, playButton, backButton);
    }

    private void addEventsToButtons(MenuButton playButton,  MenuButton backButton)
    {
        playButton.setOnAction(e ->
        {
            String player1Username = player1.getText();
            String player2Username = player2.getText();

            Settings.player1Username = player1Username;
            Settings.player2Username = player2Username;

            if(player1Username.isEmpty()) Settings.player1Username = "Player 1";
            if(player2Username.isEmpty()) Settings.player2Username = "Player 2";

            Settings.turnTime = turnTime.getValue();
            
            sceneManager.setScene(SceneType.COOPERATION);
        });

        backButton.setOnAction(e -> sceneManager.setScene(SceneType.MAIN_MENU));
    }

    private void initPlayerUsernameInputs()
    {
        HBox container = new HBox();

        container.setSpacing(50);
        container.setAlignment(Pos.CENTER);

        LabeledTextField left = new LabeledTextField("Enter first player username:", "Player1");
        LabeledTextField right = new LabeledTextField("Enter second player username:", "Player2");

        player1 = left.getTextField();
        player2 = right.getTextField();

        container.getChildren().addAll(left, right);
        layout.getChildren().add(container);
    }
}
