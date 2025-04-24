package checkers.gui.inputs;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LabeledTimeComboBox extends VBox
{
    private final String[] times = {"unlimited", "1 min", "2 min", "5 min", "10 min"};
    private final ComboBox<String> comboBox = new ComboBox<>();
    private final Text label = new Text("Choose time for turn:");

    public LabeledTimeComboBox()
    {
        comboBox.setItems(FXCollections.observableArrayList(times));
        comboBox.setMinWidth(350);
        comboBox.setMinHeight(50);
        comboBox.getSelectionModel().selectFirst();
        comboBox.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 32px;");

        label.setFont(Font.font("Arial", FontWeight.NORMAL, 32));
        label.setFill(Color.rgb(180,180,180));

        this.getChildren().addAll(label, comboBox);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
    }

    public ComboBox<String> getComboBox()
    {
        return comboBox;
    }
}
