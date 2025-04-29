package checkers.scenes;

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
            default:
                throw new IllegalArgumentException("Unknown scene type: " + type);
        }
    }
}
