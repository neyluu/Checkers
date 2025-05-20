package checkers.exceptions;

import checkers.game.utils.Position;

public class CellHavePieceException extends RuntimeException
{
    private Position position;
    public CellHavePieceException(String message, Position position) {
        super(message);
    }

    public Position getPosition()
    {
      return position;
    }
}
