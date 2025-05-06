package checkers.scenes;

import checkers.game.GameSession;
import checkers.game.MultiplayerServerGame;

public class MultiplayerServer extends GameSceneBase
{
    private MultiplayerServerGame game;

    public MultiplayerServer()
    {
        game = new MultiplayerServerGame();
        updateBoard(game.getBoard());
        swapUsernames();
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
