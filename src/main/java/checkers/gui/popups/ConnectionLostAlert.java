package checkers.gui.popups;

import checkers.network.GlobalCommunication;
import checkers.scenes.utils.SceneManager;
import checkers.scenes.utils.SceneType;
import javafx.application.Platform;

public class ConnectionLostAlert extends PopupAlert
{
    public ConnectionLostAlert()
    {
        super("Connection lost!");

        PopupAlertButton quitButton = new PopupAlertButton("Quit");
        PopupAlertButton quitMainMenuButton = new PopupAlertButton("Quit to main menu");

        quitButton.setOnAction(e ->
        {
            Platform.exit();
        });
        quitMainMenuButton.setOnAction(e ->
        {
            GlobalCommunication.communicator.close();
            SceneManager.getInstance().setScene(SceneType.MAIN_MENU);
        });

        addButtons(quitButton, quitMainMenuButton);
    }
}
