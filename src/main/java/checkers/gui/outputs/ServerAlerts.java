package checkers.gui.outputs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class ServerAlerts
{
    private Alert waitAlert = null;
    private Runnable onCancelAction = null;

    private Alert clientConnectedAlert = null;

    private String connectedPlayerUsername = "Player";

    public void setConnectedPlayerUsername(String connectedPlayerUsername)
    {
        this.connectedPlayerUsername = connectedPlayerUsername;
    }

    private void createWaitAlert()
    {
        waitAlert = new Alert(Alert.AlertType.NONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        waitAlert.getButtonTypes().add(cancelButton);
        waitAlert.setTitle("");
        waitAlert.setHeaderText("Waiting for second player");
    }
    private void createClientConnectedAlert()
    {
        clientConnectedAlert = new Alert(Alert.AlertType.NONE);
        ButtonType startGameButton = new ButtonType("Start game!", ButtonBar.ButtonData.OK_DONE);
        clientConnectedAlert.getButtonTypes().add(startGameButton);
        clientConnectedAlert.setTitle("");
        clientConnectedAlert.setHeaderText(connectedPlayerUsername + " connected!");
    }

    public void setOnCancelAction(Runnable onCancelAction)
    {
        this.onCancelAction = onCancelAction;
    }

    public void showWaitAlert()
    {
        if(waitAlert == null) createWaitAlert();

        if(!waitAlert.isShowing())
        {
            waitAlert.showAndWait().ifPresent(buttonType ->
            {
                if(buttonType.getButtonData() == ButtonType.CANCEL.getButtonData())
                {
                    if(onCancelAction != null)
                    {
                        System.out.println("Canceling server");
                        onCancelAction.run();
                    }
                }
            });
        }
    }
    public void showClientConnectedAlert()
    {
        if(clientConnectedAlert == null) createClientConnectedAlert();

        if(!clientConnectedAlert.isShowing())
        {
            clientConnectedAlert.showAndWait();
        }
    }

    public void hideWaitAlert()
    {
        if(waitAlert == null) return;
        waitAlert.close();
    }
    public void hideClientConnectedAlert()
    {
        if(clientConnectedAlert == null) return;
        clientConnectedAlert.close();
    }

    synchronized public ButtonType getWaitAlertValue()
    {
        return waitAlert.getResult();
    }
}
