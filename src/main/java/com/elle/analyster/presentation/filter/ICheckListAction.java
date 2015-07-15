package com.elle.analyster.presentation.filter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 
 * This is used as a type in ActionCheckListModel.
 * The nested class CheckAll also implements this interface.
 * @param <T> 
 */
public interface ICheckListAction<T> {
    
    void check(ICheckListModel<T> model, boolean value);
    
    /**
     * CLASS CheckAll
     * 1 usage from ActionCheckListModel
     * @param <T> 
     */
    public static class CheckAll<T> implements ICheckListAction<T> {

        @Override
        public String toString() {
            return "(All)";
        }

        @SuppressWarnings("unchecked")
        @Override
        public void check(ICheckListModel<T> model, boolean value) {
            Collection<T> items = new ArrayList<>();
            if (value) {
                for( int i=0, s=model.getSize(); i<s; i++ ) {
                    items.add((T) model.getElementAt(i));
                }
            }
            model.setCheckedItems( items );

        }
        
    }
    
}