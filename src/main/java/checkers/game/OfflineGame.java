package checkers.game;

import checkers.game.pieces.PieceType;
import checkers.game.utils.Position;
import checkers.gui.outputs.PlayerUI;
import checkers.gui.popups.GameOverAlert;
import checkers.scenes.utils.SceneManager;
import checkers.scenes.utils.SceneType;
import javafx.application.Platform;

public abstract class OfflineGame extends Game
{
    private GameOverAlert gameOverAlert = new GameOverAlert();

    public OfflineGame(PlayerUI player1UI, PlayerUI player2UI)
    {
        super(player1UI, player2UI);
        currentTurn = PieceType.WHITE;

        gameOverAlert.setEventOnPlayAgain(e ->
        {
            gameOverAlert.hide();
            reset();
            start();
        });

        gameOverAlert.setEventOnQuit(e ->
        {
            Platform.exit();
        });

        gameOverAlert.setEventOnQuitMainMenu(e ->
        {
            SceneManager.getInstance().setScene(SceneType.MAIN_MENU);
        });
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

        gameOverAlert.show();
        gameOverAlert.setInfo((currentTurn == PieceType.WHITE ? player2UI.getUsername() : player1UI.getUsername()) + " won!");
    }
}
