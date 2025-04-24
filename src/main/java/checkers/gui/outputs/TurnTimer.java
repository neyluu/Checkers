package checkers.gui.outputs;

import javafx.scene.text.Text;

import java.util.Timer;
import java.util.TimerTask;


public class TurnTimer extends Text
{
    private final int seconds;
    private int currentSeconds;

    public TurnTimer(int minutes)
    {
        this.seconds = minutes * 60;
        this.currentSeconds = (minutes == -1) ? 0 : this.seconds;
        int change = (minutes == -1) ? 1 : -1;

        Timer timer = new Timer();

        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                System.out.println(currentSeconds);
                currentSeconds += change;
                if(currentSeconds < 0) timer.cancel();
                TurnTimer.this.setText(formatTime());
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    public void start()
    {

    }
    public void stop()
    {

    }

    private String formatTime()
    {
        String formatted;

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
}
