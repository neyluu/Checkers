package checkers.game;

import checkers.game.board.Cell;
import checkers.game.pieces.Piece;
import checkers.game.pieces.PieceType;
import checkers.game.utils.Position;
import checkers.gui.outputs.PlayerUI;
import checkers.gui.popups.ConnectionLostAlert;
import checkers.gui.popups.GameOverAlert;
import checkers.network.GlobalCommunication;
import checkers.network.MovePacket;
import checkers.network.ServerState;
import checkers.scenes.utils.SceneManager;
import checkers.scenes.utils.SceneType;
import javafx.application.Platform;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;

public class MultiplayerGame extends Game
{
    private GameOverAlert gameOverAlert = new GameOverAlert();
    private ConnectionLostAlert connectionLostAlert = new ConnectionLostAlert();

    private PieceType winner;
    private boolean isServer;
    private boolean isClosed = false;

    private volatile boolean isRunning;
    private final Object communicationMutex = new Object();

    public MultiplayerGame(PlayerUI player1UI, PlayerUI player2UI, boolean isServer)
    {
        super(player1UI, player2UI);
        this.isServer = isServer;
        currentTurn = isServer ? PieceType.WHITE : PieceType.BLACK;

        createGameOverAlert();
    }

    @Override
    protected void onMove(Position from, Position to, Position beat, boolean isBeatMoves)
    {
        System.out.println("Sending move packet");

        try
        {
            if(isBeatMoves)     GlobalCommunication.communicator.sendMove(new MovePacket(from, to, isBeatMoves, beat));
            else                GlobalCommunication.communicator.sendMove(new MovePacket(from, to));
        }
        catch(SocketException e)
        {
            System.err.println("Connection lost!");
            close();
            return;
        }
        catch(IOException e)
        {
            System.err.println("Failed to send data!");
//            e.printStackTrace();
        }
    }

