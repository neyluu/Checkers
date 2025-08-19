package checkers.scenes;

import checkers.game.utils.GameSession;
import checkers.gui.buttons.MenuButton;
import checkers.gui.inputs.LabeledTextField;
import checkers.gui.inputs.LabeledTimeComboBox;
import checkers.logging.AppLogger;
import checkers.scenes.utils.SceneType;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class CooperationIntro extends SceneBase
{
    private final AppLogger logger = new AppLogger(CooperationIntro.class);

    private TextField player1;
    private TextField player2;
    private ComboBox<String> turnTime;

    public CooperationIntro()
    {
        layout.setStyle("-fx-background-color: rgb(25,25,25);");
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(50);


        initPlayerUsernameInputs();
        initButtons();
    }

    private void initButtons()
    {
        MenuButton playButton = new MenuButton("Play");
        MenuButton backButton = new MenuButton("Back");
        addEventsToButtons(playButton, backButton);

        LabeledTimeComboBox comboBox = new LabeledTimeComboBox();
        turnTime = comboBox.getComboBox();

        layout.getChildren().addAll(comboBox, playButton, backButton);
    }

    private void addEventsToButtons(MenuButton playButton,  MenuButton backButton)
    {
        playButton.setOnAction(e ->
        {
            String player1Username = player1.getText();
            String player2Username = player2.getText();

            GameSession session = GameSession.getInstance();

            session.player1Username = player1Username;
            session.player2Username = player2Username;

            if(player1Username.isEmpty()) session.player1Username = "Player 1";
            if(player2Username.isEmpty()) session.player2Username = "Player 2";

            session.turnTime = turnTime.getValue();
            session.type = "cooperation";

            logger.info("Cooperation gamer created");
            logger.info("Player 1 name: {}", session.player1Username);
            logger.info("Player 2 name: {}", session.player2Username);
            logger.info("Game time:     {}", session.turnTime);

            sceneManager.setScene(SceneType.COOPERATION);
        });

        backButton.setOnAction(e -> sceneManager.setScene(SceneType.MAIN_MENU));
    }

    private void initPlayerUsernameInputs()
    {
        HBox container = new HBox();

        container.setSpacing(50);
        container.setAlignment(Pos.CENTER);

        LabeledTextField left = new LabeledTextField("Enter first player username:", "Player1");
        LabeledTextField right = new LabeledTextField("Enter second player username:", "Player2");

        player1 = left.getTextField();
        player2 = right.getTextField();

        container.getChildren().addAll(left, right);
        layout.getChildren().add(container);
    }
}
