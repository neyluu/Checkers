package checkers.game;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

import java.util.*;

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
        // TODO tmp
        List<Position> beat = canBeat(piece);
        for(Position pos : beat)
        {
            cells[pos.x][pos.y].setStyle("-fx-background-color: rgb(255,0,0)");
        }

        List<Position> validPositions = getValidMoves(piece);
        if(validPositions.isEmpty())
        {
            System.out.println("No valid moves for this piece!");
            return;
        }

        Map<Position, Cell> validMoves = new HashMap<>();
        for(int i = 0; i < validPositions.size(); i++)
        {
            Position pos = validPositions.get(i);
            validMoves.put(pos, cells[pos.x][pos.y]);
        }
        List<String> styles = new ArrayList<>();


        for(Map.Entry<Position, Cell> entry : validMoves.entrySet())
        {
            Position pos = entry.getKey();
            Cell cell = entry.getValue();

            styles.add(cell.getStyle());
            cell.setStyle("-fx-background-color: rgb(88,41,20);");

            cell.setOnMouseClicked(e ->
            {
                System.out.println("clicked");

                try
                {
                    cell.getChildren().removeLast();
                }
                catch (Exception e3) {}


                Cell curCell = cells[piece.getX()][piece.getY()];
                try
                {
                    curCell.getChildren().removeLast();
                }
                catch (Exception e5) {}

                pieces[piece.getX()][piece.getY()] = null;
                piece.setX(pos.x);
                piece.setY(pos.y);
                pieces[piece.getX()][piece.getY()] = piece;
                piece.setOnMouseClicked(e4 ->
                {
                    movePiece(piece);
                });

                cell.getChildren().add(piece);

                if(!styles.isEmpty())
                {
                    for(Cell tmp : validMoves.values())
                    {
                        tmp.setStyle(styles.getLast());
                        styles.removeLast();
                        tmp.setOnMouseClicked(e2 -> {});
                    }
                }
            });
        }
    }

    private List<Position> getValidMoves(Piece piece)
    {
        List<Position> validMoves = new ArrayList<>();

        if(piece.getType() == PieceType.MAN_WHITE)
        {
            Cell newCellLeft, newCellRight;
            Position newPosLeft, newPosRight;
            if(piece.getX() - 1 >= 0 && piece.getY() - 1 >= 0)
            {
                newPosLeft = new Position(piece.getX() - 1, piece.getY() - 1);
                newCellLeft = cells[newPosLeft.x][newPosLeft.y];
                if(newCellLeft.getChildren().isEmpty()) validMoves.add(newPosLeft);
            }
            if(piece.getX() + 1 < size && piece.getY() - 1 >= 0)
            {
                newPosRight = new Position(piece.getX() + 1, piece.getY() - 1);
                newCellRight = cells[newPosRight.x][newPosRight.y];
                if(newCellRight.getChildren().isEmpty()) validMoves.add(newPosRight);
            }
        }

        if(piece.getType() == PieceType.MAN_BLACK)
        {
            Cell newCellLeft, newCellRight;
            Position newPosLeft, newPosRight;
            if(piece.getX() - 1 >= 0 && piece.getY() + 1 < size)
            {
                newPosLeft = new Position(piece.getX() - 1, piece.getY() + 1);
                newCellLeft = cells[newPosLeft.x][newPosLeft.y];
                if(newCellLeft.getChildren().isEmpty()) validMoves.add(newPosLeft);
            }
            if(piece.getX() + 1 < size && piece.getY() + 1 < size)
            {
                newPosRight = new Position(piece.getX() + 1, piece.getY() + 1);
                newCellRight = cells[newPosRight.x][newPosRight.y];
                if(newCellRight.getChildren().isEmpty()) validMoves.add(newPosRight);
            }
        }

        return validMoves;
    }

    private List<Position> canBeat(Piece piece)
    {
        List<Position> canBeat = new ArrayList<>();

        if(piece.getType() == PieceType.MAN_WHITE)
        {
            Position posBlackRight = new Position(piece.getX() - 1, piece.getY() - 1);
            Position posBlackLeft  = new Position(piece.getX() + 1, piece.getY() - 1);
            Position posFreeRight  = new Position(piece.getX() - 2, piece.getY() - 2);
            Position posFreeLeft   = new Position(piece.getX() + 2, piece.getY() - 2);
            Piece blackRight = pieces[posBlackRight.x][posBlackRight.y];
            Piece blackLeft  = pieces[posBlackLeft.x][posBlackLeft.y];
            Piece freeRight  = pieces[posFreeRight.x][posFreeRight.y];
            Piece freeLeft   = pieces[posFreeLeft.x][posFreeLeft.y];

            if(blackRight != null && freeRight == null)
            {
                canBeat.add(posFreeRight);
            }
            if(blackLeft != null && freeLeft == null)
            {
                canBeat.add(posFreeLeft);
            }
        }


        return canBeat;
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
