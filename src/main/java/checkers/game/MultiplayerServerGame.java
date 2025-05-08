package checkers.game;

import checkers.gui.outputs.PlayerUI;
import checkers.network.GlobalCommunication;
import checkers.network.MovePacket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiplayerServerGame extends Game
{
    private PlayerUI player1UI;
    private PlayerUI player2UI;

    private PieceType currentTurn = PieceType.WHITE;

    public MultiplayerServerGame(PlayerUI player1UI, PlayerUI player2UI)
    {
        this.player1UI = player1UI;
        this.player2UI = player2UI;
    }

    public void start()
    {
        System.out.println("Game started!");
        turn();
    }

    private void turn()
    {

//        TODO CHANGE MOVES TO NOT BEATS
        Map<Piece, List<Position[]>> beatMoves = board.getPiecesWithValidMoves(currentTurn, false);
        createMoves(beatMoves, true);
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
                        Position from = new Position(piece.getX(), piece.getY());
                        board.movePiece(from, pos[0]);

                        // TODO SEND INFO TO CLIENT
                        System.out.println("sending packet");
                        GlobalCommunication.communicator.sendMove(new MovePacket(from, pos[0]));
                        // ====

                        Piece currentPiece = piece;
//
//                        if(currentPiece.isOnKingCells())
//                        {
//                            King king = new King(currentPiece.getSize(), currentPiece.getType());
//                            king.setX(currentPiece.getX());
//                            king.setY(currentPiece.getY());
//                            cell.clearPiece();
//                            cell.setPiece(king);
//                            currentPiece = king;
//                        }
//
                        clearEvents(null, movesData, true);
//
//                        if(isBeatMoves)
//                        {
//                            board.removePiece(pos[1]);
//                            nextBeats(currentPiece);
//                        }
//                        else changeTurn();
                    });
                }
            });
        }
    }

    private void nextBeats(Piece piece)
    {
        List<Position[]> pieceBeatMoves;

        pieceBeatMoves = piece.getBeatMoves(board);
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

    }

}
