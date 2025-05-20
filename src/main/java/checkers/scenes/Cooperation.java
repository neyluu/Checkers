package checkers.scenes;

import checkers.game.CooperationGame;

public class Cooperation extends GameSceneBase
{
    private CooperationGame game;

    public Cooperation()
    {
        game = new CooperationGame(player1UI, player2UI);
        updateBoard(game.getBoard());

        startGame();
    }

    private void startGame()
    {
        Thread thread = new Thread(() -> game.start());
        thread.setDaemon(true);
        thread.start();
    }
}
