package checkers.game.utils;

import java.io.Serializable;
import java.text.MessageFormat;

public class GameSession implements Serializable
{
    private static GameSession instance = null;

    public String player1Username = "Player 1";
    public String player2Username = "Player 2";
    public String turnTime = "unlimited";
    public String type = "unknown";

    private GameSession() { }

    public static GameSession getInstance()
    {
        if(instance == null) instance = new GameSession();
        return instance;
    }

    @Override
    public String toString()
    {
        return MessageFormat.format("[player1:{0} player2:{1} turnTime:{2} type:{3}]",
                player1Username, player2Username, turnTime, type);
    }
}
