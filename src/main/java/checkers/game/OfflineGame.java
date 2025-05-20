package checkers.game;

import checkers.game.pieces.PieceType;
import checkers.game.utils.Position;
import checkers.gui.outputs.PlayerUI;
import checkers.scenes.utils.SceneManager;
import checkers.scenes.utils.SceneType;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public abstract class OfflineGame extends Game
{
    private Alert gameOverAlert;
    private ButtonType playAgain;
    private ButtonType quit;
    private ButtonType quitMainMenu;

    public OfflineGame(PlayerUI player1UI, PlayerUI player2UI)
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
    public void start()
    {
        uiPlayer2Turn();
        watchTimers();
        turn();
    }

    private void reset()
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
                    Platform.runLater(this::gameOver);
                    timersScheduler.shutdown();
                }
            });
        }
    }

    protected void gameOver()
    {
        System.out.println("GAME OVER");
        player1UI.stopTimer();
        player2UI.stopTimer();

        createGameOverAlert();
        gameOverAlert.show();
        gameOverAlert.setOnHidden(e -> handleAlertButtons());
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
            SceneManager.getInstance().setScene(SceneType.MAIN_MENU);
        }
        if(gameOverAlertResult.equals(playAgain))
        {
            reset();
            start();
        }
    }
}
