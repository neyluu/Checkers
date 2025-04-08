package checkers.game;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

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
        List<Cell> validMoves = getValidMoves(piece);
        List<String> styles = new ArrayList<>();

        for(Cell cell : validMoves)
        {
            styles.add(cell.getStyle());
            cell.setStyle("-fx-background-color: rgb(88,41,20);");

            cell.setOnMouseClicked(e ->
            {
                System.out.println("clicked");
            });
        }

//        Cell newCell1 = cells[piece.getX() - 1][piece.getY() - 1];
//        Cell newCell2 = cells[piece.getX() + 1][piece.getY() - 1];
//
//        String newCell1Style = newCell1.getStyle();
//        String newCell2Style = newCell2.getStyle();
//
//        newCell1.setStyle("-fx-background-color: rgb(88,41,20);");
//        newCell2.setStyle("-fx-background-color: rgb(88,41,20);");
//
//        newCell1.setOnMouseClicked(e ->
//        {
//            System.out.println("clicked 1 !");
//            newCell2.setOnMouseClicked(e2 -> {});
//
//            try
//            {
//                newCell1.getChildren().removeLast();
//            }
//            catch (Exception e3) {}
//
//            piece.setY(piece.getY() - 1);
//            piece.setX(piece.getX() - 1);
//            piece.setOnMouseClicked(e4 ->
//            {
//                movePiece(piece);
//            });
//
//            Cell curCell = cells[piece.getX()][piece.getY()];
//            try
//            {
//                curCell.getChildren().removeLast();
//            }
//            catch (Exception e5) {}
//            newCell1.getChildren().add(piece);
//            newCell1.setStyle(newCell1Style);
//            newCell2.setStyle(newCell2Style);
//        });

//
//        newCell2.setOnMouseClicked(e ->
//        {
//            System.out.println("clicked 2 !");
//            newCell1.setOnMouseClicked(e2 -> {});
//
//
//            try
//            {
//                newCell2.getChildren().removeLast();
//            }
//            catch (Exception e3) {}
//
//            piece.setY(piece.getY() - 1);
//            piece.setX(piece.getX() + 1);
//            piece.setOnMouseClicked(e4 ->
//            {
//                movePiece(piece);
//            });
//
//            Cell curCell = cells[piece.getX()][piece.getY()];
//            try
//            {
//                curCell.getChildren().removeLast();
//            }
//            catch (Exception e5) {}
//            newCell2.getChildren().add(piece);
//            newCell1.setStyle(newCell1Style);
//            newCell2.setStyle(newCell2Style);
//        });
    }

    private List<Cell> getValidMoves(Piece piece)
    {
        List<Cell> validMoves = new ArrayList<>();

        Cell currentCell = cells[piece.getX()][piece.getY()];

        if(piece.getType() == PieceType.MAN_WHITE)
        {
            Cell newCellLeft, newCellRight;
            if(piece.getX() - 1 >= 0 && piece.getY() - 1 >= 0)
            {
                newCellLeft = cells[piece.getX() - 1][piece.getY() - 1];
                if(newCellLeft.getChildren().isEmpty()) validMoves.add(newCellLeft);
            }
            if(piece.getX() + 1 < size && piece.getY() - 1 >= 0)
            {
                newCellRight = cells[piece.getX() + 1][piece.getY() - 1];
                if(newCellRight.getChildren().isEmpty()) validMoves.add(newCellRight);
            }
        }

        return validMoves;
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
                    piece.setType(PieceType.MAN_BLACK);
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
                    piece.setType(PieceType.MAN_WHITE);
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
