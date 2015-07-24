package com.elle.analyster.presentation.filter;

import java.awt.Component;
import java.lang.reflect.Method;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * CLASS TableAwareCheckListRenderer
 * @author cigreja
 * This class has one method and extends CheckListRenderer
 * Maybe it can just be added to the CheckListRenderer?
 * I tried to merge it but it didn't work,
 * It can be tricky because this method is overriding the method in CheckListRenderer
 */
public class TableAwareCheckListRenderer extends CheckListRenderer {
    
    // attributes
    private final JTable table;
    private final int column;

    /**
     * CONSTRUCTOR
     * * This class instance is created once in TableFilterColumnPopup
     * @param table
     * @param column 
     */
    public TableAwareCheckListRenderer( JTable table, int column ) {
        this.table = table;
        this.column = column;
    }

    /**
     * getListCellRendererComponent
     * @param list
     * @param value
     * @param index
     * @param isSelected
     * @param cellHasFocus
     * @return 
     * 
     * This method calls itself once to override the method.
     * No other usages found
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        // try to retrieve the text from table's cell renderer
        if (value instanceof DistinctColumnItem) {

            DistinctColumnItem item = (DistinctColumnItem) value;
            TableCellRenderer renderer = table.getCellRenderer( item.getRow(), column);
            
            try {
                
                Component cmpt = renderer.getTableCellRendererComponent(
                        table, item.getValue(), isSelected, hasFocus(), item.getRow(), column );

                Method method = cmpt.getClass().getMethod("getText");
                Object s = method.invoke(cmpt);
                
                if ( s instanceof String ) {
                    setText( (String)s );
                }
                
            } catch (Throwable e) {
//                e.printStackTrace();
            }
            
        }
        return this;
        
    }
    
}