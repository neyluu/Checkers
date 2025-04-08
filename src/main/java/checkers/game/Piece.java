package checkers.game;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public abstract class Piece extends Button
{
    protected boolean isKing;
    protected Player owner;
    protected String color;
    protected PieceType type = null;
    protected int x;
    protected int y;

    public int getX()
    {
        return x;
    }
    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }
    public void setY(int y)
    {
        this.y = y;
    }

    public void setType(PieceType type)
    {
        this.type = type;
    }
    public PieceType getType()
    {
        return type;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    //    public abstract List<Move> getValidMoves(Position from, Board board);
    public void promote()
    {
        isKing = true;
    }
    public boolean isKing()
    {
        return isKing;
    }
}