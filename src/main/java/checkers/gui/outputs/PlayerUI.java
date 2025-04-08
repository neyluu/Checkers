package checkers.gui.outputs;

import checkers.Settings;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PlayerUI extends VBox
{
    private Settings settings = new Settings();
    private final double panelWidth = (settings.screenWidth - ((settings.screenWidth / 2) + 100)) / 2;

    public PlayerUI(String playerName)
    {
        this.setMinWidth(panelWidth - 50);
        this.setMaxWidth(panelWidth - 50);
        this.setMinHeight(150);
        this.setMaxHeight(150);
        this.setStyle("-fx-background-color: rgb(99,99,99);");
        this.setAlignment(Pos.TOP_CENTER);

        Text text = new Text(playerName);
        text.setFill(Color.PINK);
        text.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        this.getChildren().add(text);
    }
}
