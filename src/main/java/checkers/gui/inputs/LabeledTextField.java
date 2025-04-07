package checkers.gui.inputs;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LabeledTextField extends VBox
{
    private final Text label;
    private final TextField textField;

    public LabeledTextField(String labelText, String placeholder)
    {
        super(25);
        this.label = new Text(labelText);
        this.label.setFill(Color.rgb(180,180,180));
        this.label.setFont(Font.font("Arial", FontWeight.NORMAL, 32));

        this.textField = new TextField();
        this.textField.setMinHeight(64);
        this.textField.setFont(Font.font("Arial", FontWeight.NORMAL, 32));
        this.textField.setPromptText(placeholder);

        this.getChildren().addAll(label, textField);
    }

    public String getText()
    {
        return textField.getText();
    }

    public void setText(String text)
    {
        textField.setText(text);
    }

    public TextField getTextField()
    {
        return textField;
    }

    public Text getLabel()
    {
        return label;
    }
}
