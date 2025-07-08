package checkers.scenes;

import checkers.exceptions.ServerConnectionException;
import checkers.game.utils.GameSession;
import checkers.gui.buttons.MenuButton;
import checkers.gui.inputs.LabeledIPAddres;
import checkers.gui.inputs.LabeledTextField;
import checkers.gui.popups.PopupAlert;
import checkers.gui.popups.PopupAlertButton;
import checkers.network.Client;
import checkers.network.GlobalCommunication;
import checkers.scenes.utils.SceneType;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;

public class MultiplayerJoinGame extends SceneBase
{
    private TextField textField;
    private LabeledIPAddres ipField;
    private PopupAlert failedToConnectAlert = new PopupAlert("Failed to connect to server!");

    public MultiplayerJoinGame()
    {
        layout.setStyle("-fx-background-color: rgb(25,25,25);");
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(75);

        initAlert();
        initInputs();
        initButtons();
    }

    private void initAlert()
    {
        PopupAlertButton okButton = new PopupAlertButton("OK");
        PopupAlertButton tryAgainButton = new PopupAlertButton("Try again");
        okButton.setOnAction(e ->
        {
            failedToConnectAlert.hide();
        });
        tryAgainButton.setOnAction( e ->
        {
            failedToConnectAlert.hide();
            handleJoinGame();
        });
        failedToConnectAlert.addButtons(okButton, tryAgainButton);

        container.getChildren().add(failedToConnectAlert);
    }

    private void initInputs()
    {
        LabeledTextField labeledTextField = new LabeledTextField("Enter username:", "Player 2");
        labeledTextField.setMaxWidth(350);
        textField = labeledTextField.getTextField();

        ipField = new LabeledIPAddres();
        ipField.setAlignment(Pos.CENTER);
        ipField.setMaxWidth(350);

        layout.getChildren().addAll(labeledTextField, ipField);
    }

    private void initButtons()
    {
        MenuButton join = new MenuButton("Join game");
        join.setOnAction(e -> handleJoinGame());

        MenuButton back = new MenuButton("Back");
        back.setOnAction(e -> sceneManager.setScene(SceneType.MULTIPLAYER_INTRO));

        layout.getChildren().addAll(join, back);
    }

    private void handleJoinGame()
    {
        String ip = ipField.getIP();
        if(ip.isEmpty()) ip = "localhost";

        String username = textField.getText();
        if(username.isEmpty()) username = "Player 2";
        GameSession.getInstance().player2Username = username;

        try
        {
            Client client = new Client(ip);
            client.start();
            GlobalCommunication.communicator = client;

            Runtime.getRuntime().addShutdownHook(new Thread(client::close));
        }
        catch (ServerConnectionException ex)
        {
            System.out.println(ex.getMessage());
            failedToConnectAlert.show();
        }
    }
}
