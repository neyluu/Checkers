package checkers.gui.popups;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class GameOverAlert extends PopupAlert
{
    PopupAlertButton playAgainButton = new PopupAlertButton("Play again");
    PopupAlertButton quitButton = new PopupAlertButton("Quit");
    PopupAlertButton quitMainMenuButton = new PopupAlertButton("Quit to main menu");

    public GameOverAlert()
    {
        setTitle("Game finished!");
        addButtons(playAgainButton, quitButton, quitMainMenuButton);
    }

    public void removePlayAgainButton()
    {
        removeButton(playAgainButton);
    }

    public void setEventOnPlayAgain(EventHandler<ActionEvent> event)
    {
        playAgainButton.setOnAction(event);
    }

    public void setEventOnQuit(EventHandler<ActionEvent> event)
    {
        quitButton.setOnAction(event);
    }

    public void setEventOnQuitMainMenu(EventHandler<ActionEvent> event)
    {
        quitMainMenuButton.setOnAction(event);
    }
}
