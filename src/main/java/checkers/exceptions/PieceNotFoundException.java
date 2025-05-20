package checkers.exceptions;

import checkers.game.utils.Position;

public class PieceNotFoundException extends RuntimeException
{
    private Position position;
    public PieceNotFoundException(String message, Position position)
    {
        super(message);
        this.position = position;
    }

    public Position getPosition()
    {
        return position;
    }
}
