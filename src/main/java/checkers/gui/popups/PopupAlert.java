package checkers.gui.popups;

import checkers.scenes.SceneBase;
import checkers.scenes.utils.SceneManager;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class PopupAlert extends StackPane
{
    final private VBox layout = new VBox();
    final private Label title = new Label();
    final private Label info  = new Label();
    final private HBox buttonsContainer = new HBox();


    public PopupAlert()
    {
        getStylesheets().add(getClass().getResource("/css/popup-alert.css").toExternalForm());
        layout.getStyleClass().add("popup-alert");
        title.getStyleClass().add("title");
        info.getStyleClass().add("info");
        buttonsContainer.getStyleClass().add("buttons-container");

        layout.getChildren().addAll(title, info, buttonsContainer);
        this.getChildren().add(layout);

        hide();
    }

    public PopupAlert(String title)
    {
        this();
        setTitle(title);
    }

    public PopupAlert(String title, String info)
    {
        this(title);
        setInfo(info);
    }

    public void addButton(PopupAlertButton button)
    {
        this.buttonsContainer.getChildren().add(button);
    }

    public void addButtons(PopupAlertButton... buttons)
    {
        for(PopupAlertButton button : buttons)
        {
            addButton(button);
        }
    }

    protected void removeButton(Button button)
    {
        buttonsContainer.getChildren().remove(button);
    }

    public void show()
    {
        Platform.runLater(() ->
        {
            SceneManager sceneManager = SceneManager.getInstance();
            SceneBase currentScene = sceneManager.getCurrentScene();
            currentScene.getContainer().getChildren().add(this);

            this.setVisible(true);
        });
    }

    public void hide()
    {
        Platform.runLater(() ->
        {
            SceneManager sceneManager = SceneManager.getInstance();
            SceneBase currentScene = sceneManager.getCurrentScene();
            currentScene.getContainer().getChildren().remove(this);

            this.setVisible(false);
        });
    }

    public void toggle()
    {
        this.setVisible( ! this.isVisible());
    }

    public void setTitle(String title)
    {
        Platform.runLater(() -> this.title.setText(title));
    }

    public void setInfo(String info)
    {
        Platform.runLater(() -> this.info.setText(info));
    }
}
