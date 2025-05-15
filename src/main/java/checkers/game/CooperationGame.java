package checkers.game;

import checkers.gui.outputs.PlayerUI;

public class CooperationGame extends Game
{
    private final Object lock;

    public CooperationGame(Object lock, PlayerUI player1UI, PlayerUI player2UI)
    {
        super(player1UI, player2UI);
        this.lock = lock;
        currentTurn = PieceType.WHITE;
    }

    @Override
    protected void onMove(Position from, Position to, Position beat, boolean isBeatMoves)
    {
        return;
    }

    public void start()
    {
        uiPlayer2Turn();
        watchTimers();
        turn();
    }

    public void reset()
    {
        timersScheduler.shutdown();
        timersScheduler = null;
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

    public PieceType getCurrentTurn()
    {
        return currentTurn;
    }

    public void changeTurn()
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
        }

        turn();
    }

    private void gameOver()
    {
        player1UI.stopTimer();
        player2UI.stopTimer();
        synchronized(lock)
        {
            lock.notify();
        }
    }
}
