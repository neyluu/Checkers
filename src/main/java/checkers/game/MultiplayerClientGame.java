package checkers.game;

import checkers.gui.outputs.PlayerUI;
import checkers.network.GlobalCommunication;
import checkers.network.MovePacket;
import javafx.application.Platform;

public class MultiplayerClientGame extends Game
{
    private PlayerUI player1UI;
    private PlayerUI player2UI;

    public MultiplayerClientGame(PlayerUI player1UI, PlayerUI player2UI)
    {
        this.player1UI = player1UI;
        this.player2UI = player2UI;
    }

    public void start()
    {
        turn();
    }

    private void turn()
    {
        System.out.println("Waiting for move from server");
        MovePacket move = GlobalCommunication.communicator.getMove();
        System.out.println("Move received: " + move.fromX + " " + move.fromY + " " + move.toX + " " + move.toY);
        Platform.runLater(() ->
        {
            board.movePiece(move.fromX, move.fromY, move.toX, move.toY);
        });

    }
}
