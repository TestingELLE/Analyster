/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**************************************************************************
 * *************** PopupMenuResizer Class ****** ************************
 **************************************************************************/

/**
 * Allows to resize popup with the mouse.
 *
 * Created on Aug 6, 2010
 * @author exr0bs5
 *
 * called once by ResizablePopupMenu Class
 */
public class PopupMenuResizer extends MouseAdapter {

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

    public void decorate( JPopupMenu menu ) {
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
