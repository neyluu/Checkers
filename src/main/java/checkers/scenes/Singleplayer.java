package checkers.scenes;

import checkers.game.SingleplayerGame;

public class Singleplayer extends GameSceneBase
{
    private SingleplayerGame game;

    public Singleplayer()
    {
        game = new SingleplayerGame(player1UI, player2UI);
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
