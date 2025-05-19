package checkers.game;

import checkers.gui.outputs.PlayerUI;
import checkers.network.GlobalCommunication;
import checkers.scenes.utils.SceneManager;
import checkers.scenes.utils.SceneType;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class SingleplayerGame extends Game
{


    private Alert gameOverAlert;
    private ButtonType playAgain;
    private ButtonType quit;
    private ButtonType quitMainMenu;



    public SingleplayerGame(PlayerUI player1UI, PlayerUI player2UI)
    {
        super(player1UI, player2UI);
        currentTurn = PieceType.WHITE;
    }

    @Override
    protected void onMove(Position from, Position to, Position beat, boolean isBeatMoves)
    {
        return;
    }

    @Override
    protected void changeTurn()
    {
        if(currentTurn == PieceType.WHITE)
        {
            if(board.getBlackPiecesCount() == 0)
            {
                gameOver();
                return;
            }
            currentTurn = PieceType.BLACK;
            uiPlayer1Turn();
            System.out.println("Black turn");
            aiTurn();
        }
        else if(currentTurn == PieceType.BLACK)
        {
            if(board.getWhitePieceCount() == 0)
            {
                gameOver();
                return;
            }
            currentTurn = PieceType.WHITE;
            uiPlayer2Turn();
            System.out.println("White turn");
            turn();
        }
    }

    public void start()
    {
        uiPlayer2Turn();
        watchTimers();
        turn();
    }

    public void reset()
    {
        resetScheduler();
        board.clearBoard(false);
        player1UI.resetTimer();
        player2UI.resetTimer();
        currentTurn = PieceType.WHITE;
    }

    private void watchTimers()
    {
        if(timersScheduler == null)
        {
            createTimersScheduler(() ->
            {
                if (player1UI.isTimerFinished() || player2UI.isTimerFinished())
                {
                    if (currentTurn == PieceType.WHITE) currentTurn = PieceType.BLACK;
                    else if (currentTurn == PieceType.BLACK) currentTurn = PieceType.WHITE;
                    gameOver();
                    timersScheduler.shutdown();
                }
            });
        }
    }

    private void gameOver()
    {
        player1UI.stopTimer();
        player2UI.stopTimer();

        createGameOverAlert();
        gameOverAlert.show();
        gameOverAlert.setOnHidden(e -> handleAlertButtons());
    }

    private void aiTurn()
    {
        Thread aiThread = new Thread(() ->
        {

            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }

            System.out.println("Generating beat moves...");
            Map<Piece, List<Position[]>> beatMoves = board.getPiecesWithValidMoves(currentTurn, true);
            System.out.println("Moves generated");

            if (!beatMoves.isEmpty()) {

                Random random = new Random();
                int pieceIndex = random.ints(0, beatMoves.size()).findFirst().getAsInt();
                System.out.println("Piece: " + pieceIndex);

                int i = 0;
                for (Map.Entry<Piece, List<Position[]>> entry : beatMoves.entrySet()) {
                    if (i == pieceIndex) {
                        int moveIndex = random.ints(0, entry.getValue().size()).findFirst().getAsInt();
                        System.out.println("Move: " + moveIndex);


                        Piece piece = entry.getKey();
                        List<Position[]> validMoves = entry.getValue();
                        Position[] to = validMoves.get(moveIndex);


                        Platform.runLater(() ->
                        {

                            board.movePiece(piece.getX(), piece.getY(), to[0].x, to[0].y);

                            Cell cell = board.getCell(to[0].x, to[0].y);
                            Piece currentPiece = cell.getPiece();
                            currentPiece = tryPromoteToKing(currentPiece, cell);


                            board.removePiece(to[1]);
//                    aiTurn();

                            changeTurn();
                        });
                        return;
                    }
                    i++;
                }


            }

            Map<Piece, List<Position[]>> moves = board.getPiecesWithValidMoves(currentTurn, false);
            if (moves.isEmpty()) gameOver(); // ???

            Random random = new Random();
            int pieceIndex = random.ints(0, moves.size()).findFirst().getAsInt();
            System.out.println(pieceIndex);

            int i = 0;
            for (Map.Entry<Piece, List<Position[]>> entry : moves.entrySet()) {
                if (i == pieceIndex) {
                    Piece piece = entry.getKey();
                    List<Position[]> validMoves = entry.getValue();

                    int moveIndex = random.ints(0, entry.getValue().size()).findFirst().getAsInt();
                    System.out.println("Move: " + moveIndex);

                    Position[] to = validMoves.get(moveIndex);


                    Platform.runLater(() ->
                    {


                        board.movePiece(piece.getX(), piece.getY(), to[0].x, to[0].y);

                        Cell cell = board.getCell(to[0].x, to[0].y);
                        Piece currentPiece = cell.getPiece();
                        currentPiece = tryPromoteToKing(currentPiece, cell);

                        changeTurn();
                    });

                }
                i++;
            }
        });


        aiThread.setDaemon(true);
        aiThread.start();
    }











    private void createGameOverAlert()
    {
        playAgain    = new ButtonType("Play Again", ButtonBar.ButtonData.OK_DONE);
        quit         = new ButtonType("Quit", ButtonBar.ButtonData.OK_DONE);
        quitMainMenu = new ButtonType("Quit to main menu", ButtonBar.ButtonData.OK_DONE);

        gameOverAlert = new Alert(Alert.AlertType.NONE);
        gameOverAlert.setTitle("");
        gameOverAlert.setHeaderText("Game finished!");
        gameOverAlert.setContentText((currentTurn == PieceType.WHITE ? player2UI.getUsername() : player1UI.getUsername()) + " win!");

        gameOverAlert.getButtonTypes().addAll(playAgain, quit, quitMainMenu);
    }

    private void handleAlertButtons()
    {
        ButtonType gameOverAlertResult = gameOverAlert.getResult();

        if(gameOverAlertResult.equals(quit))
        {
            Platform.exit();
        }
        if(gameOverAlertResult.equals(quitMainMenu))
        {
            GlobalCommunication.communicator.close();
            SceneManager.getInstance().setScene(SceneType.MAIN_MENU);
        }
        if(gameOverAlertResult.equals(playAgain))
        {
            reset();
            start();
        }
    }
}
