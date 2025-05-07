package checkers.gui.outputs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class ClientAlerts
{
    private Alert waitAlert = null;
    private Runnable onWaitAction = null;

    private void createWaitAlert()
    {
        waitAlert = new Alert(Alert.AlertType.NONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        waitAlert.getButtonTypes().add(cancelButton);
        waitAlert.setTitle("");
        waitAlert.setHeaderText("Waiting for game start");
    }

    public void setOnWaitAction(Runnable onWaitAction)
    {
        this.onWaitAction = onWaitAction;
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
                    if(onWaitAction != null)
                    {
                        System.out.println("Disconnecting from game");
                        onWaitAction.run();
                    }
                }
            });
        }
    }

    public void hideWaitAlert()
    {
        if(waitAlert == null) return;
        waitAlert.close();
    }
}
