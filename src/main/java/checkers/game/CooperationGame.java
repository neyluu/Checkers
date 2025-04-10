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
        // TODO MAN_WHITE will cause problems when King will be added I guess
        turn(PieceType.WHITE);
    }

    private void turn(PieceType type)
    {
        System.out.println("Turn start");

        Map<Piece, List<Position>> data = board.getPiecesWithValidMoves(type);
        if(data.isEmpty()) System.out.println("Pusto");

        for(Map.Entry<Piece, List<Position>> entry : data.entrySet())
        {
            Piece piece = entry.getKey();
            piece.setStyle("-fx-background-color: rgb(255,0,255);"); //TODO TMP
            piece.setOnMouseClicked(e ->
            {
                clearNonClickedPieces(piece, data);

                List<Position> validMoves = entry.getValue();
                for(Position pos : validMoves)
                {
                    Cell cell = board.getCell(pos.x, pos.y);
                    cell.setStyle("-fx-background-color: rgb(88,41,20);");
                    cell.setOnMouseClicked(e2 ->
                    {
                        board.movePiece(new Position(piece.getX(), piece.getY()), pos);
                        clearPiecesEvents(data);

                        if(type == PieceType.WHITE) turn(PieceType.BLACK);
                        if(type == PieceType.BLACK) turn(PieceType.WHITE);
                    });
                }
                piece.setStyle("-fx-background-color: rgb(0,255,255);"); //TODO TMP
            });
        }
    }

    private void clearNonClickedPieces(Piece clickedPiece, Map<Piece, List<Position>> data)
    {
        for(Map.Entry<Piece, List<Position>> entry : data.entrySet())
        {
            if(entry.getKey() == clickedPiece) continue;

            Piece piece = entry.getKey();
            if(piece.isWhite()) piece.setStyle("-fx-background-color: rgb(255,255,255);");
            if(piece.isBlack()) piece.setStyle("-fx-background-color: rgb(0,0,0);");

            List<Position> validMoves = entry.getValue();
            for(Position pos : validMoves)
            {
                board.getCell(pos.x, pos.y).setStyle("-fx-background-color: rgb(128,81,60);");
            }
        }
    }

    private void clearPiecesEvents(Map<Piece, List<Position>> data)
    {
        for(Map.Entry<Piece, List<Position>> entry : data.entrySet())
        {
            Piece piece = entry.getKey();
            if(piece.isWhite()) piece.setStyle("-fx-background-color: rgb(255,255,255);");
            if(piece.isBlack()) piece.setStyle("-fx-background-color: rgb(0,0,0);");

            piece.setOnMouseClicked(e -> { } );

            List<Position> validMoves = entry.getValue();
            for(Position pos : validMoves)
            {
                board.getCell(pos.x, pos.y).setStyle("-fx-background-color: rgb(128,81,60);");
                board.getCell(pos.x, pos.y).setOnMouseClicked(e2 -> { } );
            }
        }
    }
}
