package checkers.exceptions;

import checkers.game.Position;

public class CellHavePiece extends RuntimeException
{
    private Position position;
    public CellHavePiece(String message, Position position) {
        super(message);
    }

    public Position getPosition()
    {
      return position;
    }
}
