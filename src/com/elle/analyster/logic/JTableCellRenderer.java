
package com.elle.analyster.logic;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author cigreja
 */
public class JTableCellRenderer extends DefaultTableCellRenderer{
    
    private Map<Integer,ArrayList<Integer>> cells;  // cells to color
    private String[][] data;                        // original model data
    private Color defaultCellColor;
    private Color selectedCellColor;

    /**
     * CONSTRUCTOR 
     * @param table // the table this is for
     */
    public JTableCellRenderer(JTable table) {
        
        // initialize the Map of cells
        cells = new HashMap<>();
        for(int col = 0; col < table.getColumnCount(); col++){
            cells.put(col, new ArrayList<>());
        }
        
        // initialize the default cell color
        defaultCellColor = table.getBackground();
        selectedCellColor = table.getSelectionBackground();
        
        // initialize data for the table model
        data = new String[table.getModel().getRowCount()][table.getColumnCount()];
        for(int row = 0; row < table.getModel().getRowCount(); row++){
            for(int col = 0; col < table.getColumnCount(); col++){
                Object value = table.getModel().getValueAt(row, col);
                if(value == null)
                    value ="";
                data[row][col]  = value.toString();
            }
        }
        
    }

    public Map<Integer, ArrayList<Integer>> getCells() {
        return cells;
    }

    public void setCells(Map<Integer, ArrayList<Integer>> cells) {
        this.cells = cells;
    }

    public Object[][] getData() {
        return data;
    }

    public void setData(String[][] data) {
        this.data = data;
    }
    
    
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col ){
        
        Component component =  super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        
        // check if cell is in the list
        if(!cells.get(col).isEmpty() && cells.get(col).contains(row)){
            component.setBackground(Color.GREEN);
        }
        else{
            if(isSelected){
                component.setBackground(selectedCellColor);
            }
            else{
                component.setBackground(defaultCellColor);
            }
        }
        
        return component;
    }
}
