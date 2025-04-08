package checkers.game;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class Board extends GridPane
{
    final int size = 8;
    final int width = 600;
    final int cellSize = width / size;

    public Board()
    {
        this.setMaxWidth(width);

        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                VBox cell = new VBox();
                cell.setMinHeight(cellSize);
                cell.setMinWidth(cellSize);

                if((i + j) % 2 == 0)
                {
                    cell.setStyle("-fx-background-color: rgb(92,52,31);");
                }
                else
                {
                    cell.setStyle("-fx-background-color: rgb(224,188,165);");
                }

                this.add(cell, i, j);
            }
        }
    }
}
