package checkers.game;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public abstract class Piece extends Button
{
    protected boolean isKing;
    protected Player owner;
    protected String color;

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