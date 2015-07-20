package com.elle.analyster.service;

import com.elle.analyster.Analyster;
import com.elle.analyster.GUI;
import com.elle.analyster.LoginWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA. User: danielabecker Date: 5/28/15 Time: 2:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class Connection {

    private static Logger log = LoggerFactory.getLogger(Connection.class);

    public static String connection(String sql, JTable table) throws SQLException {

        Analyster ana = Analyster.getInstance();
        Vector data = new Vector();
        Vector columnNames = new Vector();
        int columns;

        if (GUI.status == false) {
            JOptionPane.showMessageDialog(null, "You have not yet logged in.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            new LoginWindow(ana).setVisible(true);
        }

        ResultSet rs = null;
        ResultSetMetaData metaData = null;
        try {
            rs = GUI.getStmt().executeQuery(sql);
            metaData = rs.getMetaData();
        } catch (Exception ex) {
            log.error("Error: ", ex);
        }
        try {
            columns = metaData.getColumnCount();
            for (int i = 1; i <= columns; i++) {
                columnNames.addElement(metaData.getColumnName(i));
            }
            while (rs.next()) {
                Vector row = new Vector(columns);
                for (int i = 1; i <= columns; i++) {
                    row.addElement(rs.getObject(i));
                }
                data.addElement(row);
            }
            rs.close();

        } catch (SQLException ex) {
            log.error("Error: ", ex);
            throw ex;
        }

        ana.tableReload(table, data, columnNames);  // Table model (table visualization) set up
        log.info("Table added succesfully");

        return null;
    }


}
