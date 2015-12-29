package com.elle.analyster.database;

import java.awt.Component;
import java.sql.Connection;
import javax.swing.JOptionPane;
import com.elle.analyster.logic.CheckBoxList;
import com.elle.analyster.logic.CheckBoxItem;
import com.elle.analyster.presentation.PopupWindow;
import java.sql.Statement;

/**
 * This class is used to track backups made for database tables.
 * PSUEDO CODE
 * - Access database table and retrieve and store backup table data.
 * 1) Associates backup files with tables backed up.
 * 2) Retrieves an array of the backup tables associated with that table.
 * 3) In order to write and retrieve this data, it would have to be stored
 *    in the database.
 *   3a) A database table would be required.
 *   3b) A connection would be required for this class to access the database.
 * 4) Method to create a new table if one does not exist.
 *    4a) prompt user permission to do so.
 *    4b) an error may occur if cannot create database; handle this.
 *
 * - Backup DB Tables GUI
 * 1) Display backups as checkbox items for deletion.
 *
 *
 *
 * @author Carlos Igreja
 * @since  2015 December 28
 */
public class BackupDBTableOrganizer extends BackupDBTables{
    
    // private variables
    private PopupWindow popupWindow;

    public BackupDBTableOrganizer(Connection connection) {
        super(connection);
    }

    public BackupDBTableOrganizer(Connection connection, Component parentComponent) {
        super(connection, parentComponent);
    }

    public BackupDBTableOrganizer(Statement statement) {
        super(statement);
    }

    public BackupDBTableOrganizer(Statement statement, Component parentComponent) {
        super(statement, parentComponent);
    }

    public BackupDBTableOrganizer(String host, String database, String username, String password) {
        super(host, database, username, password);
    }

    public BackupDBTableOrganizer(String host, String database, String username, String password, Component parentComponent) {
        super(host, database, username, password, parentComponent);
    }
    
    
    
    

}
