package checkers.game;

import checkers.exceptions.CellHavePieceException;
import checkers.exceptions.PieceNotFoundException;
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

    public Cell getCell(int x, int y)
    {
        if(!isInBounds(x, y)) return null;
        return cells[x][y];
    }
    public Cell getCell(Position pos)
    {
        return getCell(pos.x, pos.y);
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
            throw new PieceNotFoundException("Piece not found!", from);
        }
        if(cells[to.x][to.y].havePiece())
        {
            throw new CellHavePieceException("Cell is taken by another piece!", to);
        }

        Cell cellFrom = cells[from.x][from.y];
        Piece piece = cellFrom.getPiece();
        cellFrom.clearPiece();

        Cell cellTo = cells[to.x][to.y];
        piece.setX(to.x);
        piece.setY(to.y);
        cellTo.setPiece(piece);
    }

    public void removePiece(int x, int y)
    {
        Cell cell = getCell(x, y);
        if(cell == null || !cell.havePiece())
        {
            throw new PieceNotFoundException("Piece not found!", new Position(x, y));
        }
        cell.clearPiece();
    }
    public void removePiece(Position pos)
    {
        removePiece(pos.x, pos.y);
    }


    public Map<Piece, List<Position[]>> getPiecesWithValidMoves(PieceType type)
    {
        Map<Piece, List<Position[]>> data = new HashMap<>();

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
                List<Position[]> validMoves = getValidMoves(piece);
                if(validMoves.isEmpty()) continue;
                data.put(piece, validMoves);
            }
        }

        return data;
    }

    private List<Position[]> getValidMoves(Piece piece)
    {
        List<Position[]> validMoves = new ArrayList<>();

        if(piece.isWhite()) validMoves.addAll(getDiagonalMoves(piece, -1));
        if(piece.isBlack()) validMoves.addAll(getDiagonalMoves(piece, 1));

        return validMoves;
    }
    private List<Position[]> getDiagonalMoves(Piece piece, int directionY)
    {
        List<Position[]> moves = new ArrayList<>();

        checkAndAddMove(moves, piece.getX() - 1, piece.getY() + directionY);
        checkAndAddMove(moves, piece.getX() + 1, piece.getY() + directionY);

        return moves;
    }
    private void checkAndAddMove(List<Position[]> moves, int x, int y)
    {
        Position[] positions = new Position[1];
        if (isInBounds(x, y) && !cells[x][y].havePiece())
        {
            positions[0] = new Position(x, y);
            moves.add(positions);
        }
    }

    private boolean isInBounds(int x, int y)
    {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }
    private boolean isInBounds(Position pos)
    {
        return isInBounds(pos.x, pos.y);
    }


    public Map<Piece, List<Position[]>> getPiecesThatCanBeat(PieceType type)
    {
        Map<Piece, List<Position[]>> data = new HashMap<>();

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
                List<Position[]> beatMoves = getBeatMoves(piece);
                if(beatMoves.isEmpty()) continue;
                data.put(piece, beatMoves);
            }
        }

        return data;
    }

    public List<Position[]> getBeatMoves(Piece piece)
    {
        List<Position[]> beatMoves = new ArrayList<>();
        Position[] positions = new Position[2];

        if(piece.isWhite())
        {
            Position posTopLeft1 = new Position(piece.getX() - 1, piece.getY() - 1);
            Position posTopLeft2 = new Position(piece.getX() - 2, piece.getY() - 2);
            Cell cellTopLeft1 = getCell(posTopLeft1);
            Cell cellTopLeft2 = getCell(posTopLeft2);
            if(isInBounds(posTopLeft1) && cellTopLeft1.havePiece() && cellTopLeft1.getPiece().isBlack() &&
               isInBounds(posTopLeft2) && !cellTopLeft2.havePiece()
            )
            {
                positions[0] = posTopLeft2;
                positions[1] = posTopLeft1;
                beatMoves.add(positions);
            }

            Position posTopRight1 = new Position(piece.getX() + 1, piece.getY() - 1);
            Position posTopRight2 = new Position(piece.getX() + 2, piece.getY() - 2);
            Cell cellTopRight1 = getCell(posTopRight1);
            Cell cellTopRight2 = getCell(posTopRight2);
            if(isInBounds(posTopRight1) && cellTopRight1.havePiece() && cellTopRight1.getPiece().isBlack() &&
               isInBounds(posTopRight2) && !cellTopRight2.havePiece()
            )
            {
                positions[0] = posTopRight2;
                positions[1] = posTopRight1;
                beatMoves.add(positions);
            }

            Position posBottomRight1 = new Position(piece.getX() + 1, piece.getY() + 1);
            Position posBottomRight2 = new Position(piece.getX() + 2, piece.getY() + 2);
            Cell cellBottomRight1 = getCell(posBottomRight1);
            Cell cellBottomRight2 = getCell(posBottomRight2);
            if(isInBounds(posBottomRight1) && cellBottomRight1.havePiece() && cellBottomRight1.getPiece().isBlack() &&
                isInBounds(posBottomRight2) && !cellBottomRight2.havePiece()
            )
            {
                positions[0] = posBottomRight2;
                positions[1] = posBottomRight1;
                beatMoves.add(positions);
            }

            Position posBottomLeft1 = new Position(piece.getX() - 1, piece.getY() + 1);
            Position posBottomLeft2 = new Position(piece.getX() - 2, piece.getY() + 2);
            Cell cellBottomLeft1 = getCell(posBottomLeft1);
            Cell cellBottomLeft2 = getCell(posBottomLeft2);
            if(isInBounds(posBottomLeft1) && cellBottomLeft1.havePiece() && cellBottomLeft1.getPiece().isBlack() &&
                    isInBounds(posBottomLeft2) && !cellBottomLeft2.havePiece()
            )
            {
                positions[0] = posBottomLeft2;
                positions[1] = posBottomLeft1;
                beatMoves.add(positions);
            }
        }

        if(piece.isBlack())
        {
            Position posTopLeft1 = new Position(piece.getX() - 1, piece.getY() + 1);
            Position posTopLeft2 = new Position(piece.getX() - 2, piece.getY() + 2);
            Cell cellTopLeft1 = getCell(posTopLeft1);
            Cell cellTopLeft2 = getCell(posTopLeft2);
            if(isInBounds(posTopLeft1) && cellTopLeft1.havePiece() && cellTopLeft1.getPiece().isWhite() &&
                    isInBounds(posTopLeft2) && !cellTopLeft2.havePiece()
            )
            {
                positions[0] = posTopLeft2;
                positions[1] = posTopLeft1;
                beatMoves.add(positions);
            }

            Position posTopRight1 = new Position(piece.getX() + 1, piece.getY() + 1);
            Position posTopRight2 = new Position(piece.getX() + 2, piece.getY() + 2);
            Cell cellTopRight1 = getCell(posTopRight1);
            Cell cellTopRight2 = getCell(posTopRight2);
            if(isInBounds(posTopRight1) && cellTopRight1.havePiece() && cellTopRight1.getPiece().isWhite() &&
                    isInBounds(posTopRight2) && !cellTopRight2.havePiece()
            )
            {
                positions[0] = posTopRight2;
                positions[1] = posTopRight1;
                beatMoves.add(positions);
            }

            Position posBottomRight1 = new Position(piece.getX() - 1, piece.getY() - 1);
            Position posBottomRight2 = new Position(piece.getX() - 2, piece.getY() - 2);
            Cell cellBottomRight1 = getCell(posBottomRight1);
            Cell cellBottomRight2 = getCell(posBottomRight2);
            if(isInBounds(posBottomRight1) && cellBottomRight1.havePiece() && cellBottomRight1.getPiece().isWhite() &&
                    isInBounds(posBottomRight2) && !cellBottomRight2.havePiece()
            )
            {
                positions[0] = posBottomRight2;
                positions[1] = posBottomRight1;
                beatMoves.add(positions);
            }

            Position posBottomLeft1 = new Position(piece.getX() + 1, piece.getY() - 1);
            Position posBottomLeft2 = new Position(piece.getX() + 2, piece.getY() - 2);
            Cell cellBottomLeft1 = getCell(posBottomLeft1);
            Cell cellBottomLeft2 = getCell(posBottomLeft2);
            if(isInBounds(posBottomLeft1) && cellBottomLeft1.havePiece() && cellBottomLeft1.getPiece().isWhite() &&
                    isInBounds(posBottomLeft2) && !cellBottomLeft2.havePiece()
            )
            {
                positions[0] = posBottomLeft2;
                positions[1] = posBottomLeft1;
                beatMoves.add(positions);
            }
        }

        return beatMoves;
    }
}
