package checkers.scenes;

import checkers.game.utils.GameSession;
import checkers.gui.buttons.MenuButton;
import checkers.gui.inputs.LabeledTextField;
import checkers.gui.inputs.LabeledTimeComboBox;
import checkers.gui.popups.PopupAlert;
import checkers.gui.popups.PopupAlertButton;
import checkers.logging.AppLogger;
import checkers.network.GlobalCommunication;
import checkers.network.Server;
import checkers.scenes.utils.SceneType;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;

public class MultiplayerCreateGame extends SceneBase
{
    private final AppLogger logger = new AppLogger(MultiplayerCreateGame.class);

    private TextField textField;
    private ComboBox<String> comboBox;
    private PopupAlert failedCreateServerAlert = new PopupAlert("Failed to create server!");

    public MultiplayerCreateGame()
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
            failedCreateServerAlert.hide();
        });

        tryAgainButton.setOnAction(e ->
        {
            failedCreateServerAlert.hide();
            createServer();
        });

        failedCreateServerAlert.addButtons(okButton, tryAgainButton);
    }

    private void initInputs()
    {
        LabeledTextField labeledTextField = new LabeledTextField("Enter username:", "Player 1");
        textField = labeledTextField.getTextField();
        labeledTextField.setMaxWidth(350);

        LabeledTimeComboBox labeledTimeComboBox = new LabeledTimeComboBox();
        comboBox = labeledTimeComboBox.getComboBox();

        layout.getChildren().addAll(labeledTextField, labeledTimeComboBox);
    }

    private void initButtons()
    {
        MenuButton create = new MenuButton("Create game");
        create.setOnAction(e -> createServer());

        MenuButton back = new MenuButton("Back");
        back.setOnAction(e -> sceneManager.setScene(SceneType.MULTIPLAYER_INTRO));

        layout.getChildren().addAll(create, back);
    }

    private void createServer()
    {
        try
        {
            logger.info("Creating server...");

            GameSession session = GameSession.getInstance();

            String username = textField.getText();
            if(username.isEmpty()) username = "Player 1";
            session.player1Username = username;
            session.turnTime = comboBox.getValue();
            session.type = "multiplayer";

            Server server = new Server();
            server.start();
            GlobalCommunication.communicator = server;
            Runtime.getRuntime().addShutdownHook(new Thread(server::close));

            logger.info("Server created");
        }
        catch (IOException ex)
        {
            logger.error("Failed to create server");
            failedCreateServerAlert.show();
        }
    }
}
