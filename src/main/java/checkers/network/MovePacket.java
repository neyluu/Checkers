package checkers.network;

import checkers.game.Position;
import java.io.Serializable;

public class MovePacket implements Serializable
{
    public int fromX;
    public int fromY;
    public int toX;
    public int toY;

    public MovePacket(Position from, Position to)
    {
        this(from.x, from.y, to.x, to.y);
    }
    public MovePacket(int fromX, int fromY, int toX, int toY)
    {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }
}
