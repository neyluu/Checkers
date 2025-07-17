package checkers.gui.inputs;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LabeledTextField extends VBox
{
    private final Text label;
    private final TextField textField;

    public LabeledTextField(String labelText, String placeholder)
    {
        super(25);

        getStylesheets().add(getClass().getResource("/css/labeled-text-field.css").toExternalForm());
        this.getStyleClass().add("labeled-text-field");

        this.label = new Text(labelText);
        this.label.getStyleClass().add("label");

        this.textField = new TextField();
        this.textField.getStyleClass().add("text-field");

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
