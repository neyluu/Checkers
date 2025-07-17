package checkers.gui.outputs;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class PlayerUI extends VBox
{
    private TurnTimer turnTimer;
    private Text text;

    public PlayerUI()
    {
        getStylesheets().add(getClass().getResource("/css/player-ui.css").toExternalForm());
        this.getStyleClass().add("player-ui");

        text = new Text("");
        text.getStyleClass().add("username");

        turnTimer = new TurnTimer(0);
        turnTimer.getStyleClass().add("timer");

        this.getChildren().addAll(text, turnTimer);
    }
    public PlayerUI(String playerName, int minutes)
    {
        this();

        text.setText(playerName);
        turnTimer.setMinutes(minutes);
    }

    public void startTimer()
    {
        turnTimer.start();
    }
    public void stopTimer()
    {
        turnTimer.stop();
    }

    public void setUsername(String username)
    {
        text.setText(username);
    }
    public String getUsername()
    {
        return text.getText();
    }

    public void setMinutes(int minutes)
    {
        turnTimer.setMinutes(minutes);
    }

    synchronized public boolean isTimerFinished()
    {
        return turnTimer.isFinished();
    }

    public void highlight()
    {
        this.getStyleClass().add("highlight");
    }
    public void unHighlight()
    {
        this.getStyleClass().remove("highlight");
    }
    public void resetTimer()
    {
        turnTimer.reset();
    }
}
