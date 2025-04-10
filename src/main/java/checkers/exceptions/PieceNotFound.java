package checkers.exceptions;

import checkers.game.Position;

public class PieceNotFound extends RuntimeException
{
    private Position position;
    public PieceNotFound(String message, Position position)
    {
        super(message);
        this.position = position;
    }

    public Position getPosition()
    {
        return position;
    }
}
