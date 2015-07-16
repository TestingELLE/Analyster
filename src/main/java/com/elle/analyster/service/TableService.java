/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.service;

import com.elle.analyster.Analyster;
import com.elle.analyster.GUI;
import com.elle.analyster.LoadTables;

import javax.swing.*;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Here are all code related to get information from JTable
 *
 * @author danielabecker
 */
public class TableService {

    JTable assignmentTable;
    JTable reportTable;
    JTable archiveAssignTable;
    JTable viewerTable;

    /**
     * getColumnNames
     * @param number
     * @return 
     */
    public String[] getColumnNames(int number) {
        JTable table = assignmentTable;  // use this method cause table1 and table2 are private
        switch (number) {
            case 1:
                table = assignmentTable;
                break;
            case 2:
                table = reportTable;
                break;
            case 3:
                table = archiveAssignTable;
                break;
            case 4:
                table = viewerTable;
        }

        ArrayList<String> col = new ArrayList<>();
        int i, num = table.getColumnCount();

        for (i = 1; i < num; i++) { // start from 1 to eliminate the ID column
            col.add(table.getColumnName(i));
        }

        String[] columnNames = col.toArray(new String[col.size()]);
        return columnNames;
    }

    /**
     * setAssignmentTable
     * @param table 
     */
    public void setAssignmentTable(JTable table) {
        assignmentTable = table;
    }

    /**
     * setReportTable
     * @param table 
     */
    public void setReportTable(JTable table) {
        reportTable = table;
    }

    /**
     * setArchiveAssignTable
     * @param table 
     */
    public void setArchiveAssignTable(JTable table) {
        archiveAssignTable = table;
    }

    /**
     * setViewerTable
     * @param table 
     */
    public void setViewerTable(JTable table) {
        viewerTable =table;
    }

    /**
     * textToNumOfRecords
     * @param num
     * @return 
     */
    public String textToNumOfRecords(int num) { // Set text to element numofRecords2
        String setTextNumofRecords2 = "Number of records shown: " + num;
        return setTextNumofRecords2;
    }

    /**
     * loadArchiveAssignTable
     */
    private void loadArchiveAssignTable() {
        new LoadTables().loadTable(Analyster.getInstance().getArchiveAssignTable());

    }

    /**
     * loadAssignmentTable
     */
    private void loadAssignmentTable() {
        new LoadTables().loadTable(Analyster.getInstance().getassignmentTable());
    }

    /**
     * activateRecords
     * This method is probably for the activateRecords menu item
     */
    public void activateRecords() {

        int rowSelected = archiveAssignTable.getSelectedRows().length;
        int[] rowsSelected = archiveAssignTable.getSelectedRows();
        // Archive Selected Records in Assignments Archive
        if (rowSelected != -1) {

            for (int i = 0; i < rowSelected; i++) {
                String sqlInsert = "INSERT INTO " + GUI.getDatabase() + "." + assignmentTable.getName() + "(symbol, analyst, priority, dateAssigned,dateDone,notes) VALUES ( ";
                int numRow = rowsSelected[i];
                for (int j = 1; j < archiveAssignTable.getColumnCount() - 1; j++) {
                    if (archiveAssignTable.getValueAt(numRow, j) == null) {
                        sqlInsert += null + ",";
                    } else {
                        sqlInsert += "'" + archiveAssignTable.getValueAt(numRow, j) + "',";
                    }
                }
                if (archiveAssignTable.getValueAt(numRow, archiveAssignTable.getColumnCount() - 1) == null) {
                    sqlInsert += null + ")";
                } else {
                    sqlInsert += "'" + archiveAssignTable.getValueAt(numRow, archiveAssignTable.getColumnCount() - 1) + "')";
                }
                try {
                    GUI.getStmt().executeUpdate(sqlInsert);
//                    ana.getLogwind().sendMessages(sqlInsert);
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println(e.toString());
                }
            }

            archiveAssignTable.setRowSelectionInterval(rowsSelected[0], rowsSelected[0]);
            loadArchiveAssignTable();
            loadAssignmentTable();
            JOptionPane.showMessageDialog(null, rowSelected + " Record(s) Activated!");

        } else {
            JOptionPane.showMessageDialog(null, "Please, select one task!");
        }

    }

    /**
     * archiveRecords
     * This method is probably for the archiveRecords menu item
     */
    public void archiveRecords() {
        JTable table = assignmentTable;

        int rowSelected = table.getSelectedRows().length;
        int[] rowsSelected = table.getSelectedRows();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String today = dateFormat.format(date);

        // Delete Selected Records from Assignments
        if (rowSelected != -1) {
            for (int i = 0; i < rowSelected; i++) {
                String analyst = (String) table.getValueAt(rowsSelected[i], 2);
                Integer selectedTask = (Integer) table.getValueAt(rowsSelected[i], 0); // Add Note to selected taskID
                String sqlDelete = "UPDATE " + GUI.getDatabase() + "." + assignmentTable.getName() + " SET analyst = \"\",\n"
                        + " priority=null,\n"
                        + " dateAssigned= '" + today + "',"
                        + " dateDone=null,\n"
                        + " notes= \'Previous " + analyst + "' " + "where ID=" + selectedTask;
                try {
                    GUI.getStmt().executeUpdate(sqlDelete);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please, select one task!");
        }
        // Archive Selected Records in Assignments Archive
        if (rowSelected != -1) {

            for (int i = 0; i < rowSelected; i++) {
                String sqlInsert = "INSERT INTO " + GUI.getDatabase() + "." + archiveAssignTable.getName() + " (symbol, analyst, priority, dateAssigned,dateDone,notes) VALUES (";
                int numRow = rowsSelected[i];
                for (int j = 1; j < table.getColumnCount() - 1; j++) {
                    if (table.getValueAt(numRow, j) == null) {
                        sqlInsert += null + ",";
                    } else {
                        sqlInsert += "'" + table.getValueAt(numRow, j) + "',";
                    }
                }
                if (table.getValueAt(numRow, table.getColumnCount() - 1) == null) {
                    sqlInsert += null + ")";
                } else {
                    sqlInsert += "'" + table.getValueAt(numRow, table.getColumnCount() - 1) + "')";
                }
                try {
                    GUI.getStmt().executeUpdate(sqlInsert);
//                    logwind.sendMessages(sqlInsert);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            loadAssignmentTable();
            loadArchiveAssignTable();
            table.setRowSelectionInterval(rowsSelected[0], rowsSelected[rowSelected - 1]);
            JOptionPane.showMessageDialog(null, rowSelected + " Record(s) Archived!");

        } else {
            JOptionPane.showMessageDialog(null, "Please, select one task!");
        }
    }

}
