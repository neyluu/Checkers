package checkers.game;

import java.util.ArrayList;
import java.util.List;

public class Man extends Piece
{
    public Man(int size)
    {
        super(size);
        isKing = false;
    }

    @Override
    public List<Position[]> getValidMoves(Board board)
    {
        List<Position[]> validMoves = new ArrayList<>();

        if(isWhite()) validMoves.addAll(getDiagonalMoves(board, -1));
        if(isBlack()) validMoves.addAll(getDiagonalMoves(board, 1));

        return validMoves;
    }

    @Override
    public List<Position[]> getBeatMoves(Board board)
    {
        List<Position[]> beatMoves = new ArrayList<>();
        Position[] positions = new Position[2];

        if(isWhite())
        {
            Position posTopLeft1 = new Position(x - 1, y - 1);
            Position posTopLeft2 = new Position(x - 2, y - 2);
            Cell cellTopLeft1 = board.getCell(posTopLeft1);
            Cell cellTopLeft2 = board.getCell(posTopLeft2);
            if(board.isInBounds(posTopLeft1) && cellTopLeft1.havePiece() && cellTopLeft1.getPiece().isBlack() &&
                    board.isInBounds(posTopLeft2) && !cellTopLeft2.havePiece()
            )
            {
                positions[0] = posTopLeft2;
                positions[1] = posTopLeft1;
                beatMoves.add(positions);
            }

            Position posTopRight1 = new Position(x + 1, y - 1);
            Position posTopRight2 = new Position(x + 2, y - 2);
            Cell cellTopRight1 = board.getCell(posTopRight1);
            Cell cellTopRight2 = board.getCell(posTopRight2);
            if(board.isInBounds(posTopRight1) && cellTopRight1.havePiece() && cellTopRight1.getPiece().isBlack() &&
                    board.isInBounds(posTopRight2) && !cellTopRight2.havePiece()
            )
            {
                positions[0] = posTopRight2;
                positions[1] = posTopRight1;
                beatMoves.add(positions);
            }

            Position posBottomRight1 = new Position(x + 1, y + 1);
            Position posBottomRight2 = new Position(x + 2, y + 2);
            Cell cellBottomRight1 = board.getCell(posBottomRight1);
            Cell cellBottomRight2 = board.getCell(posBottomRight2);
            if(board.isInBounds(posBottomRight1) && cellBottomRight1.havePiece() && cellBottomRight1.getPiece().isBlack() &&
                    board.isInBounds(posBottomRight2) && !cellBottomRight2.havePiece()
            )
            {
                positions[0] = posBottomRight2;
                positions[1] = posBottomRight1;
                beatMoves.add(positions);
            }

            Position posBottomLeft1 = new Position(x - 1, y + 1);
            Position posBottomLeft2 = new Position(x - 2, y + 2);
            Cell cellBottomLeft1 = board.getCell(posBottomLeft1);
            Cell cellBottomLeft2 = board.getCell(posBottomLeft2);
            if(board.isInBounds(posBottomLeft1) && cellBottomLeft1.havePiece() && cellBottomLeft1.getPiece().isBlack() &&
                    board.isInBounds(posBottomLeft2) && !cellBottomLeft2.havePiece()
            )
            {
                positions[0] = posBottomLeft2;
                positions[1] = posBottomLeft1;
                beatMoves.add(positions);
            }
        }

        if(isBlack())
        {
            Position posTopLeft1 = new Position(x - 1, y + 1);
            Position posTopLeft2 = new Position(x - 2, y + 2);
            Cell cellTopLeft1 = board.getCell(posTopLeft1);
            Cell cellTopLeft2 = board.getCell(posTopLeft2);
            if(board.isInBounds(posTopLeft1) && cellTopLeft1.havePiece() && cellTopLeft1.getPiece().isWhite() &&
                    board.isInBounds(posTopLeft2) && !cellTopLeft2.havePiece()
            )
            {
                positions[0] = posTopLeft2;
                positions[1] = posTopLeft1;
                beatMoves.add(positions);
            }

            Position posTopRight1 = new Position(x + 1, y + 1);
            Position posTopRight2 = new Position(x + 2, y + 2);
            Cell cellTopRight1 = board.getCell(posTopRight1);
            Cell cellTopRight2 = board.getCell(posTopRight2);
            if(board.isInBounds(posTopRight1) && cellTopRight1.havePiece() && cellTopRight1.getPiece().isWhite() &&
                    board.isInBounds(posTopRight2) && !cellTopRight2.havePiece()
            )
            {
                positions[0] = posTopRight2;
                positions[1] = posTopRight1;
                beatMoves.add(positions);
            }

            Position posBottomRight1 = new Position(x - 1, y - 1);
            Position posBottomRight2 = new Position(x - 2, y - 2);
            Cell cellBottomRight1 = board.getCell(posBottomRight1);
            Cell cellBottomRight2 = board.getCell(posBottomRight2);
            if(board.isInBounds(posBottomRight1) && cellBottomRight1.havePiece() && cellBottomRight1.getPiece().isWhite() &&
                    board.isInBounds(posBottomRight2) && !cellBottomRight2.havePiece()
            )
            {
                positions[0] = posBottomRight2;
                positions[1] = posBottomRight1;
                beatMoves.add(positions);
            }

            Position posBottomLeft1 = new Position(x + 1, y - 1);
            Position posBottomLeft2 = new Position(x + 2, y - 2);
            Cell cellBottomLeft1 = board.getCell(posBottomLeft1);
            Cell cellBottomLeft2 = board.getCell(posBottomLeft2);
            if(board.isInBounds(posBottomLeft1) && cellBottomLeft1.havePiece() && cellBottomLeft1.getPiece().isWhite() &&
                    board.isInBounds(posBottomLeft2) && !cellBottomLeft2.havePiece()
            )
            {
                positions[0] = posBottomLeft2;
                positions[1] = posBottomLeft1;
                beatMoves.add(positions);
            }
        }

        return beatMoves;
    }

    private List<Position[]> getDiagonalMoves(Board board, int directionY)
    {
        List<Position[]> moves = new ArrayList<>();

        checkAndAddMove(board, moves, x - 1, y + directionY);
        checkAndAddMove(board, moves, x + 1, y + directionY);

        return moves;
    }
    private void checkAndAddMove(Board board, List<Position[]> moves, int x, int y)
    {
        Position[] positions = new Position[1];
        if (board.isInBounds(x, y) && !board.getCell(x, y).havePiece())
        {
            positions[0] = new Position(x, y);
            moves.add(positions);
        }
    }
}
