/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.presentation.filter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * TableFilter
 * This class takes a JTable and filters the view for searching data.
 * It also applies colors to the column headers when filtering.
 * @author cigreja
 */
public class TableFilter extends RowFilter<TableModel, Integer> {
    
    // attributes
    private JTable table; // table to be filtered 
    private TableRowSorter<TableModel> sorter;
    private Map<Integer,ArrayList<Object>> filterItems; // distincted items to filter
    private Color color;
    

    /**
     * CONSTRUCTOR
     * TableFilter
     * @param table 
     */
    public TableFilter(JTable table){
        
        this.table = table;
        
        // set table sorter
        sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);
        
        // initialize filterItems
        filterItems = new HashMap<>(); 
        for(int i = 0; i < table.getColumnCount(); i++){
            filterItems.put(i, new ArrayList<>());
        }
        
        // load all initial rows with no filtering
        loadAllRows();
        
        // initialize the color for the table header when it is filtering
        color = Color.GREEN; // default color is green
        
        //test
        applyFilter();
    }
    
    /**
     * addDistinctItem
     * @param col
     * @param selectedField 
     */
    public void addFilterItem(int col, Object selectedField){
        
        if(selectedField == null) 
            selectedField = "";
        
        filterItems.get(col).add(selectedField);
    }
    
    /**
     * addDistinctItems
     * @param col
     * @param items 
     */
    public void addFilterItems(int col, ArrayList<Object> items){
        
        for(Object item: items){
            
            if(item == null)
                item = "";
            
            filterItems.get(col).add(item);
        }
    }
    
    /**
     * removeDistinctItem
     * @param col
     * @param selectedField 
     */
    public void removeFilterItem(int col, Object selectedField){
        
        if(selectedField == null) 
            selectedField = "";
        
        filterItems.get(col).remove(selectedField);
    }
    
    /**
     * removeDistinctItems
     * @param col
     * @param items 
     */
    public void removeFilterItems(int col, ArrayList<Object> items){
        
        for(Object item: items){
            
            if(item == null)
                item = "";
            
            filterItems.get(col).remove(item);
        }
    }
    
    /**
     * removeDistinctItems
     * @param col
     */
    public void removeFilterItems(int col){

        filterItems.get(col).clear();
    }
    
    /**
     * removeAllDistinctItems
     * @param col
     * @param items 
     */
    public void removeAllFilterItems(){
        
        for(int i = 0; i < filterItems.size(); i++)
            filterItems.get(i).clear();
    }
    
    /**
     * applyFilter
     */
    public void applyFilter(){
        
        sorter.setRowFilter(this);
        applyColorHeaders();
    }
    
    /**
     * addColorHeader
     * @param columnIndex
     * @param color 
     */
    public void addColorHeader(int columnIndex){
        
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setBackground(color);
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(columnIndex)
                .setHeaderRenderer(cellRenderer);
        table.getTableHeader().repaint();
    }
    
    /**
     * applyColorHeaders
     * @param color 
     */
    public void applyColorHeaders(){
        
        for(int i = 0; i < filterItems.size(); i++){
            if(filterItems.get(i).isEmpty()){
                removeColorHeader(i);
            }
            else{
                addColorHeader(i);
            }
        }
        table.getTableHeader().repaint();
    }
    
    /**
     * removeColorHeader
     * @param columnIndex
     * @param color 
     */
    public void removeColorHeader(int columnIndex){
        
        table.getColumnModel().getColumn(columnIndex)
                .setHeaderRenderer(table.getTableHeader().getDefaultRenderer());
        table.getTableHeader().repaint();
    }
    
    /**
     * removeAllColorHeaders
     * @param color 
     */
    public void removeAllColorHeaders(){
        
        for(int i = 0; i < filterItems.size(); i++){
            removeColorHeader(i);
        }
        table.getTableHeader().repaint();
    }

    /**
     * getColor
     * @return 
     */
    public Color getColor() {
        return color;
    }

    /**
     * setColor
     * @param color 
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * getTable
     * @return 
     */
    public JTable getTable() {
        return table;
    }

    /**
     * setTable
     * @param table 
     */
    public void setTable(JTable table) {
        this.table = table;
    }

    /**
     * getSorter
     * @return 
     */
    public TableRowSorter<TableModel> getSorter() {
        return sorter;
    }

    /**
     * setSorter
     * @param sorter 
     */
    public void setSorter(TableRowSorter<TableModel> sorter) {
        this.sorter = sorter;
    }

    /**
     * getDistinctColumnItems
     * @return 
     */
    public Map<Integer, ArrayList<Object>> getFilterItems() {
        return filterItems;
    }

    /**
     * setDistinctColumnItems
     * @param distinctColumnItems 
     */
    public void setFilterItems(Map<Integer, ArrayList<Object>> distinctColumnItems) {
        this.filterItems = distinctColumnItems;
    }
    
    /**
     * loadAllRows
     * gets every row and adds it to the filterItems for the specified column
     * @param columnIndex 
     */
    public void loadAllRows(int columnIndex){
        
        // this is just items to search for
        // we decided to cap long values - notes for example
        int cap = 20; // cap the String length of list options
        Object value; // value of the cell
        
        // for every column
        for(int col = 0; col < table.getColumnCount(); col++){
            
            // clear the array
            //distinctItems.get(col).clear();
            
            ArrayList<Object> disctinctColumnItems = filterItems.get(columnIndex);
            
            // for every row
            for (int row = 0; row < table.getRowCount(); row++){
                
                // get value of cell
                value = getTable().getValueAt(row, col);
                
                // handle null values
                if(value == null)
                    value = "";
                
                // cap the String length of list options
                if(value.toString().length() > cap){
                    value = value.toString().substring(0, cap);
                }
                
                // add the first item to the array for comparison
                if(disctinctColumnItems.isEmpty()){
                    disctinctColumnItems.add(value.toString());
                }
                else{
                    // compare the values
                    if(!disctinctColumnItems.contains(value.toString())){
                        disctinctColumnItems.add(value.toString());
                    }
                }
            }
            
            // sort items
            disctinctColumnItems.sort(null);
        }
    }
    
    /**
     * loadAllRows
     * gets every row and adds it to the filterItems for every column
     */
    public void loadAllRows(){
        
        // for every column
        for(int col = 0; col < table.getColumnCount(); col++){
            loadAllRows(col);
        }
        
        // remove header highlight
        removeAllColorHeaders();
    }

    /**
     * include
     * @param entry
     * @return 
     */
    @Override
    public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
        
        TableModel model = entry.getModel();
        int row = entry.getIdentifier();
        
        // check every column
        for( int col = 0; col < model.getColumnCount(); col++ ) {

            if ( filterItems.get(col).isEmpty() ) 
                continue;
            
                // get filter values
                ArrayList<Object> distinctItems = filterItems.get(col);

                // get value
                Object cellValue = model.getValueAt(row, col);

                // handle null exception
                if(cellValue == null) 
                    cellValue = "";

                // if contains any of the filter items then include
                if(!distinctItems.contains(cellValue.toString())){
                    return true;
                }
                else
                    // search for a match and ignore case
                    for(Object distinctItem : distinctItems){
                        if(cellValue.toString().equalsIgnoreCase(distinctItem.toString())){
                            return true;
                        }
                        // notes only shows the first 20 char so if startsWith is also checked
                        else if(cellValue.toString().startsWith(distinctItem.toString())){
                            return true;
                        }
                    }
                return false; // there was no match
            }
        
        return false; // include the whole column
    }
    
}
