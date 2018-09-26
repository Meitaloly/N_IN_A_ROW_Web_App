package GameLogic;

import java.util.TimerTask;

public class GameTimer extends TimerTask {
    private Integer min = 0;
    private Integer sec =0;
    private String time;

    public void tick()
    {
        sec++;
        if(sec > 59)
        {
            sec =0;
            min++;
        }
    }

    public String getTime()
    {
        String minStr= min.toString();
        String secStr=sec.toString();

        if(min < 10) {
            minStr = "0"+min;
        }

        if(sec<10) {
            secStr = "0"+sec;

        }
        time = minStr + ":" + secStr;
        return time;
    }

    public void restTime()
    {
        min =0;
        sec =0;
    }

    @Override
    public void run() {
        tick();
    }
}
