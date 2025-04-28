package checkers.game;

import javafx.scene.control.Button;
import java.net.URL;
import java.util.List;

public abstract class Piece extends Button
{
    protected PieceType type = null;
    protected String textureName = "whiteMan.png";
    protected int x;
    protected int y;
    protected int size;

    public Piece(int size, PieceType type)
    {
        this.size = size;
        this.type = type;
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

    protected void createStyle()
    {
        URL url = getClass().getResource("/assets/" + textureName);
        if (url != null)
        {
            this.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-image: url('" + url.toExternalForm() + "');" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-position: center center;" +
                "-fx-background-size: cover;" +
                "-fx-background-radius: 50%;"
            );
        }
        else System.err.println("Image dont exist!");
    }

    public abstract List<Position[]> getValidMoves(Board board);
    public abstract List<Position[]> getBeatMoves(Board board);

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