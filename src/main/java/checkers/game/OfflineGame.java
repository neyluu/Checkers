package checkers.game;

import checkers.game.pieces.PieceType;
import checkers.game.utils.Position;
import checkers.gui.outputs.PlayerUI;
import checkers.gui.popups.PopupAlert;
import checkers.gui.popups.PopupAlertButton;
import checkers.scenes.utils.SceneManager;
import checkers.scenes.utils.SceneType;
import javafx.application.Platform;

public abstract class OfflineGame extends Game
{
    private PopupAlert gameOverAlert = new PopupAlert("Game finished!");

    public OfflineGame(PlayerUI player1UI, PlayerUI player2UI)
    {
        super(player1UI, player2UI);
        currentTurn = PieceType.WHITE;

        PopupAlertButton playAgainButton = new PopupAlertButton("Play again");
        PopupAlertButton quitButton = new PopupAlertButton("Quit");
        PopupAlertButton quitMainMenuButton = new PopupAlertButton("Quit to main menu");

        playAgainButton.setOnAction(e ->
        {
            gameOverAlert.hide();
            reset();
            start();
        });
        quitButton.setOnAction(e ->
        {
            Platform.exit();
        });
        quitMainMenuButton.setOnAction(e ->
        {
            SceneManager.getInstance().setScene(SceneType.MAIN_MENU);
        });

        gameOverAlert.setInfo("Player unknown won!");
        gameOverAlert.addButtons(playAgainButton, quitButton, quitMainMenuButton);
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
