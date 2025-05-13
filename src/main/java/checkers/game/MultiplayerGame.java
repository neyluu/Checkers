package checkers.game;

import checkers.gui.outputs.PlayerUI;
import checkers.network.GlobalCommunication;
import checkers.network.MovePacket;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MultiplayerGame extends Game
{
    private PlayerUI player1UI;
    private PlayerUI player2UI;

    private PieceType currentTurn;
    private boolean isServer;

    public MultiplayerGame(PlayerUI player1UI, PlayerUI player2UI, boolean isServer)
    {
        this.player1UI = player1UI;
        this.player2UI = player2UI;
        this.isServer = isServer;
        currentTurn = isServer ? PieceType.WHITE : PieceType.BLACK;
    }

    private void watchTimers()
    {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r ->
        {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
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

    public void start()
    {
        if(isServer)
        {
            player2UI.startTimer();
            player2UI.highlight();

            player1UI.stopTimer();
            player1UI.unHighlight();

            startServerGame();
        }
        else
        {
            player1UI.startTimer();
            player1UI.highlight();

            player2UI.stopTimer();
            player2UI.unHighlight();

            startClientGame();
        }
    }

    private void startServerGame()
    {
        System.out.println("Starting server game");
        turn();
    }
    private void startClientGame()
    {
        System.out.println("Starting client game");
        changeTurn();
    }

    private void turn()
    {
        Map<Piece, List<Position[]>> beatMoves = board.getPiecesWithValidMoves(currentTurn, true);        createMoves(beatMoves, true);

        if(!beatMoves.isEmpty()) return;

        Map<Piece, List<Position[]>> normalMoves = board.getPiecesWithValidMoves(currentTurn, false);
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
                        Position from = new Position(piece.getX(), piece.getY());
                        board.movePiece(from, pos[0]);

                        System.out.println("sending packet");
                        if(isBeatMoves)     GlobalCommunication.communicator.sendMove(new MovePacket(from, pos[0], isBeatMoves, pos[1]));
                        else                GlobalCommunication.communicator.sendMove(new MovePacket(from, pos[0]));

                        Piece currentPiece = piece;
                        System.out.println("create moves top " + currentPiece.isTop);

                        if(currentPiece.isOnKingCells())
                        {
                            King king = new King(currentPiece.getSize(), currentPiece.getType(), currentPiece.isTop());
                            king.setX(currentPiece.getX());
                            king.setY(currentPiece.getY());
                            cell.clearPiece();
                            cell.setPiece(king);
                            currentPiece = king;
                        }

                        clearEvents(null, movesData, true);

                        if(isBeatMoves)
                        {
                            board.removePiece(pos[1]);
                            nextBeats(currentPiece);
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
        System.out.println("Waiting for move from server");

        System.out.println("black: " + board.getBlackPiecesCount());
        System.out.println("white: " + board.getWhitePieceCount());

        if(board.getBlackPiecesCount() == 0 || board.getWhitePieceCount() == 0)
        {
            gameOver();
        }


        new Thread(() ->
        {
            player2UI.stopTimer();
            player2UI.unHighlight();

            player1UI.startTimer();
            player1UI.highlight();

            MovePacket move = GlobalCommunication.communicator.getMove();
            System.out.println("Move received: " + move.fromX + " " + move.fromY + " " + move.toX + " " + move.toY);
            MovePacket translatedMove = translateMove(move);

            Platform.runLater(() ->
            {
                board.movePiece(translatedMove.fromX, translatedMove.fromY, translatedMove.toX, translatedMove.toY);

                Cell cell = board.getCell(translatedMove.toX, translatedMove.toY);
                Piece currentPiece = cell.getPiece();

                System.out.println("change turn top " + currentPiece.isTop);
                if(currentPiece.isOnKingCells())
                {
                    King king = new King(currentPiece.getSize(), currentPiece.getType(), currentPiece.isTop());
                    king.setX(currentPiece.getX());
                    king.setY(currentPiece.getY());
                    cell.clearPiece();
                    cell.setPiece(king);
                }

                if (translatedMove.isBeatMove)
                {
                    board.removePiece(translatedMove.beatX, translatedMove.beatY);

                    Piece movedPiece = board.getCell(translatedMove.toX, translatedMove.toY).getPiece();
                    List<Position[]> pieceBeatMoves = movedPiece.getBeatMoves(board);

                    if (!pieceBeatMoves.isEmpty())
                    {
                        changeTurn();
                        return;
                    }
                }

                player1UI.stopTimer();
                player1UI.unHighlight();

                player2UI.startTimer();
                player2UI.highlight();

                turn();
            });
        }).start();
    }

    private MovePacket translateMove(MovePacket move)
    {
        int boardSize = board.getSize() - 1;
        return new MovePacket(
                boardSize - move.fromX,
                boardSize - move.fromY,
                boardSize - move.toX,
                boardSize - move.toY,
                move.isBeatMove,
                boardSize - move.beatX,
                boardSize - move.beatY
        );
    }

    private void gameOver()
    {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.show();

        System.out.println("Game over");
        player1UI.stopTimer();
        player2UI.stopTimer();
    }
}
