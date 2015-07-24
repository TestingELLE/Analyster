/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.presentation.filter;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.util.Collection;

/**
 * The decorator for JList which makes it work like check list
 * UI can be designed using JList and which can be later decorated to become a check list
 * @author Eugene Ryzhikov
 *
 * @param <T> list item type
 */
public class CheckList<T> {

    // class components and objects
    private final JList list;
    private static final CheckListMouseAdapter checkListMouseAdapter = new CheckListMouseAdapter();
    private CheckListRenderer checkListRenderer;
    
    /**
     * Nested class
     * Builder
     */
    public static class Builder {
        
        private JList list;

        public Builder( JList list ) {
            this.list = list == null? new JList(): list;
        }
        
        public Builder() {
            this( null );
        }
        
        public <T> CheckList<T> build() {
            return new CheckList<T>(list);
        }
        
    }
    
    
    /**
     * Wraps the standard JList and makes it work like check list 
     * @param list
     */
    private CheckList(final JList list) {

        if (list == null) throw new NullPointerException();
        this.list = list;
        this.list.getSelectionModel().setSelectionMode( ListSelectionModel.SINGLE_SELECTION);

        // call the boolean method to see if attached
        if ( !isEditorAttached() ) 
            list.addMouseListener(checkListMouseAdapter);
        
        
        checkListRenderer = new CheckListRenderer();
        this.list.setCellRenderer(checkListRenderer);
        
        setupKeyboardActions(list);

    }

    /**
     * setupKeyboardActions
     * @param list 
     */
    @SuppressWarnings("serial")
    private void setupKeyboardActions(final JList list) {
        String actionKey = "toggle-check";
        list.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0), actionKey);
        list.getActionMap().put(actionKey, new AbstractAction(){

            @Override
            public void actionPerformed(ActionEvent e) {
                toggleIndex(list.getSelectedIndex());
            }});
    }
    /**
     * isEditorAttached
     * the editor is actually a mouselistener
     * this checks if the mouselistener is attached to the Jlist
     * @return 
     */
    private boolean isEditorAttached() {
        
        for( MouseListener ml: list.getMouseListeners() ) {
            if ( ml instanceof CheckListMouseAdapter ) return true;
        }
        return false;
        
    }
    
    /**
     * getList
     * @return 
     */
    public JList getList() {
        return list;
    }
    
    /**
     * setModel
     * Sets the model for check list.
     * @param model
     */
    public void setModel( ICheckListModel<T> model ) {
        list.setModel(model);
    }
    
    /**
     * getModel
     * @return 
     */
    public ICheckListModel<T> getModel() {
        return (ICheckListModel<T>) list.getModel();
    }

    /**
     * getCheckedItems
     * Returns a collection of checked items. 
     * @return collection of checked items. Empty collection if nothing is selected
     */
    public Collection<T> getCheckedItems() {
        return getModel().getCheckedItems();
    }

    /**
     * setCheckedItems
     * Resets checked elements 
     * This called the ActionCheckListModel override
     * @param elements
     */
    public void setCheckedItems( Collection<T> elements ) {
        getModel().setCheckedItems(elements);
    }
    
    /**
     * filter
     * Filters list view without losing actual data
     * @param pattern
     * @param translator
     */
    public void filter( String pattern, CheckListFilterType listFilter ) {
        getModel().filter(pattern, listFilter);
    }
    
    /**
     * toggleIndex
     * @param index 
     */
    public void toggleIndex( int index ) {
        if ( index >= 0 && index < list.getModel().getSize()) {
            ICheckListModel<T> model = getModel();
            model.setCheckedIndex(index, !model.isCheckedIndex(index));
        }
    }
    
}
