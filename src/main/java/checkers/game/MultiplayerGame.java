package checkers.game;

import checkers.gui.outputs.PlayerUI;
import checkers.network.GlobalCommunication;
import checkers.network.MovePacket;
import checkers.network.ServerState;
import checkers.scenes.utils.SceneManager;
import checkers.scenes.utils.SceneType;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MultiplayerGame extends Game
{
    private Alert gameOverAlert;
    private PlayerUI player1UI;
    private PlayerUI player2UI;

    private PieceType winner;
    private PieceType currentTurn;
    private boolean isServer;

    private volatile boolean isRunning;
    private ScheduledExecutorService scheduler;
    private final Object communicationMutex = new Object();

    public MultiplayerGame(PlayerUI player1UI, PlayerUI player2UI, boolean isServer)
    {
        this.player1UI = player1UI;
        this.player2UI = player2UI;
        this.isServer = isServer;
        currentTurn = isServer ? PieceType.WHITE : PieceType.BLACK;
    }

    public void start()
    {
        isRunning = true;

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

        watchTimers();
    }

    private void watchTimers()
    {
        if(scheduler != null && !scheduler.isTerminated()) scheduler.shutdownNow();

        scheduler = Executors.newSingleThreadScheduledExecutor(r ->
        {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });

        scheduler.scheduleAtFixedRate(() ->
        {
            boolean isTimerPlayer1Finished = player1UI.isTimerFinished();
            boolean isTimerPlayer2Finished = player2UI.isTimerFinished();

            if(isTimerPlayer1Finished || isTimerPlayer2Finished)
            {
                winner = isServer ?
                        (isTimerPlayer1Finished ? PieceType.WHITE : PieceType.BLACK) :
                        (isTimerPlayer1Finished ? PieceType.BLACK : PieceType.WHITE);
                gameOver();
                scheduler.shutdown();
            }

        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    public void reset()
    {
        board.clearBoard( ! isServer);
        player1UI.resetTimer();
        player2UI.resetTimer();
        currentTurn = isServer ? PieceType.WHITE : PieceType.BLACK;
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

//        if(beatMoves.isEmpty() && normalMoves.isEmpty()) gameOver();

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

                        System.out.println("Sending move packet");
                        if(isBeatMoves)     GlobalCommunication.communicator.sendMove(new MovePacket(from, pos[0], isBeatMoves, pos[1]));
                        else                GlobalCommunication.communicator.sendMove(new MovePacket(from, pos[0]));

                        Piece currentPiece = piece;

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
        System.out.println("Waiting for move");

        checkGameOverAtNoPieces();

        new Thread(() ->
        {
            player2UI.stopTimer();
            player2UI.unHighlight();

            player1UI.startTimer();
            player1UI.highlight();

            if(!isRunning) return;

            Object receivedObject;
            synchronized (communicationMutex)
            {
                receivedObject  = GlobalCommunication.communicator.getObject();
            }

            if(receivedObject instanceof ServerState)
            {
                System.out.println("change turn object is serverstate");
                Platform.runLater(() ->
                {
                    System.out.println("Resetting game");
                    gameOverAlert.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                    gameOverAlert.close();
                    reset();
                    start();
                });
                return;
            }

            MovePacket move = (MovePacket) receivedObject ;

            System.out.println("Move received: " + move.fromX + " " + move.fromY + " " + move.toX + " " + move.toY);
            MovePacket translatedMove = translateMove(move);

            Platform.runLater(() ->
            {
                board.movePiece(translatedMove.fromX, translatedMove.fromY, translatedMove.toX, translatedMove.toY);

                Cell cell = board.getCell(translatedMove.toX, translatedMove.toY);
                Piece currentPiece = cell.getPiece();

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

                    checkGameOverAtNoPieces();

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

    private void checkGameOverAtNoPieces()
    {
        if(board.getBlackPiecesCount() == 0)
        {
            winner = PieceType.WHITE;
            gameOver();
        }
        if(board.getWhitePieceCount() == 0)
        {
            winner = PieceType.BLACK;
            gameOver();
        }
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
        scheduler.shutdownNow();
        isRunning = false;

        Platform.runLater(() ->
        {

            System.out.println("Game over");
            player1UI.stopTimer();
            player2UI.stopTimer();

            System.out.println("winner:" + winner);

            ButtonType playAgain = new ButtonType("PlayAgain", ButtonBar.ButtonData.OK_DONE);
            ButtonType quit = new ButtonType("Quit", ButtonBar.ButtonData.OK_DONE);
            ButtonType quitMainMenu = new ButtonType("Quit to main menu", ButtonBar.ButtonData.OK_DONE);

            gameOverAlert = new Alert(Alert.AlertType.NONE);
            gameOverAlert.setTitle("");
            gameOverAlert.setHeaderText("Game finished!");

            gameOverAlert.setContentText(
                    (winner == PieceType.WHITE
                            ? (isServer ? player2UI.getUsername() : player1UI.getUsername())
                            : (isServer ? player1UI.getUsername() : player2UI.getUsername())
                    ) + " wins!"
            );


            if(isServer)    gameOverAlert.getButtonTypes().addAll(playAgain, quit, quitMainMenu);
            else            gameOverAlert.getButtonTypes().setAll(quit, quitMainMenu);

            if(!isServer)
            {
                new Thread(() ->
                {
                    System.out.println("Getting game reset info");

                    Object recievedObject;

                    synchronized (communicationMutex)
                    {
                        recievedObject = GlobalCommunication.communicator.getObject();
                    }

                    if(recievedObject instanceof MovePacket)
                    {
                        System.out.println("gameover object is movepacket");


                        MovePacket translatedMove = translateMove((MovePacket) recievedObject);

                        Platform.runLater(() ->
                        {
                            board.movePiece(translatedMove.fromX, translatedMove.fromY, translatedMove.toX, translatedMove.toY);

                            Cell cell = board.getCell(translatedMove.toX, translatedMove.toY);
                            Piece currentPiece = cell.getPiece();

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

                                checkGameOverAtNoPieces();

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




                        return;
                    }

                    ServerState state = (ServerState) recievedObject;
//                    ServerState state = GlobalCommunication.communicator.getState();
                    System.out.println("State: " + String.valueOf(state));
                    if(state == ServerState.GAME_START)
                    {
                        Platform.runLater(() ->
                        {
                            System.out.println("Resetting game");
                            gameOverAlert.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                            gameOverAlert.close();
                            reset();
                            start();
                        });
                    }
                }).start();
            }

            gameOverAlert.show();

            gameOverAlert.setOnHidden(e ->
            {
                ButtonType gameOverAlertResult = gameOverAlert.getResult();

                if(gameOverAlertResult.equals(quit))
                {
                    Platform.exit();
                }
                if(gameOverAlertResult.equals(quitMainMenu))
                {
                    SceneManager.getInstance().setScene(SceneType.MAIN_MENU);
                }
                if(gameOverAlertResult.equals(playAgain))
                {
                    if(isServer)
                    {
                        System.out.println("Sending game reset info");
                        GlobalCommunication.communicator.sendState(ServerState.GAME_START);
                        System.out.println("Sent");
                        reset();
                        start();
                    }
                }
            });
        });
    }
}
