package checkers.gui.outputs;

import checkers.Settings;
import javafx.geometry.Insets;
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

    private final int minutes;

    public PlayerUI(String playerName, int minutes)
    {
        this.minutes = minutes;

        this.setMinWidth(panelWidth - 50);
        this.setMaxWidth(panelWidth - 50);
        this.setMinHeight(150);
        this.setMaxHeight(150);
        this.setStyle("-fx-background-color: rgb(123,123,123);");
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(10));
        this.setSpacing(30);

        Text text = new Text(playerName);
        text.setFill(Color.BLACK);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 28));

        TurnTimer turnTimer = new TurnTimer(this.minutes);
        turnTimer.setFill(Color.BLACK);
        turnTimer.setFont(Font.font("Arial", FontWeight.NORMAL, 24));

        this.getChildren().addAll(text, turnTimer);
    }
}
