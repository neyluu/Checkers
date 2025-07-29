package checkers.gui.outputs.replays;

import checkers.exceptions.ReplayFileCorrupted;
import checkers.game.replays.GameLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;

public class ReplayBar extends HBox
{
    private File file;
    private GameLoader loader;
    private GameLoader.FileHeader fileHeader;


    private VBox left = new VBox();
    private VBox middle = new VBox();
    private VBox right = new VBox();

    private Text date;
    private Text mode;

    public ReplayBar(File file) throws ReplayFileCorrupted
    {
        getStylesheets().add(getClass().getResource("/css/replay-bar.css").toExternalForm());
        this.getStyleClass().add("replay-bar");

        this.file = file;
        this.loader = new GameLoader(this.file);
        this.fileHeader = this.loader.getHeader();

        parseHeaderData();

        left.setStyle("-fx-background-color: #7676ff");
        middle.setStyle("-fx-background-color: green");
        right.setStyle("-fx-background-color: yellow");

        left.getChildren().add(date);
        middle.getChildren().add(mode);
        right.setMinWidth(1);

        this.getChildren().addAll(left, middle, right);
    }

    private void parseHeaderData()
    {
        date = new Text(fileHeader.date + " " + fileHeader.time);
        mode = new Text(fileHeader.mode);
    }
}
