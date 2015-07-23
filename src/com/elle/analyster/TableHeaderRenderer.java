package com.elle.analyster;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Common class for customizing table header renderer without loosing its L&F
 *
 * Created on Aug 10, 2010
 * @author Eugene Ryzhikov
 *
 */
public class TableHeaderRenderer extends JComponent implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        // returns component used for default header rendering
        // makes it independent on current L&F

        return table.getTableHeader().getDefaultRenderer().
                  getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

    }

    // following methods are overriden for performance reasons

    @Override
    public void validate() {}

    @Override
    public void revalidate() {}

    @Override
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}

    @Override
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {}

}