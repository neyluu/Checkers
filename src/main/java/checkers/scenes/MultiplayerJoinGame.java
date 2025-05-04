package checkers.scenes;

import checkers.exceptions.ServerConnectionException;
import checkers.game.GameSession;
import checkers.gui.buttons.MenuButton;
import checkers.gui.inputs.LabeledIPAddres;
import checkers.gui.inputs.LabeledTextField;
import checkers.network.Client;
import checkers.network.Server;
import checkers.scenes.utils.SceneManager;
import checkers.scenes.utils.SceneType;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

public class MultiplayerJoinGame extends SceneBase
{
    private TextField textField;
    private LabeledIPAddres ipField;

    public MultiplayerJoinGame()
    {
        layout.setStyle("-fx-background-color: rgb(25,25,25);");
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(75);

        initInputs();
        initButtons();
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

        Client client = new Client(ip);
        try
        {
            client.start();
            SceneManager.getInstance().getStage().setOnCloseRequest(e ->
            {
                client.close();
            });
//            Runtime.getRuntime().addShutdownHook(new Thread(client::close));
        }
        catch (ServerConnectionException ex)
        {
            System.out.println(ex.getMessage());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("Failed to connect to server");

            ButtonType tryAgainButton = new ButtonType("Try again", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().add(tryAgainButton);

            alert.showAndWait();

            if(alert.getResult() == tryAgainButton)
            {
                handleJoinGame();
            }
        }
    }
}
