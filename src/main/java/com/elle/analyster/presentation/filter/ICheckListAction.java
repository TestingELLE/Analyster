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

}