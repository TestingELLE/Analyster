
package com.elle.analyster.logic;

import com.elle.analyster.presentation.AnalysterWindow;
import com.elle.analyster.presentation.LogWindow;
import java.awt.Component;
import javax.swing.JLabel;

/**
 * LoggingAspect
 * @author Carlos Igreja
 * @since  Feb 23, 2016
 */
public class LoggingAspect {

    // variables
    private static JLabel infoLbl = AnalysterWindow.informationLabel;
    private static JLabel srchInfoLbl = AnalysterWindow.searchInformationLabel;
    
    private static void addMsg(String msg){
        LogWindow.addMessage(msg);
    }
    
    private static void addMsgWthDate(String msg){
        LogWindow.addMessageWithDate(msg);
    }
    
    private static void timerCntDwn(int time){
        AnalysterWindow.startCountDownFromNow(time);
    }
    
    public static void loadingBegin(){
        System.out.println("loading ...");
    }
    
    public static void loadingEnd(){
        System.out.println("");
    }
    
    public static void afterReturn(String msg){
        
    }
    
    public static void afterThrown(Exception e){
        
    }
}