    @Override
    public void start()
    {
        isRunning = true;
        isClosed = false;

        if(isServer)
        {
            uiPlayer2Turn();
            startServerGame();
        }
        else
        {
            uiPlayer1Turn();
            startClientGame();
        }

        watchTimers();
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

    public void reset()
    {
        resetScheduler();
        board.clearBoard( ! isServer);
        player1UI.resetTimer();
        player2UI.resetTimer();
        currentTurn = isServer ? PieceType.WHITE : PieceType.BLACK;
    }

    private void close()
    {
        if(isClosed) return;
        isClosed = true;

        System.out.println("Closing...");
        Platform.runLater(() -> connectionLostAlert.show());
    }

    private void watchTimers()
    {
        if(timersScheduler == null)
        {
            createTimersScheduler(() ->
            {
                boolean isTimerPlayer1Finished = player1UI.isTimerFinished();
                boolean isTimerPlayer2Finished = player2UI.isTimerFinished();

                if(isTimerPlayer1Finished || isTimerPlayer2Finished)
                {
                    winner = isServer ?
                            (isTimerPlayer1Finished ? PieceType.WHITE : PieceType.BLACK) :
                            (isTimerPlayer1Finished ? PieceType.BLACK : PieceType.WHITE);
                    gameOver();
                    timersScheduler.shutdown();
                }
            });
        }
    }

    public void changeTurn()
    {
        System.out.println("Waiting for move");

        checkGameOverAtNoPieces();

        Thread changeTurnThread = new Thread(() ->
        {
            if(!isRunning) return;

            uiPlayer1Turn();

            Object receivedObject = null;
            synchronized (communicationMutex)
            {
                try
                {
                    receivedObject  = GlobalCommunication.communicator.getObject();
                    if(receivedObject == null)
                    {
                        System.err.println("Connection lost!");
                        close();
                        return;
                    }
                }
                catch(SocketException e)
                {
                    System.err.println("Connection lost!");
                    close();
                    return;
                }
                catch(IOException | ClassNotFoundException e)
                {
                    System.err.println("Failed to get packet!");
//                    e.printStackTrace();
                }
            }

            if(receivedObject instanceof ServerState)
            {
                playAgainClient();
                return;
            }

            MovePacket move = (MovePacket) receivedObject ;
            System.out.println("Move received: " + move.fromX + " " + move.fromY + " " + move.toX + " " + move.toY);
            MovePacket translatedMove = translateMove(move);
            handleReceivedMove(translatedMove);
        });

        changeTurnThread.setDaemon(true);
        changeTurnThread.start();
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
        timersScheduler.shutdownNow();
        isRunning = false;

        Platform.runLater(() ->
        {
            System.out.println("Game over");
            System.out.println("Winner:" + winner);

            player1UI.stopTimer();
            player2UI.stopTimer();

            gameOverAlert.setInfo(
                (winner == PieceType.WHITE
                ? (isServer ? player2UI.getUsername() : player1UI.getUsername())
                : (isServer ? player1UI.getUsername() : player2UI.getUsername())
                ) + " won!"
            );
            gameOverAlert.show();


            if(!isServer)
            {
                Thread clientPlayAgainThread = new Thread(() ->
                {
                    System.out.println("Waiting for game resetting info");

                    Object recievedObject = null;
                    synchronized (communicationMutex)
                    {
                        try
                        {
                            recievedObject = GlobalCommunication.communicator.getObject();
                            if(recievedObject == null)
                            {
                                System.err.println("Connection lost!");
                                close();
                                return;
                            }
                        }
                        catch(SocketException e)
                        {
                            System.err.println("Connection lost!");
                            close();
                            return;
                        }
                        catch(IOException | ClassNotFoundException e)
                        {
                            System.err.println("Failed to get packet!");
//                            e.printStackTrace();
                        }
                    }

                    if(recievedObject instanceof MovePacket)
                    {
                        MovePacket translatedMove = translateMove((MovePacket) recievedObject);
                        handleReceivedMove(translatedMove);
                        return;
                    }

                    ServerState state = (ServerState) recievedObject;
                    System.out.println("State: " + String.valueOf(state));

                    if(state == ServerState.GAME_START) playAgainClient();
                });

                clientPlayAgainThread.setDaemon(true);
                clientPlayAgainThread.start();
            }
        });
    }

    private void playAgainClient()
    {
        Platform.runLater(() ->
        {
            System.out.println("Resetting game");
            gameOverAlert.hide();
            reset();
            start();
        });
    }

    private void playAgainServer()
    {
        System.out.println("Sending game reset info");

        try
        {
            GlobalCommunication.communicator.sendState(ServerState.GAME_START);
        }
        catch(SocketException e)
        {
            System.err.println("Connection lost!");
            close();
            return;
        }
        catch(IOException e)
        {
            System.err.println("Failed to send state!");
//            e.printStackTrace();
        }

        System.out.println("Sent");
        reset();
        start();
    }

    private void createGameOverAlert()
    {
        System.out.println("CREATING GAME OVER ALERT!");

        gameOverAlert.setEventOnPlayAgain(e ->
        {
            gameOverAlert.hide();
            playAgainServer();
        });
        gameOverAlert.setEventOnQuit(e ->
        {
            Platform.exit();
        });
        gameOverAlert.setEventOnQuitMainMenu(e ->
        {
            GlobalCommunication.communicator.close();
            SceneManager.getInstance().setScene(SceneType.MAIN_MENU);
        });

        if(!isServer) gameOverAlert.removePlayAgainButton();
    }

    private void handleReceivedMove(MovePacket move)
    {
        Platform.runLater(() ->
        {
            board.movePiece(move.fromX, move.fromY, move.toX, move.toY);

            Cell cell = board.getCell(move.toX, move.toY);
            Piece currentPiece = cell.getPiece();
            currentPiece = tryPromoteToKing(currentPiece, cell);

            if(move.isBeatMove || promoted)
            {
                if(move.isBeatMove) board.removePiece(move.beatX, move.beatY);
                checkGameOverAtNoPieces();

                List<Position[]> pieceBeatMoves = currentPiece.getBeatMoves(board);

                if(!pieceBeatMoves.isEmpty())
                {
                    changeTurn();
                    return;
                }
            }

            uiPlayer2Turn();
            turn();
        });
    }
}
