package checkers.scenes.utils;

import checkers.scenes.*;

public class SceneFactory
{
    public SceneBase createScene(SceneType type)
    {
        switch (type)
        {
            case MAIN_MENU :
                return new MainMenu();
            case COOPERATION_INTRO:
                return new CooperationIntro();
            case COOPERATION:
                return new Cooperation();
            case MULTIPLAYER_INTRO:
                return new MultiplayerIntro();
            case MULTIPLAYER_CREATE_GAME:
                return new MultiplayerCreateGame();
            case MULTIPLAYER_JOIN_GAME:
                return new MultiplayerJoinGame();
            case MULTIPLAYER_SERVER:
                return new MultiplayerServer();
            case MULTIPLAYER_CLIENT:
                return new MultiplayerClient();
            case SINGLEPLAYER_INTRO:
                return new SingleplayerIntro();
            case SINGLEPLAYER:
                return new Singleplayer();
            case REPLAYS_INTRO:
                return new ReplaysIntro();
            case REPLAYS:
                return new Replays();
            default:
                throw new IllegalArgumentException("Unknown scene type: " + type);
        }
    }
}
