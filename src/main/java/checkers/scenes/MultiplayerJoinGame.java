package checkers.scenes;

import checkers.gui.buttons.MenuButton;
import checkers.gui.inputs.LabeledIPAddres;
import checkers.gui.inputs.LabeledTextField;
import checkers.scenes.utils.SceneType;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;

public class MultiplayerJoinGame extends SceneBase
{
    private TextField textField;
    private LabeledIPAddres ipField;

    public MultiplayerJoinGame()
    {
        layout.setStyle("-fx-background-color: rgb(25,25,25);");
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(75);

        MenuButton back = new MenuButton("Back");
        back.setOnAction(e -> sceneManager.setScene(SceneType.MULTIPLAYER_INTRO));

        LabeledTextField labeledTextField = new LabeledTextField("Enter username:", "Player 1");
        textField = labeledTextField.getTextField();
        labeledTextField.setMaxWidth(350);

        ipField = new LabeledIPAddres();
        ipField.setAlignment(Pos.CENTER);
        ipField.setMaxWidth(350);

        MenuButton join = new MenuButton("Join game");


        layout.getChildren().addAll(labeledTextField, ipField, join, back);
    }

}
