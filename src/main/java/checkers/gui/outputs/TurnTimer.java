package checkers.gui.outputs;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.Timer;
import java.util.TimerTask;

public class TurnTimer extends Text
{
    private int seconds = 0;
    private int currentSeconds = 0;
    private int change = 0;
    private int currentChange = 0;
    private boolean finished = false;

    public TurnTimer(int minutes)
    {
        setMinutes(minutes);

        Timer timer = new Timer();

        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                if (currentChange == 0) return;

                currentSeconds += currentChange;
                if(currentSeconds < 0)
                {
                    TurnTimer.this.setFill(Color.rgb(200, 30, 30));
                    finished = true;
                    timer.cancel();
                }
                TurnTimer.this.setText(formatTime());
            }
        };

        timer.scheduleAtFixedRate(task, 1000, 1000);
    }

    synchronized public boolean isFinished()
    {
        return finished;
    }

    public void start()
    {
        this.currentChange = this.change;
    }
    public void stop()
    {
        this.currentChange = 0;
    }

    private String formatTime()
    {
        String formatted;

        if(currentSeconds < 0) return "00:00";

        int minutes = currentSeconds / 60;
        int seconds = currentSeconds % 60;

        String sMinutes = "";
        String sSeconds = "";

        if(minutes < 10) sMinutes = "0" + minutes;
        else             sMinutes = String.valueOf(minutes);

        if(seconds < 10) sSeconds = "0" + seconds;
        else             sSeconds = String.valueOf(seconds);

        formatted = sMinutes + ":" + sSeconds;

        return formatted;
    }

    public void setMinutes(int minutes)
    {
        this.seconds = minutes * 60;
        this.currentSeconds = (minutes == -1) ? 0 : this.seconds;
        this.change = (minutes == -1) ? 1 : -1;
        this.currentChange = 0;
        this.setText(formatTime());
    }
}
