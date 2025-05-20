package checkers.game.board;

import checkers.game.pieces.Piece;
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
        this.getChildren().remove(piece);
        piece = null;
    }

    public boolean havePiece()
    {
        if(piece == null) return false;
        return true;
    }
}
