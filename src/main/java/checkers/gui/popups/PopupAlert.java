package checkers.gui.popups;

import javafx.geometry.Pos;
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

        buttonsContainer.setAlignment(Pos.BASELINE_CENTER);
        layout.setAlignment(Pos.BASELINE_CENTER);

        layout.getChildren().addAll(title, info, buttonsContainer);
        this.getChildren().add(layout);
    }

    public PopupAlert(String title)
    {
        this();
        this.title.setText(title);
    }

    public PopupAlert(String title, String info)
    {
        this(title);
        this.info.setText(info);
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

    public void show()
    {
        this.setVisible(true);
    }

    public void hide()
    {
        this.setVisible(false);
    }

    public void toggle()
    {
        this.setVisible( ! this.isVisible());
    }
}
