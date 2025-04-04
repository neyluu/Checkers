package checkers.gui.buttons;

import checkers.Settings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MenuButton
{
    private Settings settings = new Settings();
    private final int buttonWidth = 350;
    private final double centerX = (double) settings.screenWidth / 2;
    private Button button = new Button();

    public MenuButton(String text)
    {
        button.setText(text);
        button.setTextFill(Color.BLACK);
        button.setFont(Font.font("Arial", FontWeight.NORMAL, 32));
        button.setMinWidth(buttonWidth);
        button.setMaxWidth(buttonWidth);
        button.setLayoutX(centerX - buttonWidth / 2);
        button.setLayoutY(0);
    }
    public MenuButton(String text, int posY)
    {
        this(text);
        button.setLayoutY(posY);
    }

    public Button getButton()
    {
        return button;
    }

    public void setEvent(EventHandler<ActionEvent> event)
    {
        button.setOnAction(event);
    }
}
