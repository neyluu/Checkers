package checkers.scenes;

import checkers.game.MultiplayerClientGame;

public class MultiplayerClient extends GameSceneBase
{
    private MultiplayerClientGame game;

    public MultiplayerClient()
    {
        game = new MultiplayerClientGame(player1UI, player2UI);
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
