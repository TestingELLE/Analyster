package com.elle.analyster.logic;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

/**
 * EditableTableModel This class allows switching the table to editable and non
 * editable by overriding the isCellEditable method with a boolean to change it
 * on the fly.
 *
 * @author Carlos Igreja
 * @since June 10, 2015
 * @version 0.6.3
 */
public class EditableTableModel extends DefaultTableModel {

    private boolean cellEditable;
    private Vector columnClass;

    /**
     * CONSTRUCTOR EditableTableModel
     *
     * @param data
     * @param columnNames
     * @param isCellEditable
     */
    public EditableTableModel(Vector data, Vector columnNames, Vector colClass) {
        super(data, columnNames);
        cellEditable = false;
        columnClass = colClass;
    }

    /**
     * isCellEditable Makes table editable or non editable
     *
     * @param row
     * @param col
     * @return
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        return cellEditable;
    }

    /**
     * isCellEditable
     *
     * @return
     */
    public boolean isCellEditable() {
        return cellEditable;
    }

    /**
     * setCellEditable
     *
     * @param cellEditable
     */
    public void setCellEditable(boolean cellEditable) {
        this.cellEditable = cellEditable;
            
    }

    /**
     * Override getColumnClass() in DefaultTableModel
     *
     * @param col
     * @return class
     */
    @Override
    public Class getColumnClass(int col) {
        if (col == 0) {

            return Integer.class;
        }else{
            return Object.class;
        }

    }

}
