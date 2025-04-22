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

    public int getSize()
    {
        return boardSize;
    }

    public int getCellSize()
    {
        return cellSize;
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

    private void setCellCheckboardColor(Cell cell, int row, int col)
    {
        if((row + col) % 2 == 0)
        {
            cell.setStyle("-fx-background-color: rgb(224,188,165);");
            return;
        }

        cell.setStyle("-fx-background-color: rgb(128,81,60);");
    }

    public boolean isInBounds(int x, int y)
    {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }
    public boolean isInBounds(Position pos)
    {
        return isInBounds(pos.x, pos.y);
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
                List<Position[]> validMoves = piece.getValidMoves(this);
                if(validMoves.isEmpty()) continue;
                data.put(piece, validMoves);
            }
        }

        return data;
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
                List<Position[]> beatMoves = piece.getBeatMoves(this);
                if(beatMoves.isEmpty()) continue;
                data.put(piece, beatMoves);
            }
        }

        return data;
    }

}
