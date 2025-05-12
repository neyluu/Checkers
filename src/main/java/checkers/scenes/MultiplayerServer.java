package checkers.scenes;

import checkers.game.GameSession;
import checkers.game.MultiplayerGame;

public class MultiplayerServer extends GameSceneBase
{
    private MultiplayerGame game;

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

    private void startGame()
    {
        Thread thread = new Thread(() -> game.start());
        thread.setDaemon(true);
        thread.start();
    }
}
