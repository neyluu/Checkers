package checkers.game;

import checkers.game.pieces.PieceType;
import checkers.gui.outputs.PlayerUI;

public class CooperationGame extends OfflineGame
{
    public CooperationGame(PlayerUI player1UI, PlayerUI player2UI)
    {
        super(player1UI, player2UI);
    }

    @Override
    public void changeTurn()
    {
        if(currentTurn == PieceType.WHITE)
        {
            if(!turnWhite()) return;
        }
        else if(currentTurn == PieceType.BLACK)
        {
            if(!turnBlack()) return;
        }

        turn();
    }
}
