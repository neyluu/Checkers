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

import java.util.List;

public class MultiplayerGame extends Game
{
    private Alert gameOverAlert;
    private PieceType winner;
    private boolean isServer;

    private volatile boolean isRunning;
    private final Object communicationMutex = new Object();

    private ButtonType playAgain;
    private ButtonType quit;
    private ButtonType quitMainMenu;

    public MultiplayerGame(PlayerUI player1UI, PlayerUI player2UI, boolean isServer)
    {
        super(player1UI, player2UI);
        this.isServer = isServer;
        currentTurn = isServer ? PieceType.WHITE : PieceType.BLACK;
    }

    @Override
    protected void onMove(Position from, Position to, Position beat, boolean isBeatMoves)
    {
        System.out.println("Sending move packet");
        if(isBeatMoves)     GlobalCommunication.communicator.sendMove(new MovePacket(from, to, isBeatMoves, beat));
        else                GlobalCommunication.communicator.sendMove(new MovePacket(from, to));
    }

    public void start()
    {
        isRunning = true;

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
        board.clearBoard( ! isServer);
        player1UI.resetTimer();
        player2UI.resetTimer();
        currentTurn = isServer ? PieceType.WHITE : PieceType.BLACK;
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

            Object receivedObject;
            synchronized (communicationMutex)
            {
                receivedObject  = GlobalCommunication.communicator.getObject();
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

            player1UI.stopTimer();
            player2UI.stopTimer();

            System.out.println("Winner:" + winner);

            createGameOverAlert();

            if(!isServer)
            {
                Thread clientPlayAgainThread = new Thread(() ->
                {
                    System.out.println("Waiting for game resetting info");

                    Object recievedObject;
                    synchronized (communicationMutex)
                    {
                        recievedObject = GlobalCommunication.communicator.getObject();
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

            gameOverAlert.show();
            gameOverAlert.setOnHidden(e -> handleAlertButtons());
        });
    }

    private void playAgainClient()
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

    private void playAgainServer()
    {
        System.out.println("Sending game reset info");
        GlobalCommunication.communicator.sendState(ServerState.GAME_START);
        System.out.println("Sent");
        reset();
        start();
    }

    private void createGameOverAlert()
    {
        playAgain    = new ButtonType("PlayAgain", ButtonBar.ButtonData.OK_DONE);
        quit         = new ButtonType("Quit", ButtonBar.ButtonData.OK_DONE);
        quitMainMenu = new ButtonType("Quit to main menu", ButtonBar.ButtonData.OK_DONE);

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
    }

    private void handleAlertButtons()
    {
        ButtonType gameOverAlertResult = gameOverAlert.getResult();

        if(gameOverAlertResult.equals(quit))            Platform.exit();
        if(gameOverAlertResult.equals(quitMainMenu))    SceneManager.getInstance().setScene(SceneType.MAIN_MENU);
        if(gameOverAlertResult.equals(playAgain))       playAgainServer();
    }

    private void handleReceivedMove(MovePacket move)
    {
        Platform.runLater(() ->
        {
            board.movePiece(move.fromX, move.fromY, move.toX, move.toY);

            Cell cell = board.getCell(move.toX, move.toY);
            Piece currentPiece = cell.getPiece();
            tryPromoteToKing(currentPiece, cell);

            if(move.isBeatMove)
            {
                board.removePiece(move.beatX, move.beatY);
                checkGameOverAtNoPieces();

                Piece movedPiece = board.getCell(move.toX, move.toY).getPiece();
                List<Position[]> pieceBeatMoves = movedPiece.getBeatMoves(board);

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
