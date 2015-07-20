/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.presentation.filter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * CLASS CheckAll
 * 1 usage from ActionCheckListModel
 * @param <T> 
 */
public class CheckAll<T> {

    /**
     * toString
     * @return 
     */
    @Override
    public String toString() {
        return "(All)";
    }

    /**
     * check
     * 0 usages
     * This method is only called when the checkAll option is selected
     * @param model
     * @param value 
     */
    public void check(ICheckListModel<T> model, boolean value) {
        Collection<T> items = new ArrayList<>();
        if (value) {
            for( int i=0; i<model.getSize(); i++ ) {
                items.add((T) model.getElementAt(i));
            }
        }
        model.setCheckedItems( items );

    }

}
