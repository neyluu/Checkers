package checkers.game;

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
        return List.of();
    }

    @Override
    public List<Position[]> getBeatMoves(Board board)
    {
        return List.of();
    }

}
