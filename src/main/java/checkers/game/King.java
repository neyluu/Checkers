package checkers.game;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece
{
    public King(int size)
    {
        super(size);
        isKing = true;
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

    @Override
    public List<Position[]> getBeatMoves(Board board)
    {
        return List.of();
    }

}
