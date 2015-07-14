/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JPopupMenu;

/**************************************************************************
 * *************** CommandAction Class ****** ******************************
 **************************************************************************/

/**
 * Simple action to for the popup window.
 * To use - override perform method. 
 * 
 * Created on Feb 4, 2011
 * @author Eugene Ryzhikov
 *
 * This class is used in TableFilterColumnPopup
 */
public class CommandAction extends AbstractAction {
    
    JPopupMenu menu;

    public CommandAction(String name, Icon icon) {
        super(name, icon);

        if ( icon != null ) {
            putValue(Action.SHORT_DESCRIPTION, name);
            putValue(Action.NAME, null);
        }

    }

    public CommandAction( String name ) {
        super(name);
    }

    @Override
    public final void actionPerformed(ActionEvent e) {
        if ( perform() ) hide(); // always returns true
    }
    /**
     * Preforms action
     * @return true if popup should be closed
     * 
     * This method is overriden in TableFilterColumnPopup
     */
    protected boolean perform(){
        return true;
    }

    public void setMenu(JPopupMenu menu) {
        this.menu = menu;
    }
    
    /**
     * Hides popup
     */
    public final void hide() {
        menu.setVisible(false);
    }
}
