package com.elle.analyster.presentation.filter;

import javax.swing.*;
import java.util.*;

/**
 * Default model for check list. It is based on the list of items
 * Implementation of checks is based on HashSet of checked items
 *
 * @author Eugene Ryzhikov
 *
 * @param <T> list element type
 * 
 * called from actionChecklist and TableFilterColumnPopup
 */
public class DefaultCheckListModel<T> extends AbstractListModel implements ICheckListModel<T> {

    // attributes
    private Set<T> checks = new HashSet<T>();
    private final List<T> dataList = new ArrayList<T>();
    private final Set<T> dataSet = new HashSet<T>();
    private List<T> filteredDataList = null;
    private Set<T> filteredDataSet = null;
    private CheckListFilterType filter;

    
    /**
     * CONSTRUCTOR 
     * DefaultCheckListModel
     * called once from TableFilterColumnPopup
     * @param data 
     */
    public DefaultCheckListModel( Collection<? extends T> data ) {

        // Not sure what the point of this is
        // it takes a collection to create two identical collections
        if ( data == null ) return;
        // this is storing all the fields for that column 
        // this must be what is used to populate the fields to check
        for (T object : data) {
            dataList.add(object);
            dataSet.add(object);
        }
    }

    /* (non-Javadoc)
     * @see org.oxbow.swingbits.list.ICheckListModel#getSize()
    method calls: Action,checklist,checkall
     */
    @Override
    public int getSize() {
        return dataList().size();
    }

    /**
     * dataList
     * @return 
     * method calls: this class
     */
    private List<T> dataList() {
        return filteredDataList == null ? dataList : filteredDataList;
    }

    /**
     * dataSet
     * @return 
     * method calls: this class
     */
    private Set<T> dataSet() {
        return filteredDataSet == null ? dataSet : filteredDataSet;
    }

    /* (non-Javadoc)
     * @see org.oxbow.swingbits.list.ICheckListModel#getElementAt(int)
    
    This is from ListModel 
    used in few classes
     */
    @Override
    public Object getElementAt(int index) {
        return dataList().get(index);
    }

    /* (non-Javadoc)
     * @see org.oxbow.swingbits.list.ICheckListModel#isChecked(int)
     */

    // this is called a lot about 4 classes
    public boolean isCheckedIndex( int index ) {
        return checks.contains(dataList().get(index));
    }

    /* (non-Javadoc)
     * @see org.oxbow.swingbits.list.ICheckListModel#setChecked(int, boolean)
     */

    // called from action checklist and mouse
    public void setCheckedIndex( int index, boolean value ) {
        T o = dataList().get(index);
        if ( value ) checks.add(o); else checks.remove(o);
        fireContentsChanged(this, index, index);
    }

    /* (non-Javadoc)
     * @see org.oxbow.swingbits.list.ICheckListModel#getChecked()
     */

    // called from action and checklist
    public Collection<T> getCheckedItems() {
        List<T> items = new ArrayList<T>(checks);
        items.retainAll(dataSet());
        return Collections.unmodifiableList( items );
    }

    /* (non-Javadoc)
     * @see org.oxbow.swingbits.list.ICheckListModel#setChecked(java.util.Collection)
     */

    // called from action checklist checkall and mouse
    public void setCheckedItems( Collection<T> items ) {
        Set<T> correctedItems = new HashSet<T>(items);
        correctedItems.retainAll(dataSet());
        checks = correctedItems;
        // this is a method of the AbstractList
        // it is used after content in the list is changed
        fireContentsChanged(this, 0, checks.size()-1);
    }

    /**
     * filter
     * @param pattern
     * @param listFilter 
     * method calls: action, table, checklist
     */
    public void filter( String pattern, CheckListFilterType listFilter ) {

        if ( pattern == null || pattern.trim().length() == 0 ) {
            filteredDataList = null;
            filteredDataSet = null;
        } else {

            filter = listFilter; 

            String p = pattern.toLowerCase();

            List<T> fDataList = new ArrayList<T>();
            Set<T> fDataSet = new HashSet<T>();

            Object value;
            for (T o : dataList) {
                value = o;
                if ( filter.include(value.toString(), p)) {
                    fDataList.add(o);
                    fDataSet.add(o);
                }
            }
            filteredDataList = fDataList;
            filteredDataSet = fDataSet;
        }

        fireContentsChanged(this, 0, dataList.size() - 1);

    }


}