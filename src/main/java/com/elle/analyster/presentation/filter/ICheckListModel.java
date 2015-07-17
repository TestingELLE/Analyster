package com.elle.analyster.presentation.filter;

import javax.swing.*;
import java.util.Collection;

/**
 * INTERFACE ICheckListModel<T> 
 * this is implemented in 
 * ActionCheckListModel & DefaultCheckListModel
 * Looks like methods for checking the state of the checks in the checklist
 * @param <T> 
 */
public interface ICheckListModel<T> extends ListModel{

    /**
     * Returns the check state of the element at specified position
     * @param index element index
     * @return true if element at specified position is checked
     * @throws IndexOutOfBoundsException if index is out of range
     */
    boolean isCheckedIndex(int index);

    /**
     * Sets the check state of the element at specified position
     * @param index element index
     * @param value 
     * @throws IndexOutOfBoundsException if index is out of range
     */
    void setCheckedIndex(int index, boolean value);

    /**
     * Returns a collections of checked items
     * @return
     */
    Collection<T> getCheckedItems();

    /**
     * Sets checked items
     * @param items
     */
    void setCheckedItems(Collection<T> items);
    
    /**
     * Allows filtered view. Setting empty or null filter will clear filter and show all items
     * @param pattern the pattern the filter will match on
     * @param translator object to string translator to aid the search
     */
    void filter(String pattern, IListFilter listFilter);

}