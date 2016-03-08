
package com.elle.analyster.dao;

import com.elle.analyster.database.SQL_Commands;
import com.elle.analyster.dbtables.BackupDBTableRecord;
import com.elle.analyster.logic.CheckBoxItem;
import com.elle.analyster.logic.LoggingAspect;
import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * BackupDBTableDAO
 * @author Carlos Igreja
 * @since  Mar 7, 2016
 */
public class BackupDBTableDAO {

    // database table information
    private final String DB_TABLE_NAME = "Table_Backups";
    private final String COL_PK_ID = "id";
    private final String COL_APPLICATION = "applicationName";
    private final String COL_TABLE_NAME = "tableName";
    private final String COL_BACKUP_NAME = "backupTableName";
    private final String APPLICATION_NAME = "Analyster";
    
    // components
    private SQL_Commands sql_commands;
    private Component parentComponent;
    private Connection connection;
    private Statement statement;
    
    public BackupDBTableDAO(Connection connection, Component parentComponent){
        this.sql_commands = new SQL_Commands(connection);
        this.connection = connection;
        try {
            this.statement = connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTableDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.parentComponent = parentComponent;
    }
    
    public void createDBTableToStoreBackupsInfo(){
        
        String createTableQuery = 
                "CREATE TABLE " + DB_TABLE_NAME +
                "(" +
                        COL_PK_ID + " int(4) PRIMARY KEY AUTO_INCREMENT, " +
                        COL_APPLICATION + " VARCHAR(50) NOT NULL, " +
                        COL_TABLE_NAME + " VARCHAR(50) NOT NULL, " +
                        COL_BACKUP_NAME + " VARCHAR(50) NOT NULL " +
                ");";

        if(!sql_commands.updateQuery(createTableQuery))
            JOptionPane.showMessageDialog(parentComponent, "unable to create table " + DB_TABLE_NAME);
    }

    public ArrayList<BackupDBTableRecord> getRecords() {

        ArrayList<BackupDBTableRecord> records = new ArrayList<>();

        // sql query to return a result set
        String sql = "SELECT * " +
                     " FROM " + DB_TABLE_NAME +
                     " WHERE " + COL_APPLICATION + " = '" + APPLICATION_NAME + "' ;";

        ResultSet result = null;

        try {
            //Here we create our query
            PreparedStatement statement = connection.prepareStatement(sql);

            //Creating a variable to execute query
            result = statement.executeQuery();

        }
        catch (SQLException ex) {
            LoggingAspect.afterThrown(ex);
            // if table doesn't exist and needs to be created
            if(ex.getMessage().endsWith("exist")){
                createDBTableToStoreBackupsInfo();
                result = sql_commands.executeQuery(sql);
            }
        }
        finally{
            // create checkbox items from result set and load up array list
            if(result != null){
                try {
                    while(result.next())
                    {
                        BackupDBTableRecord record = new BackupDBTableRecord();
                        record.setId(result.getInt(1));
                        record.setApplicationName(result.getString(2));
                        record.setTableName(result.getString(3));
                        record.setBackupTableName(result.getString(4));
                        records.add(record);
                    }
                } catch (SQLException ex) {
                    LoggingAspect.afterThrown(ex);
                }
            }
        }

        return records;
    }

    /**
     * Deletes record of the backup table (not the actual backup table)
     * @param record database table record
     * @return boolean true if successful and false if sql error occurred 
     */
    public boolean deleteRecord(BackupDBTableRecord record) {

        String sql = "";
        int id = record.getId();
        String backupTableName = record.getBackupTableName();

        try {
            // drop table
            sql = "DROP TABLE " + backupTableName + " ; ";
            statement.executeUpdate(sql);

            // delete record
            sql = "DELETE FROM " + DB_TABLE_NAME +
                    " WHERE " + COL_PK_ID + " = " + id + ";";
            statement.executeUpdate(sql);

            LoggingAspect.afterReturn("Deleted backup " + backupTableName);
            return true;
        } catch (SQLException e) {
            LoggingAspect.afterThrown(e);
            return false;
        }
    }

    /**
     * Deletes record of the backup table (not the actual backup table)
     * @param backupTableName name of backup table to delete
     * @return boolean true if successful and false if sql error occurred
     */
    public boolean deleteRecord(String backupTableName) {

        String sql = "";

        try {
            // drop table
            sql = "DROP TABLE " + backupTableName + " ; ";
            statement.executeUpdate(sql);

            // delete record
            sql = "DELETE FROM " + DB_TABLE_NAME +
                    " WHERE " + COL_BACKUP_NAME + " = '" + backupTableName + "';";
            statement.executeUpdate(sql);

            LoggingAspect.afterReturn("Deleted backup " + backupTableName);
            return true;
        } catch (SQLException e) {
            LoggingAspect.afterThrown(e);
            return false;
        }
    }
    
    public boolean addRecord(BackupDBTableRecord record){

        String sql = "";
        String tableName = record.getTableName();
        String backupTableName = record.getBackupTableName();

        try {

            // create the backup table
            sql = "CREATE TABLE " + backupTableName
                    + " LIKE " + tableName + " ; ";
            statement.executeUpdate(sql);

            // backup the table data
            sql =  "INSERT INTO " + backupTableName
                    + " SELECT * FROM " + tableName +  " ;";
            statement.executeUpdate(sql);

            // add record
            sql = "INSERT INTO " + DB_TABLE_NAME + " ( " + COL_APPLICATION + ", " + COL_TABLE_NAME + ", " + COL_BACKUP_NAME + ")"
                    + " VALUES ('" + APPLICATION_NAME + "', '" +  tableName + "', '" +  backupTableName + "');";
            statement.executeUpdate(sql);

            LoggingAspect.afterReturn("Created backup " + backupTableName);
            return true;
        } catch (SQLException ex) {
            LoggingAspect.afterThrown(ex);
            return false;
        }
    }
}
