package checkers.game;

import checkers.game.pieces.PieceType;
import checkers.gui.outputs.PlayerUI;
import checkers.logging.GameLogger;

public class CooperationGame extends OfflineGame
{
    private GameLogger gameLogger = new GameLogger(CooperationGame.class);

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
                gameOver("All black pieces are beaten");
                return;
            }
            currentTurn = PieceType.BLACK;
            gameLogger.log("======================");
            gameLogger.log("Current turn: {}", currentTurn);
            uiPlayer1Turn();
        }
        else if(currentTurn == PieceType.BLACK)
        {
            if(board.getWhitePieceCount() == 0)
            {
                gameOver("All white pieces are beaten");
                return;
            }
            currentTurn = PieceType.WHITE;
            gameLogger.log("======================");
            gameLogger.log("Current turn: {}", currentTurn);
            uiPlayer2Turn();
        }

        turn();
    }
}
