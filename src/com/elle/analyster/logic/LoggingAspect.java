
package com.elle.analyster.logic;

import com.elle.analyster.presentation.AnalysterWindow;
import static com.elle.analyster.presentation.AnalysterWindow.informationLabel;
import static com.elle.analyster.presentation.AnalysterWindow.searchInformationLabel;
import com.elle.analyster.presentation.LogWindow;

/**
 * LoggingAspect
 * @author Carlos Igreja
 * @since  Feb 23, 2016
 */
public class LoggingAspect {
    
    public static void addLogMsg(String msg){
        LogWindow.addMessage(msg);
    }
    
    public static void addLogMsgWthDate(String msg){
        LogWindow.addMessageWithDate(msg);
    }
    
    public static void timerCntDwn(int time){
        AnalysterWindow.startCountDownFromNow(time);
    }
    
    public static void addInfoMsg(String msg){
        informationLabel.setText(msg);
    }
    
    public static void addSearchInfoMsg(String msg){
        searchInformationLabel.setText(msg);
    }
    
    public static void afterReturn(String msg){
        
        // display message to user
        informationLabel.setText(msg);
        timerCntDwn(10);
        
        // add message to log
        addLogMsgWthDate(msg);
        
    }
    
    public static void afterThrown(Exception e){
        
        // display message to user
        informationLabel.setText("An error occurred. Please see log file.");
        timerCntDwn(10);
        
        // add error message to log
        addLogMsgWthDate("An exception was thrown: ");
        
        // log exception 
        addLogMsg("Error message: " + e.getMessage());
        StackTraceElement element = e.getStackTrace()[0]; // first element
        addLogMsg("Package.Class: " + element.getClassName());
        addLogMsg("Method: " + element.getMethodName());
        addLogMsg("Line: " + element.getLineNumber());
    }
}
