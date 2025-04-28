package checkers.gui.buttons;

import checkers.Settings;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MenuButton extends Button
{
    private Settings settings = new Settings();
    private final int buttonWidth = 350;
    private final double centerX = (double) settings.screenWidth / 2;

    public MenuButton(String text)
    {
        this.setText(text);
        this.setTextFill(Color.BLACK);
        this.setFont(Font.font("Arial", FontWeight.NORMAL, 32));
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
