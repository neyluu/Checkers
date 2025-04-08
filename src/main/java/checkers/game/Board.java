package checkers.game;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.Arrays;

public class Board extends GridPane
{
    final int size = 8;
    final int width = 600;
    final int cellSize = width / size;

    private Piece[][] pieces = new Piece[size][size];
    private Cell[][] cells = new Cell[8][8];

    public Board()
    {
        this.setMaxWidth(width);

        clearBoard();
        initPieces();
        drawBoard();
    }

    private void drawBoard()
    {
        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                Cell cell = new Cell(cellSize);
                cell.setAlignment(Pos.CENTER);

                if((i + j) % 2 == 0)
                {
                    cell.setStyle("-fx-background-color: rgb(224,188,165);");
                }
                else
                {
                    cell.setStyle("-fx-background-color: rgb(128,81,60);");
                }

                Piece piece = pieces[i][j];
                if(piece != null)
                {
                    piece.setStyle(
                            "-fx-background-color: " + piece.getColor() + ";" +
                            "-fx-background-radius: 50%;"
                    );
                    piece.setMinHeight(cellSize - 10);
                    piece.setMinWidth(cellSize - 10);
                    int posX = i;
                    int posY = j;
                    piece.setOnMouseClicked(e ->
                    {
                        piece.setX(posX);
                        piece.setY(posY);
                        movePiece(piece);
                    });
                    cell.getChildren().add(piece);
                }

                cells[i][j] = cell;

                this.add(cell, i, j);
            }
        }
    }

    private void movePiece(Piece piece)
    {
        Cell newCell = cells[piece.getX()][piece.getY() - 1];
        try
        {
            newCell.getChildren().removeLast();
        }
        catch (Exception e) {}

        piece.setY(piece.getY() - 1);
        piece.setOnMouseClicked(e ->
        {
            movePiece(piece);
        });

        Cell curCell = cells[piece.getX()][piece.getY()];
        try
        {
            curCell.getChildren().removeLast();
        }
        catch (Exception e) {}
        newCell.getChildren().add(piece);
    }

    private void initPieces()
    {
        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                if ((row + col) % 2 == 1)
                {
                    Piece piece = new Man();
                    piece.setColor("rgb(0,0,0)");
                    pieces[col][row] = piece;
                }
            }
        }

        for (int row = 5; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                if ((row + col) % 2 == 1)
                {
                    Piece piece = new Man();
                    piece.setColor("rgb(255,255,255)");
                    pieces[col][row] = piece;
                }
            }
        }
    }

    private void clearBoard()
    {
        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                pieces[i][j] = null;
            }
        }
    }
}
