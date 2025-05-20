package checkers.scenes;

import checkers.game.utils.GameSession;
import checkers.game.MultiplayerGame;

public class MultiplayerServer extends GameSceneBase
{
    public MultiplayerServer()
    {
        game = new MultiplayerGame(player1UI, player2UI, true);
        updateBoard(game.getBoard());
        swapUsernames();

        startGame();
    }

    private void swapUsernames()
    {
        GameSession session = GameSession.getInstance();
        String tmp = session.player1Username;
        session.player1Username = session.player2Username;
        session.player2Username = tmp;

        player1UI.setUsername(session.player1Username);
        player2UI.setUsername(session.player2Username);
    }
}
