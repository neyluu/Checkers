package checkers.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CooperationGame extends Game
{
    private PieceType currentTurn = PieceType.WHITE;

    public CooperationGame()
    {
        super();
        start();
    }

    private void start()
    {
        // TODO MAN_WHITE will cause problems when King will be added I guess
        turn();
    }

    private void turn()
    {
        System.out.println("Turn start");


        Map<Piece, List<Position[]>> getBeats = board.getPiecesThatCanBeat(currentTurn);
        for(Map.Entry<Piece, List<Position[]>> entry : getBeats.entrySet())
        {
            Piece piece = entry.getKey();

            piece.setOnMouseClicked(e ->
            {
                clearEvents(piece, getBeats, false);

                List<Position[]> validMoves = entry.getValue();
                for(Position[] pos : validMoves)
                {
                    Cell cell = board.getCell(pos[0].x, pos[0].y);
                    cell.setStyle("-fx-background-color: rgb(150,41,20);");
                    cell.setOnMouseClicked(e2 ->
                    {
                        board.movePiece(new Position(piece.getX(), piece.getY()), pos[0]);
                        board.removePiece(pos[1]);
                        clearEvents(null, getBeats, true);

                        changeTurn();
                    });
                }
            });
        }


        if(!getBeats.isEmpty()) return;

        Map<Piece, List<Position[]>> data = board.getPiecesWithValidMoves(currentTurn);
        if(data.isEmpty()) System.out.println("Pusto");

        for(Map.Entry<Piece, List<Position[]>> entry : data.entrySet())
        {
            Piece piece = entry.getKey();
//            piece.setStyle("-fx-background-color: rgb(255,0,255);"); //TODO TMP
            piece.setOnMouseClicked(e ->
            {
                clearEvents(piece, data, false);

                List<Position[]> validMoves = entry.getValue();
                for(Position[] pos : validMoves)
                {
                    Cell cell = board.getCell(pos[0].x, pos[0].y);
                    cell.setStyle("-fx-background-color: rgb(88,41,20);");
                    cell.setOnMouseClicked(e2 ->
                    {
                        board.movePiece(new Position(piece.getX(), piece.getY()), pos[0]);
                        clearEvents(null, data, true);

                        changeTurn();
                    });
                }
//                piece.setStyle("-fx-background-color: rgb(0,255,255);"); //TODO TMP
            });
        }
    }

//    private void clearEvents(Piece except, Map<Piece, List<Position>> data, boolean clearPiecesEvents)
//    {
//        for(Map.Entry<Piece, List<Position>> entry : data.entrySet())
//        {
//            if(except != null && entry.getKey() == except) continue;
//
//            Piece piece = entry.getKey();
//            if(piece.isWhite()) piece.setStyle("-fx-background-color: rgb(255,255,255);");
//            if(piece.isBlack()) piece.setStyle("-fx-background-color: rgb(0,0,0);");
//
//            if(clearPiecesEvents) piece.setOnMouseClicked(e -> { } );
//
//            List<Position> validMoves = entry.getValue();
//            for(Position pos : validMoves)
//            {
//                board.getCell(pos.x, pos.y).setStyle("-fx-background-color: rgb(128,81,60);");
//                board.getCell(pos.x, pos.y).setOnMouseClicked(e2 -> { } );
//            }
//        }
//    }
    private void clearEvents(Piece except, Map<Piece, List<Position[]>> data, boolean clearPiecesEvents)
    {
        for(Map.Entry<Piece, List<Position[]>> entry : data.entrySet())
        {
            if(except != null && entry.getKey() == except) continue;

            Piece piece = entry.getKey();
            if(piece.isWhite()) piece.setStyle("-fx-background-color: rgb(255,255,255);");
            if(piece.isBlack()) piece.setStyle("-fx-background-color: rgb(0,0,0);");

            if(clearPiecesEvents) piece.setOnMouseClicked(e -> { } );

            List<Position[]> validMoves = entry.getValue();
            for(Position[] pos : validMoves)
            {
                board.getCell(pos[0].x, pos[0].y).setStyle("-fx-background-color: rgb(128,81,60);");
                board.getCell(pos[0].x, pos[0].y).setOnMouseClicked(e2 -> { } );
            }
        }
    }

    private void changeTurn()
    {
        if(currentTurn == PieceType.WHITE) currentTurn = PieceType.BLACK;
        else if(currentTurn == PieceType.BLACK) currentTurn = PieceType.WHITE;
        turn();
    }
}
