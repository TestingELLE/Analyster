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

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public abstract class PopupWindow {

    // class variables
    private JPopupMenu menu;
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
    
    /**************************************************************************
     * *************** ResizablePopupMenu Class ****** ************************
     **************************************************************************/

    class ResizablePopupMenu extends JPopupMenu implements PopupMenuListener {

        private static final long serialVersionUID = 1L;

        private static final int DOT_SIZE = 2;
        private static final int DOT_START = 2;
        private static final int DOT_STEP = 4;

        private final boolean resizable;

        public ResizablePopupMenu( boolean resizable ) {
            super();
            this.resizable = resizable;
            if ( resizable ) PopupMenuResizer.decorate(this);
            //addPopupMenuListener(this);
        }

        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

        @Override
        public  void popupMenuCanceled(PopupMenuEvent e) {}

        @Override
        public void paintChildren(Graphics g) {
            super.paintChildren(g);
            if ( resizable ) drawResizer(g);
        }

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

        private void drawDot( Graphics g, int x, int y) {
            g.setColor(Color.WHITE);
            g.fillRect( x, y, DOT_SIZE, DOT_SIZE);
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect( x-1, y-1, DOT_SIZE, DOT_SIZE);
        }

    }


    /**************************************************************************
     * *************** PopupWindow Class methods ******************************
     **************************************************************************/
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

    /**
     * Hides popup
     */
    public final void hide() {
        menu.setVisible(false);
    }

    protected void beforeShow() {}

    protected void beforeHide() {}
    
    
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

        private static final long serialVersionUID = 1L;

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
            if ( perform() ) hide();
        }
        /**
         * Preforms action
         * @return true if popup should be closed
         */
        protected boolean perform(){
            return true;
        }
    }
}


/**************************************************************************
 * *************** PopupMenuResizer Class ****** ************************
 **************************************************************************/

/**
 * Allows to resize popup with the mouse.
 *
 * Created on Aug 6, 2010
 * @author exr0bs5
 *
 */
final class PopupMenuResizer extends MouseAdapter {

    // class attributes
    private final JPopupMenu menu;
    private static final int REZSIZE_SPOT_SIZE = 10;
    private Point mouseStart = new Point( Integer.MIN_VALUE, Integer.MIN_VALUE );
    private Dimension startSize;
    private boolean isResizing = false;


    // CONSTRUCTOR
    private PopupMenuResizer( JPopupMenu menu ) {
        this.menu = menu;
        this.menu.setLightWeightPopupEnabled(true);
        menu.addMouseListener(this);
        menu.addMouseMotionListener(this);
    }

    public static void decorate( JPopupMenu menu ) {
        new PopupMenuResizer( menu );
    }
    
    private boolean isInResizeSpot( Point point ) {

        if ( point == null ) return false;

        Rectangle resizeSpot = new Rectangle(
            menu.getWidth()-REZSIZE_SPOT_SIZE,
            menu.getHeight()-REZSIZE_SPOT_SIZE,
            REZSIZE_SPOT_SIZE,
            REZSIZE_SPOT_SIZE );

        return resizeSpot.contains(point);

    }

    @Override
    public void mouseMoved(MouseEvent e) {

        menu.setCursor(
           Cursor.getPredefinedCursor(
              isInResizeSpot( e.getPoint() )? Cursor.SE_RESIZE_CURSOR: Cursor.DEFAULT_CURSOR ));
    }

    private Point toScreen( MouseEvent e ) {
        
        Point p = e.getPoint();
        SwingUtilities.convertPointToScreen(p, e.getComponent());
        return p;
        
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        mouseStart = toScreen(e);
        startSize = menu.getSize();
        isResizing = isInResizeSpot(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseStart = new Point( Integer.MIN_VALUE, Integer.MIN_VALUE );
        isResizing = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        if ( !isResizing ) return;

        Point p = toScreen(e);
        
        int dx = p.x - mouseStart.x;
        int dy = p.y - mouseStart.y;

        
        Dimension minDim = menu.getMinimumSize();
//        Dimension maxDim = menu.getMaximumSize();
        Dimension newDim = new Dimension(startSize.width + dx, startSize.height + dy);

        if ( newDim.width >= minDim.width && newDim.height >= minDim.height /*&&
             newDim.width <= maxDim.width && newDim.height <= maxDim.height*/    ) {
            menu.setPopupSize( newDim );
        }

    }
}
    
