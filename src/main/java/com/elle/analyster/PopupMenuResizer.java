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


    /**
     * CONSTRUCTOR
     * PopupMenuResizer
     * @param menu 
     */
    private PopupMenuResizer( JPopupMenu menu ) {
        this.menu = menu;
        this.menu.setLightWeightPopupEnabled(true);
        menu.addMouseListener(this);
        menu.addMouseMotionListener(this);
    }

    /**
     * decorate
     * Very strange behavior
     * If this method is not static I get an unknown table error when
     * the program boots up.
     * I believe it is a custom error I made throwUnknownTableException
     * It is working for now so that I can at least show the UML relation.
     * @param menu 
     */
    public static void decorate( JPopupMenu menu ) {
        new PopupMenuResizer( menu );
    }
}
