/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.service;

import com.elle.analyster.Analyster;
import com.elle.analyster.GUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * @author Daniela.
 */
//Delete from DataBase record selected
public class DeleteRecord {
    private Logger log = LoggerFactory.getLogger(DeleteRecord.class);
    String sqlDelete =null;

    public String deleteRecordSelected( JTable table) throws HeadlessException {
        String tableName = table.getName();
        int rowsSelected = table.getSelectedRows().length;
        if (rowsSelected != -1) {
            for (int i = 0; i < rowsSelected; i++) {
                int row = table.getSelectedRows()[i];
                Integer selectedTask = (Integer) table.getValueAt(row, 0); // Add Note to selected taskID
                sqlDelete = "DELETE FROM " + GUI.getDatabase() + "." + tableName + " where ID=" + selectedTask;
                try {
                    GUI.getStmt().executeUpdate(sqlDelete);
                } catch (SQLException e) {
                    log.info(e.getMessage());
                }
            }
            
            // this is where the table is refreshing 
            Analyster.getInstance().loadTable(Analyster.getInstance().getTabs().get(tableName).getTable());

            // output pop up dialog that a record was deleted 
            JOptionPane.showMessageDialog(Analyster.getInstance(), rowsSelected + " Record(s) Deleted");
        }
        return sqlDelete;
    }
    
}
