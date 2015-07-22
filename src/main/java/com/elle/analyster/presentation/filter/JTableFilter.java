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
import javax.swing.table.TableColumn;

/**
 * JTableFilter class
 * @author cigreja
 */
public class JTableFilter {
    
    // class attributes
    private boolean actionsVisible; 
    private int filterIconPlacement;
    private boolean useTableRenderers;
    private int columnIndex;
    
    // components
    private JTable table; 
    private TableRowFilter filter; // this is a nested class here
    
    // Arrays
    private Map<Integer,Set<DistinctColumnItem>> data; // distincted items to filter
    private Collection <DistinctColumnItem> itemChecked;


    /**
     * CONSTRUCTOR
     * JTableFilter
     * @param table 
     */
    public JTableFilter(JTable table) {
        
        actionsVisible = true; // this should start at false and turned on
        filterIconPlacement = SwingConstants.LEADING;
        useTableRenderers = false;
        columnIndex = -1;
        filter = new TableRowFilter(); // this is a nested class here
        this.table = table; 
        data = new HashMap<>(); // this map stores the distinct items for each column
    }

    /**
     * apply
     * called twice from Analyster methods (filterBySearch & filterByDoubleClick)
     * @param col
     * @param selectField
     */
    public void apply(int col, Object selectField) { //Create Collection from selected fields 
        
        Collection<DistinctColumnItem> items = new ArrayList<>();
        
        // handle null exceptions
        if(selectField == null) selectField = "";
        
        DistinctColumnItem distinctColumnItem = new DistinctColumnItem(selectField, col);
        items.add(distinctColumnItem);
        apply(col, items);
    }
    
    /**
     * apply
     * Called from Analyster, this, TableFilterColumnPopup
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
     * this method is called from Analyster & TableFilterColumnPopup classes
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
     * this method is called from this
     * @param model 
     */
    public void modelChanged(TableModel model) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        sorter.setSortsOnUpdates(true);
        getTable().setRowSorter(sorter);
    }

    /**
     * setColumnIndex
     * this method is called from Analyster & TableFilterColumnPopup
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
     * it is called from Analyster, this, TableFilterColumnPopup, 
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
        return collectDistinctColumnItems(column);
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
    
    public TableRowFilter getTableRowFilter(){
        return filter;
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
            Set<DistinctColumnItem> vals =  data.get(column);
            if ( vals == null ) {
                vals = new HashSet<DistinctColumnItem>();
                data.put(column, vals);
            }
            vals.addAll(values);
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
     * setActionsVisible
     * @param visible 
     */
    public void setActionsVisible( boolean visible ) {actionsVisible = visible;}
    
    /**
     * getActionsVisible
     * @return 
     */
    public boolean getActionsVisible(){return actionsVisible;}
    
    /**
     * getUseTableRenderers
     * @return 
     */
    public boolean getUseTableRenderers(){return useTableRenderers;}      
            
    /**
     * apply
     * this was from the TableFilterSupport class
     */
    public void apply() {

        JTable table = this.getTable();
        
        FilterTableHeaderRenderer headerRenderer =
                new FilterTableHeaderRenderer(this, filterIconPlacement);
        
        this.modelChanged( table.getModel() );  // wouldn't this be the same filter?

        for( TableColumn c:  Collections.list( table.getColumnModel().getColumns()) ) {
            c.setHeaderRenderer( headerRenderer );
        }

        table.addPropertyChangeListener("model", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                // can't use this in this method
                modelChanged( (TableModel) e.getNewValue() ); 
            } 
        }); // end addPropertyChangeListener
    } // end apply
    
    
    
    /**
     * NESTED CLASS
     * TableRowFilter
     * this class is used once to create an instance in this outer class
     * it is also called as another instance in method execute in the outer class
     */
    public class TableRowFilter extends RowFilter<Object, Object>  {

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
            
            // use parent filter condition
            if (parentFilter != null && !parentFilter.include(entry)) {
                return false;
            }

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
        
    }
}
