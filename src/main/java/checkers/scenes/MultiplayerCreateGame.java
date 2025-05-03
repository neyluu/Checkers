package checkers.scenes;

import checkers.game.GameSession;
import checkers.gui.buttons.MenuButton;
import checkers.gui.inputs.LabeledTextField;
import checkers.gui.inputs.LabeledTimeComboBox;
import checkers.network.Server;
import checkers.scenes.utils.SceneType;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;

public class MultiplayerCreateGame extends SceneBase
{
    private TextField textField;
    private ComboBox<String> comboBox;

    public MultiplayerCreateGame()
    {
        layout.setStyle("-fx-background-color: rgb(25,25,25);");
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(75);

        initInputs();
        initButtons();
    }

    private void initInputs()
    {
        LabeledTextField labeledTextField = new LabeledTextField("Enter username:", "Player 1");
        textField = labeledTextField.getTextField();
        labeledTextField.setMaxWidth(350);

        LabeledTimeComboBox labeledTimeComboBox = new LabeledTimeComboBox();
        comboBox = labeledTimeComboBox.getComboBox();

        layout.getChildren().addAll(labeledTextField, labeledTimeComboBox);
    }

    private void initButtons()
    {
        MenuButton create = new MenuButton("Create game");
        create.setOnAction(e ->
        {
            try
            {
                GameSession session = GameSession.getInstance();
                session.player1Username = textField.getText();
                session.turnTime = comboBox.getValue();

                Server server = new Server();
                server.start();
            }
            catch (IOException ex)
            {

            }
        });

        MenuButton back = new MenuButton("Back");
        back.setOnAction(e -> sceneManager.setScene(SceneType.MULTIPLAYER_INTRO));

        layout.getChildren().addAll(create, back);
    }
}
