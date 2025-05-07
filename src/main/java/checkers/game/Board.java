package checkers.game;

import checkers.exceptions.CellHavePieceException;
import checkers.exceptions.PieceNotFoundException;
import javafx.scene.layout.GridPane;

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

        clearBoard(false);
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

    public int getWhitePieceCount()
    {
        return countPieces(PieceType.WHITE);
    }
    public int getBlackPiecesCount()
    {
        return countPieces(PieceType.BLACK);
    }
    private int countPieces(PieceType type)
    {
        int counter = 0;

        for(int i = 0; i < boardSize; i++)
        {
            for(int j = 0; j < boardSize; j++)
            {
                Cell cell = getCell(i, j);
                if(cell.havePiece() && cell.getPiece().isType(type)) counter++;
            }
        }

        return counter;
    }

    public boolean isInBounds(int x, int y)
    {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }
    public boolean isInBounds(Position pos)
    {
        return isInBounds(pos.x, pos.y);
    }

    public void clearBoard(boolean inverted)
    {
        initCells();
        initPieces(inverted);
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

    private void initPieces(boolean inverted)
    {
        initBottomPieces(inverted);
        initTopPieces(inverted);
    }

    private void initBottomPieces(boolean inverted)
    {
        for (int row = 5; row < 8; row++)
        {
            placePieceInCols(row, inverted ? PieceType.MAN_BLACK : PieceType.MAN_WHITE);
        }
    }
    private void initTopPieces(boolean inverted)
    {
        for (int row = 0; row < 3; row++)
        {
            placePieceInCols(row, inverted ? PieceType.MAN_WHITE : PieceType.MAN_BLACK);
        }
    }
    private void placePieceInCols(int row, PieceType type)
    {
        for (int col = 0; col < 8; col++)
        {
            if ((row + col) % 2 == 1)
            {
                Piece piece = new Man(cellSize, type);
                piece.setX(col);
                piece.setY(row);
                cells[col][row].setPiece(piece);
            }
        }
    }

    public void movePiece(int fromX, int fromY, int toX, int toY)
    {
        movePiece(new Position(fromX, fromY), new Position(toX, toY));
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


    public Map<Piece, List<Position[]>> getPiecesWithValidMoves(PieceType type, boolean isBeatMoves)
    {
        Map<Piece, List<Position[]>> data = new HashMap<>();

        for(Cell[] cellsRow : cells)
        {
            for(Cell cell : cellsRow)
            {
                Piece piece = cell.getPiece();
                if(piece == null ||
                   type == PieceType.WHITE && piece.isBlack() ||
                   type == PieceType.BLACK && piece.isWhite())
                {
                    continue;
                }

                List<Position[]> validMoves;

                if(isBeatMoves) validMoves = piece.getBeatMoves(this);
                else            validMoves = piece.getValidMoves(this);

                if(validMoves.isEmpty()) continue;
                data.put(piece, validMoves);
            }
        }

        return data;
    }
}
