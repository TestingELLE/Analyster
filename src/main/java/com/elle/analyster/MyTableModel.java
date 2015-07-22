/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 */
public class MyTableModel extends DefaultTableModel {

    boolean readOnly;
    List<Object> dataChanged = new ArrayList<>();

    public MyTableModel() {

    }

    // NO USAGES
//    public MyTableModel(Object[][] rowData, Object[] columnNames, boolean filteringStatus) {
//        super(rowData, columnNames);
//        readOnly = filteringStatus;
//    }

    public MyTableModel(Vector data, Vector columnNames, boolean filteringStatus) {
        super(data, columnNames);
        readOnly = filteringStatus;
    }

    public MyTableModel(boolean filteringStatus) {
        readOnly = filteringStatus;
    }

    @Override
    public Class getColumnClass(int col) {
        if (col == 0) {
            return Integer.class;
        } else {
            return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {// This method change field table from edit to read only or viceversa.
        if (col == 0) {
            return false;
        } else if (readOnly == true) {
            return false;
        } else {
            return true;
        }
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }



    public List<Object> getDataChanged() {
        return dataChanged;
    }


}
