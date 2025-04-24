package checkers.game;

import checkers.gui.outputs.PlayerUI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CooperationGame extends Game
{
    private final Object lock;
    private PieceType currentTurn = PieceType.WHITE;

    private PlayerUI player1UI;
    private PlayerUI player2UI;

    public CooperationGame(Object lock, PlayerUI player1UI, PlayerUI player2UI)
    {
        this.lock = lock;
        this.player1UI = player1UI;
        this.player2UI = player2UI;
    }

    public void start()
    {
        player2UI.startTimer();
        player1UI.stopTimer();
        player2UI.highlight();
        watchTimers();
        turn();
    }

    private void watchTimers()
    {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() ->
        {
            if (player1UI.isTimerFinished() || player2UI.isTimerFinished())
            {
                if (currentTurn == PieceType.WHITE) currentTurn = PieceType.BLACK;
                else if (currentTurn == PieceType.BLACK) currentTurn = PieceType.WHITE;
                gameOver();
                scheduler.shutdown();
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    public PieceType getCurrentTurn()
    {
        return currentTurn;
    }

    private void turn()
    {
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
                        System.out.println(pos[0].x + " " + pos[0].y);
                        if(piece.isOnKingCells())
                        {
                            King king = new King(piece.getSize(), piece.getType());
                            king.setX(piece.getX());
                            king.setY(piece.getY());
                            cell.clearPiece();
                            cell.setPiece(king);
                        }

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
        if(currentTurn == PieceType.WHITE)
        {
            if(board.getBlackPiecesCount() == 0)
            {
                gameOver();
                return;
            }
            currentTurn = PieceType.BLACK;
            player1UI.startTimer();
            player2UI.stopTimer();
            player1UI.highlight();
            player2UI.unHighlight();
            System.out.println("Black turn");
        }
        else if(currentTurn == PieceType.BLACK)
        {
            if(board.getWhitePieceCount() == 0)
            {
                gameOver();
                return;
            }
            currentTurn = PieceType.WHITE;
            player2UI.startTimer();
            player1UI.stopTimer();
            player2UI.highlight();
            player1UI.unHighlight();
            System.out.println("White turn");
        }
        turn();
    }

    private void gameOver()
    {
        synchronized(lock)
        {
            lock.notify();
        }
    }
}
