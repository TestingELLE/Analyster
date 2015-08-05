
package com.elle.analyster.logic;

import java.util.Comparator;
import java.util.Date;

/**
 * LogMessage class
 * this class stores log message information
 * @author Carlos Igreja
 */
public class LogMessage {

    private final Date date;
    private final String message;

    public LogMessage(Date date, String message) {
        this.date = date;
        this.message = message;
    }

    public Date getDate(){ return date;}
    public String getMessage(){return message;}

    public static class SortByMostRecentDateFirst implements Comparator<LogMessage>
    {
        @Override
        public int compare(LogMessage c, LogMessage c1) {
            return c1.getDate().compareTo(c.getDate());
        }    
    }

    public static class SortByMostRecentDateLast implements Comparator<LogMessage>
    {
        @Override
        public int compare(LogMessage c, LogMessage c1) {
            return c.getDate().compareTo(c1.getDate());
        }    
    }
}
