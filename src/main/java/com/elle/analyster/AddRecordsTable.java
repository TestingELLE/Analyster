/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster;


import com.elle.analyster.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Louis W.
 */
public class AddRecordsTable {
    
    // attributes
    private String tableName;
    @Autowired
    private Analyster ana; // never used
    private TableService tableService;

    /**
     * update
     * called twice from AddRecords
     * @param table
     * @param a 
     */
    public void update(String table, Analyster a) {
        tableService = new TableService();
        tableService.setAssignmentTable(a.getassignmentTable());
        tableService.setReportTable(a.getReportTable());
        tableName = table; // table = string of combobox selected
        ana = a; 
    }
    
    // this method does not look like a good idea
    public String getDateName() {
        if (tableName.equals("Assignments")) {
            return "dateAssigned"; 
        } else {
            return "analysisDate";
        }
    }
    
    public int getDateColumn() {
        String[] columnNames;
        String dateName = getDateName();
        int i;
        
        if (tableName.equals("Assignments")) {
            columnNames = tableService.getColumnNames(1);
            for (i = 0; i < columnNames.length; i++) {
                if (columnNames[i].equals(dateName))
                    return i;
            }
            return -1; 
        } else {
            columnNames = tableService.getColumnNames(2);
            for (i = 0; i < columnNames.length; i++) {
                if (columnNames[i].equals(dateName))
                    return i;
            }
            return -1;
        }
    }
    
    public int getLastColumn() {
        if (tableName.equals("Assignments")) {
            return tableService.getColumnNames(1).length - 1;    // -1 because array starts from 0
        } else {
            return tableService.getColumnNames(2).length - 1;
        }
    }
    
    public String[] getColumnTitles() {

        if (tableName.equals("Assignments")) {
            return tableService.getColumnNames(1); 
        } else {
            return tableService.getColumnNames(2);
        }
    }
    
    public String[] getEmptyRow() {
        String[] table1 = {"", "", "", "", ""},
                 table2 = {"", "", "", "", "", "", ""};
        if (tableName.equals("Assignments")) {
            return table1; 
        } else {
            return table2;
        }
    }
    
}
