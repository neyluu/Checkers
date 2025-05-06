package checkers.scenes;

import checkers.game.MultiplayerServerGame;

public class MultiplayerServer extends GameSceneBase
{
    private MultiplayerServerGame game;

    public MultiplayerServer()
    {
        game = new MultiplayerServerGame();
        updateBoard(game.getBoard());
    }
}
