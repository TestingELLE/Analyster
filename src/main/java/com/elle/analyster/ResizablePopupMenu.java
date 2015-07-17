
package com.elle.analyster;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**************************************************************************
 * *************** ResizablePopupMenu Class ****** ************************
 **************************************************************************/

public class ResizablePopupMenu extends JPopupMenu implements PopupMenuListener {

    // attributes
    private static final long serialVersionUID = 1L;
    private static final int DOT_SIZE = 2;
    private static final int DOT_START = 2;
    private static final int DOT_STEP = 4;
    private final boolean resizable;
    private PopupMenuResizer popupMenuResizer;

    /**
     * CONSTRUCTOR
     * ResizablePopupMenu
     * @param resizable 
     */
    public ResizablePopupMenu( boolean resizable ) {
        super();
        this.resizable = resizable;
        if ( resizable ) popupMenuResizer.decorate(this);
        addPopupMenuListener(this);
    }

    /**
     * popupMenuWillBecomeVisible
     * @param e 
     */
    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}

    /**
     * popupMenuWillBecomeInvisible
     * @param e 
     */
    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

    /**
     * popupMenuCanceled
     * @param e 
     */
    @Override
    public  void popupMenuCanceled(PopupMenuEvent e) {}

    /**
     * paintChildren
     * @param g 
     */
    @Override
    public void paintChildren(Graphics g) {
        super.paintChildren(g);
        if ( resizable ) drawResizer(g);
    }

    /**
     * drawResizer
     * @param g 
     */
    private void drawResizer(Graphics g) {

        int x = getWidth()-2;
        int y = getHeight()-2;

        Graphics g2 = g.create();

        try {
            for ( int dy = DOT_START, j = 2; j > 0; j--, dy += DOT_STEP ) {
                for( int dx = DOT_START, i = 0; i < j; i++, dx += DOT_STEP ) {
                    drawDot( g2, x-dx, y-dy );
                }
            }
        } finally {
            g2.dispose();
        }

    };

    /**
     * drawDot
     * @param g
     * @param x
     * @param y 
     */
    private void drawDot( Graphics g, int x, int y) {
        g.setColor(Color.WHITE);
        g.fillRect( x, y, DOT_SIZE, DOT_SIZE);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect( x-1, y-1, DOT_SIZE, DOT_SIZE);
    }

}
