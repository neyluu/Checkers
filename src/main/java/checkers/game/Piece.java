package checkers.game;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public abstract class Piece extends Button
{
    protected boolean isKing;
    protected Player owner;
    protected PieceType type = null;
    protected int x;
    protected int y;

    public Piece(int size)
    {
        this.setMinHeight(size);
        this.setMinWidth(size);
        this.setStyle("-fx-background-radius: 50%;");
    }

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
        if(type == PieceType.MAN_WHITE || type == PieceType.KING_WHITE)
        {
            this.setStyle("-fx-background-color: rgb(255,255,255);" + this.getStyle());
        }
        else
        {
            this.setStyle("-fx-background-color: rgb(0,0,0);" + this.getStyle());
        }
    }
    public PieceType getType()
    {
        return type;
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

    public boolean isWhite()
    {
        return type == PieceType.MAN_WHITE || type == PieceType.KING_WHITE;
    }
    public boolean isBlack()
    {
        return type == PieceType.MAN_BLACK || type == PieceType.KING_BLACK;
    }
}