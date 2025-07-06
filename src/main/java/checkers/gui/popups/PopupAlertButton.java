package checkers.gui.popups;

import javafx.scene.control.Button;

public class PopupAlertButton extends Button
{
    public PopupAlertButton(String text)
    {
        getStylesheets().add(getClass().getResource("/css/popup-alert-button.css").toExternalForm());
        this.getStyleClass().add("popup-alert-button");

        this.setText(text);
    }
}
