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
    
    private String tableName;
    @Autowired
    private Analyster ana;
    private TableService tableService;

    public void update(String table, Analyster a) {
        tableService = new TableService();
        tableService.setAssignmentTable(a.getassignmentTable());
        tableService.setReportTable(a.getReportTable());
        tableName = table;
        ana = a; 
    }
    
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
