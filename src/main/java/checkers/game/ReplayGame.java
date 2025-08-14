package checkers.game;

import checkers.game.utils.Position;
import checkers.gui.outputs.PlayerUI;

public class ReplayGame extends Game
{
    public ReplayGame(PlayerUI player1UI, PlayerUI player2UI)
    {
        super(player1UI, player2UI);
    }

    @Override
    protected void changeTurn() {

    }

    @Override
    protected void onMove(Position from, Position to, Position beat, boolean isBeatMoves) {

    }

    @Override
    public void start() {

    }
}
