package checkers.scenes;

import checkers.game.ReplayGame;
import checkers.gui.buttons.MenuButton;
import javafx.scene.Node;

public class Replays extends GameSceneBase
{
    private MenuButton previousMove = new MenuButton("Previous move");
    private MenuButton nextMove = new MenuButton("Next move");

    public Replays()
    {
        game = new ReplayGame(player1UI, player2UI, previousMove, nextMove);
        updateBoard(game.getBoard());

        addButtons();
    }

    private void addButtons()
    {
        final int buttonWidth = 250;
        previousMove.setMinWidth(buttonWidth);
        previousMove.setMaxWidth(buttonWidth);
        nextMove.setMinWidth(buttonWidth);
        nextMove.setMaxWidth(buttonWidth);

        Node playerUI = right.getChildren().getLast();
        right.getChildren().removeLast();

        right.setSpacing(70);
        right.getChildren().addAll(previousMove, nextMove, playerUI);
    }
}
