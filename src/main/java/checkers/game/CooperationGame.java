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
        turn();
    }

    private void turn()
    {
        System.out.println("Turn start");

        Map<Piece, List<Position[]>> beatMoves = board.getPiecesThatCanBeat(currentTurn);
        createMoves(beatMoves, true);

        if(!beatMoves.isEmpty()) return;

        Map<Piece, List<Position[]>> normalMoves = board.getPiecesWithValidMoves(currentTurn);
        createMoves(normalMoves, false);
    }

    private void createMoves(Map<Piece, List<Position[]>> movesData, boolean isBeatMoves)
    {
        for(Map.Entry<Piece, List<Position[]>> entry : movesData.entrySet())
        {
            Piece piece = entry.getKey();

            piece.setOnMouseClicked(e ->
            {
                clearEvents(piece, movesData, false);

                List<Position[]> validMoves = entry.getValue();
                for(Position[] pos : validMoves)
                {
                    Cell cell = board.getCell(pos[0].x, pos[0].y);
                    cell.setStyle("-fx-background-color: rgb(88,41,20);");
                    cell.setOnMouseClicked(e2 ->
                    {
                        board.movePiece(new Position(piece.getX(), piece.getY()), pos[0]);
                        clearEvents(null, movesData, true);

                        if(isBeatMoves)
                        {
                            board.removePiece(pos[1]);
                            nextBeats(piece);
                        }
                        else changeTurn();
                    });
                }
            });
        }
    }

    private void nextBeats(Piece piece)
    {
        List<Position[]> pieceBeatMoves;

        pieceBeatMoves = board.getBeatMoves(piece);
        if(pieceBeatMoves.isEmpty()) changeTurn();

        Map<Piece, List<Position[]>> data = new HashMap<>();
        data.put(piece, pieceBeatMoves);

        createMoves(data, true);
    }

    private void clearEvents(Piece except, Map<Piece, List<Position[]>> data, boolean clearPiecesEvents)
    {
        for(Map.Entry<Piece, List<Position[]>> entry : data.entrySet())
        {
            if(except != null && entry.getKey() == except) continue;

            Piece piece = entry.getKey();
//            if(piece.isWhite()) piece.setStyle("-fx-background-color: rgb(255,255,255);");
//            if(piece.isBlack()) piece.setStyle("-fx-background-color: rgb(0,0,0);");

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
