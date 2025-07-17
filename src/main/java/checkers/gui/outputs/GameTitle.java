package checkers.gui.outputs;

import javafx.scene.control.Label;

public class GameTitle extends Label
{
    private final String text = "CHECKERS";

    public GameTitle()
    {
        super();

        getStylesheets().add(getClass().getResource("/css/game-title.css").toExternalForm());
        this.getStyleClass().add("game-title");

        this.setText(text);
    }
}
