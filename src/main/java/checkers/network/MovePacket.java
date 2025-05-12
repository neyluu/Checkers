package checkers.network;

import checkers.game.Position;
import java.io.Serializable;

public class MovePacket implements Serializable
{
    private static final long serialVersionUID = 1L;

    public int fromX;
    public int fromY;
    public int toX;
    public int toY;

    public boolean isBeatMove = false;
    public int beatX;
    public int beatY;

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
    public MovePacket(int fromX, int fromY, int toX, int toY, boolean isBeatMove, int beatX, int beatY)
    {
        this(fromX, fromY, toX, toY);

        this.isBeatMove = isBeatMove;
        this.beatX = beatX;
        this.beatY = beatY;
    }
    public MovePacket(Position from, Position to, boolean isBeatMove, Position beat)
    {
        this(from, to);

        this.isBeatMove = isBeatMove;
        this.beatX = beat.x;
        this.beatY = beat.y;
    }
}
