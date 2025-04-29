package checkers.gui.inputs;

import javafx.scene.layout.HBox;

public class LabeledIPAddres extends HBox
{
    private LabeledTextField IPField = new LabeledTextField("Enter IP address:", "IP");

    public LabeledIPAddres()
    {
        this.getChildren().addAll(IPField);
    }

    public String getIP()
    {
        return IPField.getText();
    }
}

