
package com.elle.analyster.logic;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author cigreja
 */
public class JTableCellRenderer extends DefaultTableCellRenderer{
    
    private Map<Integer,ArrayList<Integer>> cells;
    private Color defaultCellColor;

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
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col ){
        
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        
        // check if cell is in the list
        if(!cells.get(col).isEmpty() && cells.get(col).contains(row)){
            label.setBackground(Color.GREEN);
        }
        else{
            label.setBackground(defaultCellColor);
        }
        
        return label;
    }
}
