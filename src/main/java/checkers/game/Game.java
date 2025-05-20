package checkers.game;

import checkers.game.board.Board;
import checkers.game.board.Cell;
import checkers.game.pieces.King;
import checkers.game.pieces.Piece;
import checkers.game.pieces.PieceType;
import checkers.game.utils.Position;
import checkers.gui.outputs.PlayerUI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class Game implements GameStarter
{
    final protected Board board = new Board();

    protected boolean promoted = false;

    protected PlayerUI player1UI;
    protected PlayerUI player2UI;
    protected PieceType currentTurn;

    protected ScheduledExecutorService timersScheduler = null;

    public Game(PlayerUI player1UI, PlayerUI player2UI)
    {
        this.player1UI = player1UI;
        this.player2UI = player2UI;
    }

    @Override
    public Board getBoard()
    {
        return board;
    }

    protected void uiPlayer1Turn()
    {
        player1UI.startTimer();
        player1UI.highlight();

        player2UI.stopTimer();
        player2UI.unHighlight();
    }

    protected void uiPlayer2Turn()
    {
        player2UI.startTimer();
        player2UI.highlight();

        player1UI.stopTimer();
        player1UI.unHighlight();
    }

    protected void createTimersScheduler(Runnable event)
    {
        timersScheduler = Executors.newSingleThreadScheduledExecutor(r ->
        {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });

        timersScheduler.scheduleAtFixedRate(event, 0, 1000, TimeUnit.MILLISECONDS);
    }

    protected void resetScheduler()
    {
        timersScheduler.shutdown();
        timersScheduler = null;
    }

    protected void turn()
    {
        Map<Piece, List<Position[]>> beatMoves = board.getPiecesWithValidMoves(currentTurn, true);
        createMoves(beatMoves, true);

        if(!beatMoves.isEmpty()) return;

        Map<Piece, List<Position[]>> normalMoves = board.getPiecesWithValidMoves(currentTurn, false);

        //        if(beatMoves.isEmpty() && normalMoves.isEmpty()) gameOver();

        createMoves(normalMoves, false);
    }

    protected void createMoves(Map<Piece, List<Position[]>> movesData, boolean isBeatMoves)
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

                        onMove(from, pos[0], isBeatMoves ? pos[1] : null, isBeatMoves);

                        Piece currentPiece = piece;

                        currentPiece = tryPromoteToKing(currentPiece, cell);
                        clearEvents(null, movesData, true);

                        if(isBeatMoves || promoted)
                        {
                            if(isBeatMoves) board.removePiece(pos[1]);
                            nextBeats(currentPiece);
                        }
                        else changeTurn();
                    });
                }
            });
        }
    }

    protected void clearEvents(Piece except, Map<Piece, List<Position[]>> data, boolean clearPiecesEvents)
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

    protected void nextBeats(Piece piece)
    {
        List<Position[]> pieceBeatMoves;

        pieceBeatMoves = piece.getBeatMoves(board);
        if(pieceBeatMoves.isEmpty()) changeTurn();

        Map<Piece, List<Position[]>> data = new HashMap<>();
        data.put(piece, pieceBeatMoves);

        createMoves(data, true);
    }

    protected Piece tryPromoteToKing(Piece piece, Cell cell)
    {
        if(piece.isOnKingCells())
        {
            promoted = true;

            King king = new King(piece.getSize(), piece.getType(), piece.isTop());
            king.setX(piece.getX());
            king.setY(piece.getY());
            cell.clearPiece();
            cell.setPiece(king);
            return king;
        }

        promoted = false;
        return piece;
    }

    abstract protected void changeTurn();
    abstract protected void onMove(Position from, Position to, Position beat, boolean isBeatMoves);
}
