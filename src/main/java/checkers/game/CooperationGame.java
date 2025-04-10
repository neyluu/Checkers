package checkers.game;

import java.util.List;
import java.util.Map;

public class CooperationGame extends Game
{

    public CooperationGame()
    {
        super();
        start();
    }

    private void start()
    {
        // TODO MAN_WHITE will couse problems when King will be added I guess
        turn(PieceType.WHITE);
    }

    private void turn(PieceType type)
    {
        System.out.println("Turn start");

        Map<Piece, List<Position>> data = board.getPiecesWithValidMoves(type);
        if(data.isEmpty())
        {
            System.out.println("Pusto");
        }
        for(Map.Entry<Piece, List<Position>> entry : data.entrySet())
        {
            Piece piece = entry.getKey();
            piece.setStyle("-fx-background-color: rgb(255,0,255);"); //TODO TMP
            piece.setOnMouseClicked(e ->
            {
                piece.setStyle("-fx-background-color: rgb(0,255,255);"); //TODO TMP
            });
        }
    }
}
