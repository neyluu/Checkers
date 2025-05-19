package checkers.scenes;

import checkers.game.GameSession;
import checkers.gui.buttons.MenuButton;
import checkers.gui.inputs.LabeledTextField;
import checkers.gui.inputs.LabeledTimeComboBox;
import checkers.scenes.utils.SceneType;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class SingleplayerIntro extends SceneBase
{
    private TextField usernameField;
    private ComboBox<String> turnTime;

    public SingleplayerIntro()
    {
        layout.setStyle("-fx-background-color: rgb(25,25,25);");
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(50);

        initInputs();
        initButtons();
    }

    private void initInputs()
    {
        LabeledTextField username = new LabeledTextField("Enter username: ", "Player 1");
        username.setMaxWidth(350);
        usernameField = username.getTextField();

        LabeledTimeComboBox comboBox = new LabeledTimeComboBox();
        turnTime = comboBox.getComboBox();

        layout.getChildren().addAll(username, comboBox);
    }

    private void initButtons()
    {
        MenuButton playButton = new MenuButton("Play");
        MenuButton backButton = new MenuButton("Back");

        playButton.setOnAction(e -> createGame());
        backButton.setOnAction(e -> sceneManager.setScene(SceneType.MAIN_MENU));

        layout.getChildren().addAll(playButton, backButton);
    }

    private void createGame()
    {
        String playerUsername = usernameField.getText();
        GameSession session = GameSession.getInstance();

        session.player1Username = "Bot";
        session.player2Username = playerUsername;
        if(playerUsername.isEmpty()) session.player2Username = "Player";

        session.turnTime = turnTime.getValue();
        sceneManager.setScene(SceneType.SINGLEPLAYER);
    }
}
