package checkers.scenes;

import checkers.gui.buttons.MenuButton;
import checkers.gui.inputs.LabeledTextField;
import checkers.gui.inputs.LabeledTimeComboBox;
import checkers.scenes.utils.SceneType;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class MultiplayerCreateGame extends SceneBase
{
    private TextField textField;
    private ComboBox comboBox;

    public MultiplayerCreateGame()
    {
        layout.setStyle("-fx-background-color: rgb(25,25,25);");
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(75);

        MenuButton back = new MenuButton("Back");
        back.setOnAction(e -> sceneManager.setScene(SceneType.MULTIPLAYER_INTRO));

        LabeledTextField labeledTextField = new LabeledTextField("Enter username:", "Player 1");
        textField = labeledTextField.getTextField();
        labeledTextField.setMaxWidth(350);

        LabeledTimeComboBox labeledTimeComboBox = new LabeledTimeComboBox();
        comboBox = labeledTimeComboBox.getComboBox();

        MenuButton create = new MenuButton("Create game");
        create.setOnAction(e -> {

        });

        layout.getChildren().addAll(labeledTextField, labeledTimeComboBox, create, back);
    }
}
