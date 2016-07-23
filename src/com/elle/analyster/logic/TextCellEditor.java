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
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.table.TableCellEditor;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;

/**
 *
 * @author fuxiaoqian
 */
public class TextCellEditor extends DefaultCellEditor
        implements TableCellEditor {

//    JFormattedTextField cellText;
//    Object cellValue;
    UndoManager undo;
    protected static final String UNDO = "undo";
    protected static final String REDO = "redo";

    public TextCellEditor() {
        
        super(new JTextField());
        JTextField editor = (JTextField) super.editorComponent;
        Border border = UIManager.getBorder("Table.cellNoFocusBorder");
        if (border != null) {
            editor.setBorder(border);
        }
        editor.setMargin(new java.awt.Insets(-2, -2, -2, -2));
        JTextField cellEditor = (JTextField) super.editorComponent;
        undo = new UndoManager();
        cellEditor.getDocument().addUndoableEditListener(
                new UndoableEditListener() {

                    @Override
                    public void undoableEditHappened(UndoableEditEvent e) {
                        undo.addEdit(e.getEdit());
                    }
                });
        AbstractAction undoAction = new AbstractAction(UNDO) {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (undo.canUndo()) {
                    undo.undo();
                }
            }
        };
        AbstractAction redoAction = new AbstractAction(REDO) {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (undo.canRedo()) {
                    undo.redo();
                }
            }

        };
        // Create an undo action and add it to the text component
        cellEditor.getActionMap().put(UNDO, undoAction);
        cellEditor.getActionMap().put(REDO, redoAction);

        // Bind the undo action to ctl-Z
        cellEditor.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.
                getDefaultToolkit().getMenuShortcutKeyMask()), UNDO);
        cellEditor.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.
                getDefaultToolkit().getMenuShortcutKeyMask()), REDO);
        
        //bind the copy, paste and cut function
        cellEditor.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit()
                .getMenuShortcutKeyMask()), DefaultEditorKit.copyAction);
        cellEditor.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit()
                .getMenuShortcutKeyMask()), DefaultEditorKit.pasteAction);
        cellEditor.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit()
                .getMenuShortcutKeyMask()), DefaultEditorKit.cutAction);

    }

    @Override
    public Object getCellEditorValue() {
        return super.getCellEditorValue();
    }

    @Override
    public JTextField getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        return (JTextField)super.getTableCellEditorComponent(table, value, isSelected, row, column);
    }

}
