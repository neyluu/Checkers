package checkers.gui.outputs.replays;

import checkers.exceptions.ReplayFileCorrupted;
import checkers.game.replays.GameLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
    private Text players;

    private static ReplayBar lastClicked = null;

    public ReplayBar(File file) throws ReplayFileCorrupted
    {
        getStylesheets().add(getClass().getResource("/css/replay-bar.css").toExternalForm());
        this.getStyleClass().add("replay-bar");
        left.getStyleClass().add("left");
        middle.getStyleClass().add("middle");
        right.getStyleClass().add("right");
        this.file = file;
        this.loader = new GameLoader(this.file);
        this.fileHeader = this.loader.getHeader();

        parseHeaderData();

        left.getChildren().add(date);
        middle.getChildren().add(mode);
        right.getChildren().add(players);

        HBox.setHgrow(left, Priority.NEVER);
        HBox.setHgrow(middle, Priority.NEVER);
        HBox.setHgrow(right, Priority.ALWAYS);

        this.getChildren().addAll(left, middle, right);

        this.setOnMouseClicked(e -> handleClick());
    }

    public static ReplayBar getLastClicked()
    {
        return lastClicked;
    }

    public GameLoader getLoader()
    {
        return loader;
    }

    public GameLoader.FileHeader getFileHeader()
    {
        return fileHeader;
    }

    private void parseHeaderData()
    {
        date = new Text(fileHeader.date + " " + fileHeader.time);
        mode = new Text(fileHeader.mode);
        players = new Text(fileHeader.player1 + " vs " + fileHeader.player2);
    }

    private void handleClick()
    {
        if(lastClicked != null)
        {
            lastClicked.getStyleClass().remove("selected");
        }
        this.getStyleClass().add("selected");
        lastClicked = this;
    }
}
