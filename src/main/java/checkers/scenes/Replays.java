package checkers.scenes;

import checkers.game.ReplayGame;

public class Replays extends GameSceneBase
{
    public Replays()
    {
        game = new ReplayGame(player1UI, player2UI);
        updateBoard(game.getBoard());
    }
}
