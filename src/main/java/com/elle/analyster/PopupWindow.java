/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster;

/**
 *
 * @author danielabecker
 */

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;

public abstract class PopupWindow {

    // class variables
    private ResizablePopupMenu menu;
    private Dimension defaultSize = new Dimension(100,100);

    /**
     * CONSTRUCTOR
     * PopupWindow
     * @param resizable 
     */
    public PopupWindow( boolean resizable ) {
        
        // ResizablePopupMenu is a JPopupMenu
        menu = new ResizablePopupMenu( resizable ) {

            private static final long serialVersionUID = 1L;

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                if ( menu.getComponentCount() == 0 ) {
                    JComponent content = buildContent();
                    defaultSize = content.getPreferredSize();
                    
                    menu.add( content );

                }
                beforeShow();
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                beforeHide();
            }

        };
    }

    public final Dimension getDefaultSize() {
        return defaultSize;
    }

    public final Dimension getPreferredSize() {
        return menu.getPreferredSize();
    }

    public final void setPreferredSize( Dimension preferredSize ) {
        menu.setPreferredSize(preferredSize);
    }

    /**
     * Override this method to add content yo the owner.
     * This method is only executed when owner has no subcomponents
     * @param owner
     */
    protected abstract JComponent buildContent();

    /**
     * Shows Popup in predefined location
     * @param invoker
     * @param x
     * @param y
     */
    public void show( Component invoker, int x, int y ) {
        menu.show( invoker, x, y );
    }

    /**
     * Shows popup in predefined location
     * @param invoker
     * @param location
     */
    public void show( Component invoker, Point location ) {
        show( invoker, location.x, location.y );
    }

    protected void beforeShow() {}

    protected void beforeHide() {}

    public JPopupMenu getMenu() {
        return menu;
    }
    
    
}
    
