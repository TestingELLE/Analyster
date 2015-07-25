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
        
        // initialize disctinctColumnItems
        distinctColumnItems = new HashMap<>(); 
        for(int i = 0; i < table.getModel().getColumnCount(); i++){
            distinctColumnItems.put(i, new ArrayList<Object>());
        }
        
        // initialize the color for the table header when it is filtering
        color = Color.GREEN; // default color is green
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
     * removeDistinctItems
     * @param col
     */
    public void removeDistinctItems(int col){

        distinctColumnItems.get(col).clear();
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
        
        for(int i = 0; i < distinctColumnItems.size(); i++){
            if(distinctColumnItems.get(i).isEmpty()){
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
        
        for(int i = 0; i < distinctColumnItems.size(); i++){
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
    public Map<Integer, ArrayList<Object>> getDistinctColumnItems() {
        return distinctColumnItems;
    }

    /**
     * setDistinctColumnItems
     * @param distinctColumnItems 
     */
    public void setDistinctColumnItems(Map<Integer, ArrayList<Object>> distinctColumnItems) {
        this.distinctColumnItems = distinctColumnItems;
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
