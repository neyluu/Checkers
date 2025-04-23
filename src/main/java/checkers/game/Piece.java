package checkers.game;

import javafx.scene.control.Button;

import java.util.List;

public abstract class Piece extends Button
{
    protected boolean isKing;
    protected Player owner;
    protected PieceType type = null;
    protected int x;
    protected int y;
    protected int size;

    public Piece(int size)
    {
        this.size = size;
        this.setMinHeight(this.size);
        this.setMinWidth(this.size);
        this.setStyle("-fx-background-radius: 50%;");
    }

    public int getSize()
    {
        return size;
    }

    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }

    public PieceType getType()
    {
        return type;
    }

    public void setX(int x)
    {
        this.x = x;
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

    public abstract List<Position[]> getValidMoves(Board board);
    public abstract List<Position[]> getBeatMoves(Board board);


//    public void promote()
//    {
//        isKing = true;
//    }
//    public boolean isKing()
//    {
//        return isKing;
//    }

    public boolean isType(PieceType type)
    {
        if(type == PieceType.WHITE) return isWhite();
        if(type == PieceType.BLACK) return isBlack();
        return false;
    }
    public boolean isWhite()
    {
        return type == PieceType.MAN_WHITE || type == PieceType.KING_WHITE;
    }
    public boolean isBlack()
    {
        return type == PieceType.MAN_BLACK || type == PieceType.KING_BLACK;
    }
    public boolean isOpposite(Piece piece)
    {
        if(isBlack() && piece.isWhite()) return true;
        if(isWhite() && piece.isBlack()) return true;
        return false;
    }
    public boolean isOnKingCells()
    {
        if(isWhite() && y == 0) return true;
        if(isBlack() && y == 7) return true;
        return false;
    }
}