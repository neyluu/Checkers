package checkers.gui.outputs.replays;

import checkers.Settings;
import checkers.exceptions.ReplayFileCorrupted;
import checkers.game.replays.GameLoader;
import checkers.game.utils.GameSession;
import checkers.logging.AppLogger;
import checkers.scenes.utils.SceneManager;
import checkers.scenes.utils.SceneType;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReplaysChooser extends ScrollPane
{
    private final AppLogger logger = new AppLogger(ReplaysChooser.class);

    private final VBox container = new VBox();
    private final StackPane wrapper = new StackPane();
    private List<ReplayBar> records = new ArrayList<>();

    private ReplayBar selectedRecord = null;

    public ReplaysChooser()
    {
        System.out.println("constructor");
        getStylesheets().add(getClass().getResource("/css/replays-chooser.css").toExternalForm());
        this.getStyleClass().add("replays-chooser");
        container.getStyleClass().add("container");

        wrapper.getChildren().add(container);
        wrapper.setAlignment(Pos.CENTER);
        this.setContent(wrapper); 

        this.setPannable(true);
        this.setFitToWidth(true);
        this.setFitToHeight(false);

        createRecords();
        displayRecords();
    }

    public void load()
    {
        selectedRecord = ReplayBar.getLastClicked();

        if(selectedRecord == null)
        {
            // TODO
            System.out.println("Theres no selected record!");
            return;
        }

        fillGameSession();

        SceneManager.getInstance().setScene(SceneType.REPLAYS);
    }

    private void fillGameSession()
    {
        GameSession gameSession = GameSession.getInstance();
        GameLoader.FileHeader fileHeader = selectedRecord.getFileHeader();

        gameSession.player1Username = fileHeader.player1;
        gameSession.player2Username = fileHeader.player2;
        gameSession.turnTime = fileHeader.gameTime;
        gameSession.type = fileHeader.mode;
    }

    private void createRecords()
    {
        logger.info("Creating replay records...");

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

        logger.info("Created {} log records!", records.size());
    }

    private void displayRecords()
    {
        if(records.isEmpty())
        {
            Text emptyList = new Text("List is empty!");
            emptyList.getStyleClass().add("empty-list-text");
            container.getChildren().add(emptyList);
            return;
        }

        for(ReplayBar record : records)
        {
            container.getChildren().add(record);
        }
    }
}
