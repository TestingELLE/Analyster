package com.elle.analyster.presentation.filter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * JTableFilter class
 * @author cigreja
 */
public class JTableFilter {

    // no set - filter cleared; set - some kind of filtering
    private final Map<Integer,Set<DistinctColumnItem>> data = new HashMap<Integer,Set<DistinctColumnItem>>();
    
    // this is a nested class here
    private TableRowFilter filter = new TableRowFilter();
    
    private int columnIndex = -1;
    
    private Collection <DistinctColumnItem> itemChecked;

    private final Map<Integer, Collection<DistinctColumnItem>> distinctItemCache
            = Collections.synchronizedMap(new HashMap<Integer, Collection<DistinctColumnItem>>());

    private JTable table = new JTable(); 
    //private TableFilterState filterState = new TableFilterState();
    
    /**
     * CONSTRUCTOR
     * JTableFilter
     * @param table 
     */
    public JTableFilter(JTable table) {
        this.table = table; 
        setupDistinctItemCacheRefresh(); 
    }
    
    /**
     * setupDistinctItemCacheRefresh
     * called from the constructor
     */
    public void setupDistinctItemCacheRefresh() {
        distinctItemCache.clear();
        this.table.addPropertyChangeListener("model", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                distinctItemCache.clear();
                TableModel model = (TableModel) e.getNewValue();
                if (model != null) {
                    model.addTableModelListener(new TableModelListener() {

                        @Override
                        public void tableChanged(TableModelEvent e) {
                            distinctItemCache.clear();
                        }
                    });
                }
            }
        });
    }

    /**
     * apply
     * called twice from Analyster methods (filterBySearch & filterByDoubleClick)
     * @param col
     * @param selectField
     */
    public void apply(int col, Object selectField) { //Create Collection from selected fields 
        
        Collection<DistinctColumnItem> item = new ArrayList<>();
        
        // handle null exceptions
        if(selectField == null) selectField = "";
        
        DistinctColumnItem distinctColumnItem =new DistinctColumnItem(selectField, col);
        item.add(distinctColumnItem);
        apply(col, item);
    }
    
    /**
     * apply
     * Called from Analyster, LoadTables, this, TableFilterColumnPopup
     * @param col
     * @param items
     */
    public void apply(int col, Collection<DistinctColumnItem> items) {
        
        // create a column map key and add this collection
        setValues(col, items); 

        // new DRS instance of the Table's RowSorter
        DefaultRowSorter<?, ?> drs = (DefaultRowSorter<?, ?>) getTable().getRowSorter();

        // get RowFilter of DRS and store as prevFilter
        RowFilter<Object, Object> prevFilter = (RowFilter<Object, Object>) drs.getRowFilter();

        // pass filter to TableRowFilter nested class 
        // set that filter to prevFilter
        filter.setParentFilter(prevFilter);

        // DRS is now passed this filter to be set
        // this points to the table filter
        drs.setRowFilter(filter); // this calls the equals method in DistinctColumnItem

        // this was the IFilterChangeListener implementation
        table.getTableHeader().repaint();
        table.getModel().getRowCount(); // not sure if this is needed
    }

    /**
     * saveFilterCriteria
     * this method is called from LoadTables & TableFilterColumnPopup classes
     * @param collection 
     */
    public void saveFilterCriteria(Collection collection) {
             itemChecked = collection;
    }

    /**
     * getFilterCriteria
     * this method is called from Analyster
     * @return 
     */
    public Collection<DistinctColumnItem> getFilterCriteria() {
        return itemChecked;
    }

    /**
     * modelChanged
     * this method is called from TableRowFilterSupport
     * @param model 
     */
    public void modelChanged(TableModel model) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        sorter.setSortsOnUpdates(true);
        getTable().setRowSorter(sorter);
    }

    /**
     * setColumnIndex
     * this method is called from LoadTables & TableFilterColumnPopup
     * @param mColumnIndex 
     */
    public void setColumnIndex(int mColumnIndex) {
        columnIndex = mColumnIndex;

    }

    /**
     * getColumnIndex
     * this method is called from Analyster
     * @return 
     */
    public int getColumnIndex() {
        return columnIndex;
    }
    
    /**
     * getTable
     * this method is used a lot
     * it is called from Analyster, LoadTables, this TableFilterColumnPopup, 
     * & TableRowFilterSupport
     * @return 
     */
    public JTable getTable() {
        return table;
    }

    /**
     * isFiltered
     * this method is called once from FilterTableHeaderRenderer
     * @param column
     * @return 
     */
    public boolean isFiltered(int column) {
        Collection<DistinctColumnItem> checks = getFilterState(column);
        return !(CollectionUtils.isEmpty(checks)) && getDistinctColumnItems(column).size() != checks.size();
    }
    
    /**
     * getFilterState
     * this method is called once from isFiltered & once from TableFilterColumnPopup
     * @param column
     * @return 
     */
    public Collection<DistinctColumnItem> getFilterState(int column) {
        return getValues(column);
    }
    
    /**
     * getDistinctColumnItems
     * this method is called once from isFiltered method and once from the
     * Analyster & TableFilterColumnPopup classes
     * @param column
     * @return 
     */
    public Collection<DistinctColumnItem> getDistinctColumnItems(int column) {
        Collection<DistinctColumnItem> result = distinctItemCache.get(column);
        if (result == null) {
            result = collectDistinctColumnItems(column);
            distinctItemCache.put(column, result);
        }
        return result;

    }
    
    /**
     * collectDistinctColumnItems
     * this method is called from getDistinctColumnItems
     * @param column
     * @return 
     */
    private Collection<DistinctColumnItem> collectDistinctColumnItems(int column) {
        Set<DistinctColumnItem> result = new TreeSet<DistinctColumnItem>(); // to collect unique items
        for (int row = 0; row < table.getModel().getRowCount(); row++) {
            Object value = table.getModel().getValueAt(row, column);
            
            // handle null exception
            if(value == null)value = "";
            
            result.add(new DistinctColumnItem(value, row));
        }
        return result;
    }

    /**
     * includeRow
     * this method is called once by the nested class TableRowFilter 
     * method include that overrides the Java API method of the RowFilter class
     * @param row
     * @return 
     */
    public boolean includeRow(JTableFilter.Row row) {
        return include(row);
    }
    
    /**************************************************************************
     *************************** TableFilterState Methods *********************
     **************************************************************************/

    /**
     * Clears filtering for specific column
     */
    public void clear( int column ) {
        data.remove(column);
    }
    
    
    /**
     * Clears all filtering
     */
    public void clear() {
        data.clear();
    }
    
    /**
     * prepareValueSet
     * creates an array of data for a column if one doesn't exist
     * then adds the array to the data map and the column index is the key
     * @param column
     * @return Set<DistinctColumnItem> // the array of data for that column
     */
    private Set<DistinctColumnItem> prepareValueSet( int column ) {
        Set<DistinctColumnItem> vals =  data.get(column);
        if ( vals == null ) {
            vals = new HashSet<DistinctColumnItem>();
            data.put(column, vals);
        }
        return vals;
    }
    
    
    /**
     * Adds filter value for specified column 
     * @param column // int column index
     * @param value // DistinctColumnItem
     */
    public void addValue( int column, DistinctColumnItem value ) {
        prepareValueSet(column).add(value);
    }

    
    /**
     * Adds a collection of filter values for specified column 
     * @param column
     * @param values
     */
    public void addValues( int column, Collection<DistinctColumnItem> values ) {
        prepareValueSet(column).addAll(values);
    }

    /**
     * Resets a collection of filter values for specified column
     * @param column
     * @param values
     */
    public void setValues( int column, Collection<DistinctColumnItem> values ) {
        
        data.remove(column); // remove this column key from map
        
        // if values is not empty
        if ( !CollectionUtils.isEmpty(values)) {
            
            // create a column map key and add this collection
            prepareValueSet(column).addAll(values);
        }
    }
    
    /**
     * 
     * @param column
     * @return 
     */
    public Collection<DistinctColumnItem> getValues( int column ) {
        Set<DistinctColumnItem> vals =  data.get(column);
        return vals == null? Collections.<DistinctColumnItem>emptySet(): vals;
    }
    
    /**
     * Standard test for row inclusion using current filter values
     * @param entry
     * @return true if row has to be included
     */
    public boolean include( JTableFilter.Row entry ) {                 //////////////// Include in new data
    
        // check every column
        for( int col=0; col< entry.getValueCount(); col++ ) {
            
            // get filter values
            Collection<DistinctColumnItem> values = getValues(col);
            if ( CollectionUtils.isEmpty(values) ) continue; // no filtering for this column
            
            // get value
            Object value = entry.getValue(col);
            
            // handle null exception
            if(entry.getValue(col) == null) value = "";
            
            
            if ( !values.contains( new DistinctColumnItem( value, 0))) {
                return false;
            } 
        }

        return true;
        
    }
    
    /***************************************************************************
     ********************** End TableFilterState Methods **********************
     **************************************************************************/
    
    
    /**
     * NESTED CLASS
     * TableRowFilter
     * this class is used once to create an instance in this outer class
     * it is also called as another instance in method execute in the outer class
     */
    class TableRowFilter extends RowFilter<Object, Object>  {

        private RowFilter<Object, Object> parentFilter; // extend and then make one?

        /**
         * setParentFilter
         * this method is called once from apply
         * @param parentFilter 
         */
        public void setParentFilter(RowFilter<Object, Object> parentFilter) {
            this.parentFilter = (parentFilter == null || parentFilter == this) ? null : parentFilter;
        }

        /**
         * include
         * this method is only called once from itself
         * @param entry
         * @return 
         */
        @Override
        public boolean include(final Entry<? extends Object, ? extends Object> entry) {

            // test
            // this code only appears to work for the search text box
//            
//            if(entry.getStringValue(col).equalsIgnoreCase(str)){
//                return true;
//            }
//            
            // use parent filter condition
            if (parentFilter != null && !parentFilter.include(entry)) {
                return false;
            }

            return includeRow(new Row() { // Row is the nested interface

                /**
                 * getValueCount
                 * this method is called once from TableFilterState
                 * @return 
                 */
                @Override
                public int getValueCount() {
                    return entry.getValueCount();
                }

                /**
                 * getValue
                 * this method is called twice from TableFilterState
                 * @param column
                 * @return 
                 */
                @Override
                public Object getValue(int column) {
                    return entry.getValue(column);
                }
            });
        }
    }
    
    /**
     * NESTED INTERFACE 
     * Row
     * this interface is called twice in this outer class
     * and once from TableFilterState
     */
    public interface Row {
        
        /**
         * getValueCount
         * this method is called once from TableFilterState
         * @return 
         */
        int getValueCount();

        /**
         * getValue
         * this method is called twice from TableFilterState
         * @param column
         * @return 
         */
        Object getValue(int column);
    }

}
