package checkers.game;

import checkers.game.board.Cell;
import checkers.game.pieces.Piece;
import checkers.game.pieces.PieceType;
import checkers.game.replays.GameSaver;
import checkers.game.utils.Position;
import checkers.gui.outputs.PlayerUI;
import checkers.gui.popups.ConnectionLostAlert;
import checkers.gui.popups.GameOverAlert;
import checkers.logging.AppLogger;
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

    private final GameSaver gameSaver = GameSaver.get();
    private final AppLogger logger = new AppLogger(MultiplayerGame.class);

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
        logger.info("Sending move packet...");

        try
        {
            if(isBeatMoves)     GlobalCommunication.communicator.sendMove(new MovePacket(from, to, isBeatMoves, beat));
            else                GlobalCommunication.communicator.sendMove(new MovePacket(from, to));

            logger.info("Packet sent successfully!");
        }
        catch(SocketException e)
        {
            logger.error("Connection lost!");
            close();
        }
        catch(IOException e)
        {
            logger.error("Failed to send packet!");
        }
    }

    @Override
    public void start()
    {
        logger.info("Starting game");
        logger.game("======================");
        logger.game("Current turn: {}", currentTurn);

        isRunning = true;
        isClosed = false;

        gameSaver.start();
        gameSaver.setTurn(isServer ? GameSaver.TurnType.WHITE : GameSaver.TurnType.BLACK);

        if(isServer)
        {
//            uiPlayer2Turn();
            startServerGame();
        }
        else
        {
//            uiPlayer1Turn();
            startClientGame();
        }

        watchTimers();
    }

    private void startServerGame()
    {
        turn();
    }
    private void startClientGame()
    {
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

        logger.info("Closing connection");
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
                    gameOver("Time left");
                    timersScheduler.shutdown();
                }
            });
        }
    }

    public void changeTurn()
    {
//        System.out.println("Waiting for move");

        checkGameOverAtNoPieces();

        Thread changeTurnThread = new Thread(() ->
        {
            if(!isRunning) return;

            logger.debug("ui1");

            uiPlayer1Turn();

            Object receivedObject = null;
            synchronized (communicationMutex)
            {
                try
                {
                    receivedObject  = GlobalCommunication.communicator.getObject();
                    if(receivedObject == null)
                    {
                        logger.error("Connection lost!");
                        close();
                        return;
                    }
                }
                catch(SocketException e)
                {
                    logger.error("Connection lost!");
                    close();
                    return;
                }
                catch(IOException | ClassNotFoundException e)
                {
                    logger.error("Failed to get packet!");
                }
            }

            if(receivedObject instanceof ServerState)
            {
                playAgainClient();
                return;
            }

            MovePacket move = (MovePacket) receivedObject ;
//            System.out.println("Move received: " + move.fromX + " " + move.fromY + " " + move.toX + " " + move.toY);
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
            gameOver("All black pieces are beaten");
        }
        if(board.getWhitePieceCount() == 0)
        {
            winner = PieceType.BLACK;
            gameOver("All white pieces are beaten");
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

    private void gameOver(String reasonMessage)
    {
        timersScheduler.shutdownNow();
        isRunning = false;

        Platform.runLater(() ->
        {
            logger.game("======================");
            logger.info("Game finished - {}", reasonMessage);

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
                    logger.info("Waiting for game reset state...");

                    Object recievedObject = null;
                    synchronized (communicationMutex)
                    {
                        try
                        {
                            recievedObject = GlobalCommunication.communicator.getObject();
                            if(recievedObject == null)
                            {
                                logger.error("Connection lost!");
                                close();
                                return;
                            }
                        }
                        catch(SocketException e)
                        {
                            logger.error("Connection lost!");
                            close();
                            return;
                        }
                        catch(IOException | ClassNotFoundException e)
                        {
                            logger.error("Failed to get state!");
                        }
                    }

                    if(recievedObject instanceof MovePacket)
                    {
                        MovePacket translatedMove = translateMove((MovePacket) recievedObject);
                        handleReceivedMove(translatedMove);
                        return;
                    }

                    ServerState state = (ServerState) recievedObject;
                    logger.debug("State: {}", String.valueOf(state));

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
            logger.info("Resetting game");
            gameOverAlert.hide();
            reset();
            start();
        });
    }

    private void playAgainServer()
    {
        logger.info("Sending game reset state...");

        try
        {
            GlobalCommunication.communicator.sendState(ServerState.GAME_START);
            logger.info("Game reset state sent successfully!");
        }
        catch(SocketException e)
        {
            logger.error("Connection lost!");
            close();
            return;
        }
        catch(IOException e)
        {
            logger.error("Failed to send state!");
        }

        logger.info("Resetting game");
        reset();
//        start();
    }

    private void createGameOverAlert()
    {
        gameOverAlert.setEventOnPlayAgain(e ->
        {
            logger.info("Preparing another game");
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

            logger.debug("ui2");
            uiPlayer2Turn();

            turn();
        });
    }
}
