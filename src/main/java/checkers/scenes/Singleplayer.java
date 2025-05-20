package checkers.scenes;

import checkers.game.SingleplayerGame;

public class Singleplayer extends GameSceneBase
{
    public Singleplayer()
    {
        game = new SingleplayerGame(player1UI, player2UI);
        updateBoard(game.getBoard());
        startGame();
    }
}
