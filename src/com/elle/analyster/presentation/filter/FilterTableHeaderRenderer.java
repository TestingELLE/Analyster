package com.elle.analyster.presentation.filter;


import com.elle.analyster.TableHeaderRenderer;

import javax.swing.*;
import java.awt.*;

/**
 * Table header renderer to show the column filter state 
 * 
 * Created on Feb 10, 2011
 * @author Eugene Ryzhikov
 *
 */
class FilterTableHeaderRenderer extends TableHeaderRenderer {

    private static final long serialVersionUID = 1L;

    private ImageIcon icon;
    private final int filterIconPlacement;
    private final JTableFilter tableFilter;

    
    public FilterTableHeaderRenderer(JTableFilter tableFilter,
            int filterIconPlacement) {
        this.tableFilter = tableFilter;
        this.filterIconPlacement = filterIconPlacement;

        if (this.filterIconPlacement != SwingConstants.LEADING &&
                this.filterIconPlacement != SwingConstants.TRAILING) {
            throw new UnsupportedOperationException("The filter icon " +
                    "placement can only take the values of " +
                    "SwingConstants.LEADING or SwingConstants.TRAILING");
        }
    }
    


    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        
        final JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        int modelColumn =  table.convertColumnIndexToModel(column);
        if (tableFilter.isFiltered(modelColumn)) {
        }
        
        return label;
    }

}