package checkers.scenes;

import checkers.gui.buttons.MenuButton;
import checkers.gui.inputs.LabeledTextField;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CooperationIntro extends SceneBase
{
    private VBox layout = new VBox();
    private TextField player1;
    private TextField player2;

    public CooperationIntro()
    {
        type = SceneType.COOPERATION_INTRO;

        layout.setStyle("-fx-background-color: rgb(25,25,25);");
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(100);


        initPlayerUsernameInputs();
        initButtons();


        setScene();
    }

    @Override
    protected void setScene()
    {
        scene = new Scene(layout, settings.screenWidth, settings.screenHeight);
    }

    private void initButtons()
    {
        MenuButton playButton = new MenuButton("Play");
        MenuButton backButton = new MenuButton("Back");

        playButton.setEvent(e ->
        {
            try
            {
                String player1Username = player1.getText();
                String player2Username = player2.getText();

                if(player1Username.isEmpty()) player1Username = "Player 1";
                if(player2Username.isEmpty()) player2Username = "Player 2";

                Cooperation cooperation = new Cooperation(player1Username, player2Username);
                sceneManager.addScene(cooperation);
                sceneManager.setScene(SceneType.COOPERATION);
            }
            catch(IndexOutOfBoundsException ex)
            {
                System.err.println("Scene index is out of bound!");
            }
        });

        backButton.setEvent(e ->
        {
            try
            {
                sceneManager.setScene(SceneType.MAIN_MENU);
            }
            catch(IndexOutOfBoundsException ex)
            {
                System.err.println("Scene index is out of bound!");
            }
        });

        layout.getChildren().add(playButton);
        layout.getChildren().add(backButton);
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
