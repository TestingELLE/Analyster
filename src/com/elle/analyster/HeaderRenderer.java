package com.elle.analyster;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class HeaderRenderer implements TableCellRenderer {

    TableCellRenderer renderer;

    public HeaderRenderer(JTable table) {
        renderer = table.getTableHeader().getDefaultRenderer();
    }

    @Override
    public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int col) {
        return renderer.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, col);
    }
}
