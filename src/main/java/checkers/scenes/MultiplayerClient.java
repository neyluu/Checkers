package checkers.scenes;

import checkers.game.MultiplayerClientGame;

public class MultiplayerClient extends GameSceneBase
{
    private MultiplayerClientGame game;

    public MultiplayerClient()
    {
        game = new MultiplayerClientGame();
        game.getBoard().clearBoard(true);
        updateBoard(game.getBoard());
    }
}
