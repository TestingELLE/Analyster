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
    private Map<Integer,ArrayList<Object>> distinctColumnItems; // distincted items to filter
    

    /**
     * CONSTRUCTOR
     * TableFilter
     * @param table 
     */
    public TableFilter(JTable table){
        
        this.table = table;
        sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);
        
        distinctColumnItems = new HashMap<>();
        
        for(int i = 0; i < table.getModel().getColumnCount(); i++){
            distinctColumnItems.put(i, new ArrayList<Object>());
        }
    }
    
    /**
     * addDistinctItem
     * @param col
     * @param selectedField 
     */
    public void addDistinctItem(int col, Object selectedField){
        
        if(selectedField == null) 
            selectedField = "";
        
        distinctColumnItems.get(col).add(selectedField);
    }
    
    /**
     * addDistinctItems
     * @param col
     * @param items 
     */
    public void addDistinctItems(int col, ArrayList<Object> items){
        
        for(Object item: items){
            
            if(item == null)
                item = "";
            
            distinctColumnItems.get(col).add(item);
        }
    }
    
    /**
     * removeDistinctItem
     * @param col
     * @param selectedField 
     */
    public void removeDistinctItem(int col, Object selectedField){
        
        if(selectedField == null) 
            selectedField = "";
        
        distinctColumnItems.get(col).remove(selectedField);
    }
    
    /**
     * removeDistinctItems
     * @param col
     * @param items 
     */
    public void removeDistinctItems(int col, ArrayList<Object> items){
        
        for(Object item: items){
            
            if(item == null)
                item = "";
            
            distinctColumnItems.get(col).remove(item);
        }
    }
    
    /**
     * removeAllDistinctItems
     * @param col
     * @param items 
     */
    public void removeAllDistinctItems(){
        
        for(int i = 0; i < distinctColumnItems.size(); i++)
            distinctColumnItems.get(i).clear();
    }
    
    /**
     * applyFilter
     */
    public void applyFilter(){
        
        sorter.setRowFilter(this);
        addColorHeaders(Color.GREEN);
    }
    
    /**
     * addColorHeader
     * @param columnIndex
     * @param color 
     */
    public void addColorHeader(int columnIndex, Color color){
        
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setBackground(color);
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(columnIndex)
                .setHeaderRenderer(cellRenderer);
        table.getTableHeader().repaint();
    }
    
    /**
     * addColorHeaders
     * @param color 
     */
    public void addColorHeaders(Color color){
        
        for(int i = 0; i < distinctColumnItems.size(); i++){
            if(distinctColumnItems.get(i).isEmpty()){
                removeColorHeader(i);
            }
            else{
                addColorHeader(i, color);
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
     * removeColorHeaders
     * @param color 
     */
    public void removeAllColorHeaders(){
        
        for(int i = 0; i < distinctColumnItems.size(); i++){
            removeColorHeader(i);
        }
        table.getTableHeader().repaint();
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

            if ( distinctColumnItems.get(col).isEmpty() ) 
                continue; // no filtering for this column
            
            // get filter values
            ArrayList<Object> distinctItems = distinctColumnItems.get(col);

            // get value
            Object cellValue = model.getValueAt(row, col);

            // handle null exception
            if(cellValue == null) 
                cellValue = "";

            // search for a match and ignore case
            for(Object distinctItem : distinctItems){
                if(!cellValue.toString().equalsIgnoreCase(distinctItem.toString())){
                    return false;
                }
            }
            
        }

        return true;
    }
    
}
