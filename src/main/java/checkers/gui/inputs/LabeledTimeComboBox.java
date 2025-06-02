package checkers.gui.inputs;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LabeledTimeComboBox extends VBox
{
    private final String[] times = {"unlimited", "1 min", "2 min", "5 min", "10 min"};
    private final ComboBox<String> comboBox = new ComboBox<>();
    private final Text label = new Text("Choose time for turn:");

    public LabeledTimeComboBox()
    {
        getStylesheets().add(getClass().getResource("/css/labeled-time-combo-box.css").toExternalForm());
        this.getStyleClass().add("container");
        comboBox.getStyleClass().add("combo-box");
        label.getStyleClass().add("label");

        comboBox.setItems(FXCollections.observableArrayList(times));
        comboBox.setMinWidth(350);
        comboBox.setMinHeight(50);
        comboBox.getSelectionModel().selectFirst();

        this.getChildren().addAll(label, comboBox);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
    }

    public ComboBox<String> getComboBox()
    {
        return comboBox;
    }
}
