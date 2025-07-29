package checkers.gui.outputs.replays;

import checkers.Settings;
import checkers.exceptions.ReplayFileCorrupted;
import checkers.logging.AppLogger;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReplaysChooser extends ScrollPane
{
    private final AppLogger logger = new AppLogger(ReplaysChooser.class);

    private final VBox container = new VBox();
    private List<ReplayBar> records = new ArrayList<>();

    public ReplaysChooser()
    {
        getStylesheets().add(getClass().getResource("/css/replays-chooser.css").toExternalForm());
        this.getStyleClass().add("replays-chooser");
        container.getStyleClass().add("container");
        this.setContent(container);

        createRecords();
        displayRecords();
    }

    private void createRecords()
    {
        File replaysDirectory = new File(Settings.replaysPath);
        File[] replays = replaysDirectory.listFiles();

        for(File file : replays)
        {
            try
            {
                ReplayBar replayBar = new ReplayBar(file);
                records.add(replayBar);
            }
            catch (ReplayFileCorrupted e) { }
        }
    }

    private void displayRecords()
    {
        for(ReplayBar record : records)
        {
            container.getChildren().add(record);
        }
    }
}
