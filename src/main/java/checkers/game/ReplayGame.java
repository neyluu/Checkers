package checkers.game;

import checkers.exceptions.ReplayFileCorrupted;
import checkers.game.replays.GameLoader;
import checkers.game.utils.Position;
import checkers.gui.buttons.MenuButton;
import checkers.gui.outputs.PlayerUI;
import checkers.gui.outputs.replays.ReplayBar;

public class ReplayGame extends Game
{
    private MenuButton previousMove;
    private MenuButton nextMove;
    private GameLoader gameLoader;

    public ReplayGame(PlayerUI player1UI, PlayerUI player2UI, MenuButton previousMove, MenuButton nextMove)
    {
        super(player1UI, player2UI);

        this.previousMove = previousMove;
        this.nextMove = nextMove;

        this.gameLoader = ReplayBar.getLastClicked().getLoader();
    }

    @Override
    protected void changeTurn() {

    }

    @Override
    protected void onMove(Position from, Position to, Position beat, boolean isBeatMoves) {

    }

    @Override
    public void start() {
        System.out.println("test");
    }
}
