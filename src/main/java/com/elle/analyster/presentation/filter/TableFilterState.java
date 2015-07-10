package com.elle.analyster.presentation.filter;

import java.io.Serializable;
import java.util.*;

class TableFilterState implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // no set - filter cleared; set - some kind of filtering
    private final Map<Integer,Set<DistinctColumnItem>> data = new HashMap<Integer,Set<DistinctColumnItem>>();
    
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
    
        for( int col=0; col< entry.getValueCount(); col++ ) {
            Collection<DistinctColumnItem> values = getValues(col);
            if ( CollectionUtils.isEmpty(values) ) continue; // no filtering for this column
            
            // get value
            Object value = entry.getValue(col);
            
            // handle null exception
            if(entry.getValue(col) == null) value = "";
            
            if ( !values.contains( new DistinctColumnItem( value, 0))) {return false;}
        }
        return true;
        
    }
    
    
}