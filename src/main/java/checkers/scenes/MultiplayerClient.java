package checkers.scenes;

import checkers.game.MultiplayerGame;

public class MultiplayerClient extends GameSceneBase
{
    public MultiplayerClient()
    {
        game = new MultiplayerGame(player1UI, player2UI, false);
        game.getBoard().clearBoard(true);
        updateBoard(game.getBoard());

        startGame();
    }
}
