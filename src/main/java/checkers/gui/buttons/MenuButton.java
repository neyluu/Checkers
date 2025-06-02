package checkers.gui.buttons;

import checkers.Settings;
import javafx.scene.control.Button;

public class MenuButton extends Button
{
    private final int buttonWidth = 350;
    private final double centerX = (double) Settings.screenWidth / 2;

    public MenuButton(String text)
    {
        getStylesheets().add(getClass().getResource("/css/menu-button.css").toExternalForm());
        this.getStyleClass().add("menu-button");

        this.setText(text);
        this.setMinWidth(buttonWidth);
        this.setMaxWidth(buttonWidth);
        this.setLayoutX(centerX - buttonWidth / 2);
        this.setLayoutY(0);
    }
    public MenuButton(String text, int posY)
    {
        this(text);
        this.setLayoutY(posY);
    }
}
