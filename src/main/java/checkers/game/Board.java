package checkers.game;

import checkers.exceptions.CellHavePiece;
import checkers.exceptions.PieceNotFound;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board extends GridPane
{
    private final int boardSize = 8;
    private final int width = 600;
    private final int cellSize = width / boardSize;

    private Cell[][] cells = new Cell[boardSize][boardSize];

    public Board()
    {
        this.setMaxWidth(width);

        clearBoard();
    }

    private void clearBoard()
    {
        initCells();
        initPieces();
    }

    private void initCells()
    {
        for(int i = 0; i < boardSize; i++)
        {
            for(int j = 0; j < boardSize; j++)
            {
                Cell cell = new Cell(cellSize);
                setCellCheckboardColor(cell, i, j);
                cells[i][j] = cell;
                this.add(cell, i, j);
            }
        }
    }

    private void setCellCheckboardColor(Cell cell, int row, int col)
    {
        if((row + col) % 2 == 0)
        {
            cell.setStyle("-fx-background-color: rgb(224,188,165);");
            return;
        }

        cell.setStyle("-fx-background-color: rgb(128,81,60);");
    }

    private void initPieces()
    {
        initWhitePieces();
        initBlackPieces();
    }

    private void initWhitePieces()
    {
        for (int row = 5; row < 8; row++)
        {
            placePieceInCols(row, PieceType.MAN_WHITE);
        }
    }
    private void initBlackPieces()
    {
        for (int row = 0; row < 3; row++)
        {
            placePieceInCols(row, PieceType.MAN_BLACK);
        }
    }
    private void placePieceInCols(int row, PieceType type)
    {
        for (int col = 0; col < 8; col++)
        {
            if ((row + col) % 2 == 1)
            {
                Piece piece = new Man(cellSize - 10);
                piece.setType(type);
                piece.setX(col);
                piece.setY(row);
                cells[col][row].setPiece(piece);
            }
        }
    }

    public void movePiece(Position from, Position to)
    {
        if(!cells[from.x][from.y].havePiece())
        {
            throw new PieceNotFound("Piece not found!", from);
        }
        if(cells[to.x][to.y].havePiece())
        {
            throw new CellHavePiece("Cell is taken by another piece!", to);
        }

        Cell cellFrom = cells[from.x][from.y];
        Piece piece = cellFrom.getPiece();
        cellFrom.clearPiece();

        Cell cellTo = cells[to.x][to.y];
        piece.setX(to.x);
        piece.setY(to.y);
        cellTo.setPiece(piece);
    }

    public Map<Piece, List<Position>> getPiecesWithValidMoves(PieceType type)
    {
        Map<Piece, List<Position>> data = new HashMap<>();

        for(Cell[] cellRow : cells)
        {
            for(Cell cell : cellRow)
            {
                Piece piece = cell.getPiece();
                if(piece == null ||
                   type == PieceType.WHITE && piece.isBlack() ||
                   type == PieceType.BLACK && piece.isWhite())
                {
                   continue;
                }
                List<Position> validMoves = getValidMoves(piece);
                if(validMoves.isEmpty()) continue;
                data.put(piece, validMoves);
            }
        }

        return data;
    }

    private List<Position> getValidMoves(Piece piece)
    {
        List<Position> validMoves = new ArrayList<>();

        if(piece.isWhite()) validMoves.addAll(getDiagonalMoves(piece, -1));
        if(piece.isBlack()) validMoves.addAll(getDiagonalMoves(piece, 1));

        return validMoves;
    }
    private List<Position> getDiagonalMoves(Piece piece, int directionY)
    {
        List<Position> moves = new ArrayList<>();

        checkAndAddMove(moves, piece.getX() - 1, piece.getY() + directionY);
        checkAndAddMove(moves, piece.getX() + 1, piece.getY() + directionY);

        return moves;
    }
    private void checkAndAddMove(List<Position> moves, int x, int y)
    {
        if (isInBounds(x, y) && !cells[x][y].havePiece())
        {
            moves.add(new Position(x, y));
        }
    }
    private boolean isInBounds(int x, int y)
    {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }










//    private List<Position> getValidMoves(Piece piece)
//    {
//        List<Position> validMoves = new ArrayList<>();
//
//        if(piece.getType() == PieceType.MAN_WHITE)
//        {
//            Cell newCellLeft, newCellRight;
//            Position newPosLeft, newPosRight;
//            if(piece.getX() - 1 >= 0 && piece.getY() - 1 >= 0)
//            {
//                newPosLeft = new Position(piece.getX() - 1, piece.getY() - 1);
//                newCellLeft = cells[newPosLeft.x][newPosLeft.y];
//                if(newCellLeft.getChildren().isEmpty()) validMoves.add(newPosLeft);
//            }
//            if(piece.getX() + 1 < boardSize && piece.getY() - 1 >= 0)
//            {
//                newPosRight = new Position(piece.getX() + 1, piece.getY() - 1);
//                newCellRight = cells[newPosRight.x][newPosRight.y];
//                if(newCellRight.getChildren().isEmpty()) validMoves.add(newPosRight);
//            }
//        }
//
//        if(piece.getType() == PieceType.MAN_BLACK)
//        {
//            Cell newCellLeft, newCellRight;
//            Position newPosLeft, newPosRight;
//            if(piece.getX() - 1 >= 0 && piece.getY() + 1 < boardSize)
//            {
//                newPosLeft = new Position(piece.getX() - 1, piece.getY() + 1);
//                newCellLeft = cells[newPosLeft.x][newPosLeft.y];
//                if(newCellLeft.getChildren().isEmpty()) validMoves.add(newPosLeft);
//            }
//            if(piece.getX() + 1 < boardSize && piece.getY() + 1 < boardSize)
//            {
//                newPosRight = new Position(piece.getX() + 1, piece.getY() + 1);
//                newCellRight = cells[newPosRight.x][newPosRight.y];
//                if(newCellRight.getChildren().isEmpty()) validMoves.add(newPosRight);
//            }
//        }
//
//        return validMoves;
//    }








//    private void movePiece(Piece piece)
//    {
//        // TODO tmp
//        List<Position> beat = canBeat(piece);
//        for(Position pos : beat)
//        {
//            cells[pos.x][pos.y].setStyle("-fx-background-color: rgb(255,0,0)");
//        }
//
//
//
//
//        List<Position> validPositions = getValidMoves(piece);
//        if(validPositions.isEmpty())
//        {
//            System.out.println("No valid moves for this piece!");
//            return;
//        }
//
//        Map<Position, Cell> validMoves = new HashMap<>();
//        for(int i = 0; i < validPositions.size(); i++)
//        {
//            Position pos = validPositions.get(i);
//            validMoves.put(pos, cells[pos.x][pos.y]);
//        }
//        List<String> styles = new ArrayList<>();
//
//
//        for(Map.Entry<Position, Cell> entry : validMoves.entrySet())
//        {
//            Position pos = entry.getKey();
//            Cell cell = entry.getValue();
//
//            styles.add(cell.getStyle());
//            cell.setStyle("-fx-background-color: rgb(88,41,20);");
//
//            cell.setOnMouseClicked(e ->
//            {
//                System.out.println("clicked");
//
//                try
//                {
//                    cell.getChildren().removeLast();
//                }
//                catch (Exception e3) {}
//
//
//                Cell curCell = cells[piece.getX()][piece.getY()];
//                try
//                {
//                    curCell.getChildren().removeLast();
//                }
//                catch (Exception e5) {}
//
//                pieces[piece.getX()][piece.getY()] = null;
//                piece.setX(pos.x);
//                piece.setY(pos.y);
//                pieces[piece.getX()][piece.getY()] = piece;
//                piece.setOnMouseClicked(e4 ->
//                {
//                    movePiece(piece);
//                });
//
//                cell.getChildren().add(piece);
//
//                if(!styles.isEmpty())
//                {
//                    for(Cell tmp : validMoves.values())
//                    {
//                        tmp.setStyle(styles.getLast());
//                        styles.removeLast();
//                        tmp.setOnMouseClicked(e2 -> {});
//                    }
//                }
//            });
//        }
//    }



//    private List<Position> canBeat(Piece piece)
//    {
//        List<Position> canBeat = new ArrayList<>();
//
//        if(piece.getType() == PieceType.MAN_WHITE)
//        {
//            Position posBlackRight = new Position(piece.getX() - 1, piece.getY() - 1);
//            Position posBlackLeft  = new Position(piece.getX() + 1, piece.getY() - 1);
//            Position posFreeRight  = new Position(piece.getX() - 2, piece.getY() - 2);
//            Position posFreeLeft   = new Position(piece.getX() + 2, piece.getY() - 2);
//            Piece blackRight = pieces[posBlackRight.x][posBlackRight.y];
//            Piece blackLeft  = pieces[posBlackLeft.x][posBlackLeft.y];
//            Piece freeRight  = pieces[posFreeRight.x][posFreeRight.y];
//            Piece freeLeft   = pieces[posFreeLeft.x][posFreeLeft.y];
//
//            if(blackRight != null && freeRight == null)
//            {
//                canBeat.add(posFreeRight);
//            }
//            if(blackLeft != null && freeLeft == null)
//            {
//                canBeat.add(posFreeLeft);
//            }
//        }
//
//
//        return canBeat;
//    }

}
