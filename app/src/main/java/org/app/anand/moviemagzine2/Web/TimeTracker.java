package org.app.anand.moviemagzine2.Web;

/**
 * Created by User on 11/3/2016.
 */

import java.util.Calendar;

public class TimeTracker {

    long current;
    long previous;
    int secondsInDay=86400;
    int secondsInMonth=2592000;
    Calendar c;

    public TimeTracker()
    {
        Calendar c = Calendar.getInstance();
        current=c.getTimeInMillis()/1000;
        previous=current - secondsInDay;
    }

    public long getNow()
    {
        c = Calendar.getInstance();
        c.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH,Calendar.HOUR_OF_DAY,Calendar.MINUTE,Calendar.SECOND);
        return c.getTimeInMillis();
    }
    public long getCurrent() {
        return current;
    }

    public long getPrevious() {
        return previous;
    }
    public long getMonthAgo(long current)
    {
        return (current-secondsInMonth);
    }
}