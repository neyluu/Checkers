package checkers.scenes;

import checkers.gui.buttons.MenuButton;
import javafx.geometry.Pos;
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

    public CooperationIntro()
    {
        layout.setStyle("-fx-background-color: rgb(25,25,25);");
        layout.setAlignment(Pos.CENTER);


        HBox container = new HBox();

        VBox left = new VBox();
        {
            left.setSpacing(25);

            Text leftLabel = new Text("Enter first player username:");
            leftLabel.setFill(Color.rgb(180,180,180));
            leftLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 32));
            left.getChildren().add(leftLabel);

            TextField leftInput = new TextField();
            leftInput.setMinHeight(64);
            leftInput.setFont(Font.font("Arial", FontWeight.NORMAL, 32));
            leftInput.setOnAction(e -> System.out.println(leftInput.getText()));
            left.getChildren().add(leftInput);
        }


        VBox right = new VBox();
        {
            right.setSpacing(25);

            Text rightLabel = new Text("Enter first player username:");
            rightLabel.setFill(Color.rgb(180,180,180));
            rightLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 32));
            right.getChildren().add(rightLabel);

            TextField rightInput = new TextField();
            rightInput.setMinHeight(64);
            rightInput.setFont(Font.font("Arial", FontWeight.NORMAL, 32));
            rightInput.setOnAction(e -> System.out.println(rightInput.getText()));
            right.getChildren().add(rightInput);
        }


        container.setSpacing(50);
        container.setAlignment(Pos.CENTER);
        container.getChildren().add(left);
        container.getChildren().add(right);

        layout.setSpacing(100);
        layout.getChildren().add(container);

        MenuButton playButton = new MenuButton("Play");
        playButton.setEvent(e ->
        {
            try
            {
                sceneManager.setScene(2);
            }
            catch(IndexOutOfBoundsException ex)
            {
                System.err.println("Scene index is out of bound!");
            }
        });
        layout.getChildren().add(playButton.getButton());

        MenuButton backButton = new MenuButton("Back");
        backButton.setEvent(e ->
        {
            try
            {
                sceneManager.setScene(0);
            }
            catch(IndexOutOfBoundsException ex)
            {
                System.err.println("Scene index is out of bound!");
            }
        });
        layout.getChildren().add(backButton.getButton());

        setScene();
    }

    @Override
    protected void setScene()
    {
        scene = new Scene(layout);
    }
}
