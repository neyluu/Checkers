package checkers.game;

import javafx.scene.layout.VBox;

public class Cell extends VBox
{
    public Cell(int size)
    {
        this.setMinHeight(size);
        this.setMinWidth(size);
    }

}
