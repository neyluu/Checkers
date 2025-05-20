package checkers.game.pieces;

import checkers.game.board.Board;
import checkers.game.board.Cell;
import checkers.game.utils.Position;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece
{
    public King(int size, PieceType type, boolean isTop)
    {
        super(size, type, isTop);

        if(isWhite())   textureName = "whiteKing.png";
        else            textureName = "blackKing.png";
        createStyle();
    }

    @Override
    public List<Position[]> getValidMoves(Board board)
    {
        List<Position[]> validMoves = new ArrayList<>();

        validMoves.addAll(getDiagonalMoves(board, -1, -1));
        validMoves.addAll(getDiagonalMoves(board, 1, -1));
        validMoves.addAll(getDiagonalMoves(board, -1, 1));
        validMoves.addAll(getDiagonalMoves(board, 1, 1));

        return validMoves;
    }

    @Override
    public List<Position[]> getBeatMoves(Board board)
    {
        List<Position[]> beatMoves = new ArrayList<>();

        beatMoves.addAll(getDiagonalBeatMoves(board, -1, -1));
        beatMoves.addAll(getDiagonalBeatMoves(board, 1, -1));
        beatMoves.addAll(getDiagonalBeatMoves(board, -1, 1));
        beatMoves.addAll(getDiagonalBeatMoves(board, 1, 1));

        return beatMoves;
    }

    private List<Position[]> getDiagonalMoves(Board board, int directionX, int directionY)
    {
        List<Position[]> moves = new ArrayList<>();

        for(int i = 1; i < board.getSize(); i++)
        {
            Position pos = new Position(x + i * directionX, y + i * directionY);
            Cell cell = board.getCell(pos);

            if(board.isInBounds(pos) && !cell.havePiece())
            {
                Position[] positions = new Position[2];
                positions[0] = pos;
                moves.add(positions);
            }
            else break;
        }

        return moves;
    }

    private List<Position[]> getDiagonalBeatMoves(Board board, int directionX, int directionY)
    {
        List<Position[]> moves = new ArrayList<>();

        Position firstSelf = null;
        Position firstOther = null;
        int firstOtherIndex = 1;

        for(int i = 1; i < board.getSize(); i++)
        {
            Position pos = new Position(x + i * directionX, y + i * directionY);
            Cell cell = board.getCell(pos);

            if(board.isInBounds(pos) && cell.havePiece())
            {
                if( ! cell.getPiece().isOpposite(this) && firstSelf == null) firstSelf = pos;
                if(cell.getPiece().isOpposite(this) && firstOther == null)
                {
                    firstOther = pos;
                    firstOtherIndex = i;
                }
            }
            if(firstSelf != null && firstOther != null) break;
        }

        if(firstOther == null || firstSelf != null &&
           firstSelf.distanceTo(x, y) < firstOther.distanceTo(x, y))
        {
            return List.of();
        }

        for(int i = firstOtherIndex + 1; i < board.getSize(); i++)
        {
            Position pos = new Position(x + i * directionX, y + i * directionY);
            Cell cell = board.getCell(pos);

            if( ! (board.isInBounds(pos))) break;
            if(!cell.havePiece())
            {
                Position[] positions = new Position[2];
                positions[0] = pos;
                positions[1] = firstOther;
                moves.add(positions);
            }
            else break;
        }

        return moves;
    }
}
