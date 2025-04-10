package checkers.game;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class Cell extends VBox
{
    private Piece piece = null;

    public Cell(int size)
    {
        this.setMinHeight(size);
        this.setMinWidth(size);
        this.setAlignment(Pos.CENTER);
    }

    public Piece getPiece()
    {
        return piece;
    }
    public void setPiece(Piece piece)
    {
        this.piece = piece;
        this.getChildren().add(piece);
    }
    public void clearPiece()
    {
        piece = null;
        this.getChildren().remove(piece);
    }

    public boolean havePiece()
    {
        if(piece == null) return false;
        return true;
    }
}
