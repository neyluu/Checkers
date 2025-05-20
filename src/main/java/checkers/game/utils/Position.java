package checkers.game.utils;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Position
{
    public int x;
    public int y;

    public Position() {}
    public Position(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public double distanceTo(Position other)
    {
        return sqrt((pow(x - other.x, 2) + pow(y - other.y, 2)));
    }
    public double distanceTo(int x, int y)
    {
        return distanceTo(new Position(x, y));
    }
}
