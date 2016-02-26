/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.logic;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 * @author fuxiaoqian this class is to change the text component's copy and
 * paste short cut to command c/v in mac and control c/v in windows
 */
public class ShortCutSetting {

    /**
     * Changing the text component's copy and paste short cut to command c/v in
     * mac and control c/v in windows
     */
    public static void copyAndPasteShortCut(InputMap ip) {
        ip.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit()
                .getMenuShortcutKeyMask()), DefaultEditorKit.copyAction);
        ip.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit()
                .getMenuShortcutKeyMask()), DefaultEditorKit.pasteAction);
        ip.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit()
                .getMenuShortcutKeyMask()), DefaultEditorKit.cutAction);
    }
    
    /**
     * add undo and redo short cut to text component
     */
    public static void undoAndRedoShortCut(JTextComponent textComp) {
        final UndoManager undo = new UndoManager();
        Document doc = textComp.getDocument();

        // Listen for undo and redo events
        doc.addUndoableEditListener(new UndoableEditListener() {
            public void undoableEditHappened(UndoableEditEvent evt) {
                undo.addEdit(evt.getEdit());
            }
        });

        // Create an undo action and add it to the text component
        textComp.getActionMap().put("Undo",
                new AbstractAction("Undo") {
                    public void actionPerformed(ActionEvent evt) {
                        try {
                            if (undo.canUndo()) {
                                undo.undo();
                            }
                        } catch (CannotUndoException e) {
                            LoggingAspect.afterThrown(e);
                        }
                    }
                });

        // Bind the undo action to ctl-Z
        textComp.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");

        // Create a redo action and add it to the text component
        textComp.getActionMap().put("Redo",
                new AbstractAction("Redo") {
                    public void actionPerformed(ActionEvent evt) {
                        try {
                            if (undo.canRedo()) {
                                undo.redo();
                            }
                        } catch (CannotRedoException e) {
                            LoggingAspect.afterThrown(e);
                        }
                    }
                });

        // Bind the redo action to ctl-Y
        textComp.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");
    }
}
