package checkers.scenes;

import checkers.game.MultiplayerGame;

public class MultiplayerClient extends GameSceneBase
{
    private MultiplayerGame game;

    public MultiplayerClient()
    {
        game = new MultiplayerGame(player1UI, player2UI, false);
        game.getBoard().clearBoard(true);
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
