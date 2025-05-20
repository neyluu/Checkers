package checkers.scenes;

import checkers.game.CooperationGame;

public class Cooperation extends GameSceneBase
{
    public Cooperation()
    {
        game = new CooperationGame(player1UI, player2UI);
        updateBoard(game.getBoard());

        startGame();
    }
}
