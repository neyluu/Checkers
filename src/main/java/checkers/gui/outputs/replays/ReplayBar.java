package checkers.gui.outputs.replays;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReplayBar extends HBox
{
    private LocalDateTime time = LocalDateTime.now();

    private File file;

    private VBox left = new VBox();
    private VBox middle = new VBox();
    private VBox right = new VBox();

    private Text date;
    private Text mode;

    public ReplayBar(File file)
    {
        getStylesheets().add(getClass().getResource("/css/replay-bar.css").toExternalForm());
        this.getStyleClass().add("replay-bar");

        this.file = file;
        parseFileData();

        left.setStyle("-fx-background-color: #7676ff");
        middle.setStyle("-fx-background-color: green");
        right.setStyle("-fx-background-color: yellow");

        left.getChildren().add(date);
        middle.getChildren().add(mode);
        right.setMinWidth(1);

        this.getChildren().addAll(left, middle, right);
    }

    private void parseFileData()
    {
        String filename = file.getName();

        String dateTimeRaw = filename.substring(0, 20);
        DateTimeFormatter parseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd__HH-mm-ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeRaw, parseFormatter);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String dateTimeFormatted = dateTime.format(formatter);
        date = new Text(dateTimeFormatted);
        
        int dotIndex = filename.indexOf('.');
        String modeRaw = filename.substring(22, dotIndex);
        mode = new Text(modeRaw);
    }
}
