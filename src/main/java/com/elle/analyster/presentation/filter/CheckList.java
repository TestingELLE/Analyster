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
    private static final CheckListEditor checkBoxEditor = new CheckListEditor();
    
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

        if ( !isEditorAttached() ) list.addMouseListener(checkBoxEditor);
        this.list.setCellRenderer(new CheckListRenderer());
        
        setupKeyboardActions(list);

    }

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
    
    private boolean isEditorAttached() {
        
        for( MouseListener ml: list.getMouseListeners() ) {
            if ( ml instanceof CheckListEditor ) return true;
        }
        return false;
        
    }
    
    public JList getList() {
        return list;
    }
    
    /**
     * Sets data to a check list. Simplification for setting new the model 
     * @param data
     */
    public void setData( Collection<T> data ) {
        setModel( new DefaultCheckListModel<T>(data));
    }
    
    /**
     * Sets the model for check list.
     * @param model
     */
    public void setModel( ICheckListModel<T> model ) {
        list.setModel(model);
    }
    
    @SuppressWarnings("unchecked")
    public ICheckListModel<T> getModel() {
        return (ICheckListModel<T>) list.getModel();
    }

    /**
     * Returns a collection of checked items. 
     * @return collection of checked items. Empty collection if nothing is selected
     */
    public Collection<T> getCheckedItems() {
        return getModel().getCheckedItems();
    }

    /**
     * Resets checked elements 
     * @param elements
     */
    public void setCheckedItems( Collection<T> elements ) {
        getModel().setCheckedItems(elements);
    }
    
    /**
     * Filters list view without losing actual data
     * @param pattern
     * @param translator
     */
    public void filter( String pattern, IListFilter listFilter ) {
        getModel().filter(pattern, listFilter);
    }
    
    public void toggleIndex( int index ) {
        if ( index >= 0 && index < list.getModel().getSize()) {
            ICheckListModel<T> model = getModel();
            model.setCheckedIndex(index, !model.isCheckedIndex(index));
        }
    }
    
}
