package checkers.scenes;

import checkers.game.GameSession;
import checkers.gui.buttons.MenuButton;
import checkers.gui.inputs.LabeledIPAddres;
import checkers.gui.inputs.LabeledTextField;
import checkers.network.Client;
import checkers.scenes.utils.SceneType;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;

import java.io.IOException;

public class MultiplayerJoinGame extends SceneBase
{
    private TextField textField;
    private LabeledIPAddres ipField;

    public MultiplayerJoinGame()
    {
        layout.setStyle("-fx-background-color: rgb(25,25,25);");
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(75);

        initInputs();
        initButtons();
    }


    private void initInputs()
    {
        LabeledTextField labeledTextField = new LabeledTextField("Enter username:", "Player 2");
        labeledTextField.setMaxWidth(350);
        textField = labeledTextField.getTextField();

        ipField = new LabeledIPAddres();
        ipField.setAlignment(Pos.CENTER);
        ipField.setMaxWidth(350);

        layout.getChildren().addAll(labeledTextField, ipField);
    }

    private void initButtons()
    {
        MenuButton join = new MenuButton("Join game");
        join.setOnAction(e ->
        {
            String ip = ipField.getIP();
            if(ip.isEmpty()) ip = "localhost";
            GameSession.getInstance().player2Username = textField.getText();

            Client client = new Client(ip);
            client.start();

        });

        MenuButton back = new MenuButton("Back");
        back.setOnAction(e -> sceneManager.setScene(SceneType.MULTIPLAYER_INTRO));

        layout.getChildren().addAll(join, back);
    }
}
