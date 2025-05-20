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
            if(board.getBlackPiecesCount() == 0)
            {
                gameOver();
                return;
            }
            currentTurn = PieceType.BLACK;
            uiPlayer1Turn();
            System.out.println("Black turn");
        }
        else if(currentTurn == PieceType.BLACK)
        {
            if(board.getWhitePieceCount() == 0)
            {
                gameOver();
                return;
            }
            currentTurn = PieceType.WHITE;
            uiPlayer2Turn();
            System.out.println("White turn");
        }

        turn();
    }
}
