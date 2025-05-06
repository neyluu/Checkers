package checkers.game;

import java.io.Serializable;

public class GameSession implements Serializable
{
    private static GameSession instance = null;

    public String player1Username = "Player 1";
    public String player2Username = "Player 2";
    public String turnTime = "unlimited";

    private GameSession() { }

    public static GameSession getInstance()
    {
        if(instance == null) instance = new GameSession();
        return instance;
    }
}
