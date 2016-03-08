
package com.elle.analyster.dao;

import com.elle.analyster.database.SQL_Commands;
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
    private final String BACKUP_DB_TABLE_NAME = "Table_Backups";
    private final String BACKUP_DB_TABLE_COLUMN_PK = "id";
    private final String BACKUP_DB_TABLE_COLUMN_1 = "tableName";
    private final String BACKUP_DB_TABLE_COLUMN_2 = "backupTableName";
    
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
    
    public BackupDBTableDAO(Connection connection){
        this.sql_commands = new SQL_Commands(connection);
        this.connection = connection;
        try {
            this.statement = connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTableDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }
    
    public void createDBTableToStoreBackupsInfo(){
        
        String createTableQuery = 
                "CREATE TABLE " + BACKUP_DB_TABLE_NAME +
                "(" +
                BACKUP_DB_TABLE_COLUMN_PK + " int(4) PRIMARY KEY AUTO_INCREMENT, " +
                BACKUP_DB_TABLE_COLUMN_1 + " VARCHAR(50) NOT NULL, " +
                BACKUP_DB_TABLE_COLUMN_2 + " VARCHAR(50) NOT NULL " +
                ");";

        if(!sql_commands.updateQuery(createTableQuery))
            JOptionPane.showMessageDialog(parentComponent, "unable to create table " + BACKUP_DB_TABLE_NAME );
    }
    
    public ArrayList<CheckBoxItem> getCheckBoxItemsFromDB(String tableName) {
        
        // check box items array list to return
        ArrayList<CheckBoxItem> items = new ArrayList<>();
        
        // sql query to return a result set
        String sql = 
                "SELECT " + BACKUP_DB_TABLE_COLUMN_PK + "," + BACKUP_DB_TABLE_COLUMN_2 +
               " FROM " + BACKUP_DB_TABLE_NAME +
               " WHERE " + BACKUP_DB_TABLE_COLUMN_1 + " = '" + tableName + "' ;";
        
        ResultSet result = null;
        
        try {
            //Here we create our query
            PreparedStatement statement = getConnection().prepareStatement(sql);

            //Creating a variable to execute query
            result = statement.executeQuery();

        } catch (SQLException ex) {
            LoggingAspect.afterThrown(ex);
            // if table doesn't exist and needs to be created
            if(ex.getMessage().endsWith("exist")){
                createDBTableToStoreBackupsInfo();
                result = sql_commands.executeQuery(sql);
            }
        } finally{
            // create checkbox items from result set and load up array list
            if(result != null){
                try {
                    while(result.next())
                    {
                        // get column data
                        int id = Integer.parseInt(result.getString(1));
                        String backupName = result.getString(2);
                        
                        // create checkBoxItem
                        CheckBoxItem item = new CheckBoxItem(backupName);
                        
                        // set checkbox item id (same id as primary key on db table)
                        item.setId(id);
                        
                        // add checkbox item to the array list
                        items.add(item);
                    }
                } catch (SQLException ex) {
                    LoggingAspect.afterThrown(ex);
                }
            }
        }
        
        return items;
    }
    
    /**
     * Deletes record of the backup table (not the actual backup table)
     * @param id id of record in database table
     * @return boolean true if successful and false if sql error occurred 
     */
    public boolean deleteItem(int id) {
        
        if(id == -1)
            return false;
        
        String sql = 
                "DELETE FROM " + BACKUP_DB_TABLE_NAME +
               " WHERE " + BACKUP_DB_TABLE_COLUMN_PK + " = " + id + ";";
        
        try {
            getStatement().executeUpdate(sql);
            return true;
        } catch (SQLException ex) {
            LoggingAspect.afterThrown(ex);
            return false;
        }
    }
    
    public boolean addBackupRecord(String tableName, String backupTableName){
        String sql = 
                "INSERT INTO " + BACKUP_DB_TABLE_NAME + 
               " ( " + BACKUP_DB_TABLE_COLUMN_1 + ", " + BACKUP_DB_TABLE_COLUMN_2 + ")" 
                + " VALUES ('" + tableName + "', '" +  backupTableName + "');";
        try {
            getStatement().executeUpdate(sql);
            return true;
        } catch (SQLException ex) {
            LoggingAspect.afterThrown(ex);
            return false;
        }
    }
    
    public boolean dropBackupRecord(String tableName, String backupTableName) {
        String sql = 
                "DELETE FROM " + BACKUP_DB_TABLE_NAME + 
               " WHERE " + BACKUP_DB_TABLE_COLUMN_1 + " = '" + tableName +
               "' AND " + BACKUP_DB_TABLE_COLUMN_2 + " = '" + backupTableName + "' ;";
        try {
            getStatement().executeUpdate(sql);
            return true;
        } catch (SQLException ex) {
            LoggingAspect.afterThrown(ex);
            return false;
        }
    }
    
    /**
     * Creates a table in the database
     * @param tableName           the original table name
     * @param backupTableName     the name of the backup table
     * @throws SQLException       can use handleSQLexWithMessageBox method in catch
     */
    public void createTableLike(String tableName, String backupTableName) throws SQLException{
        
        // sql query to create the table 
        String sqlCreateTable = "CREATE TABLE " + backupTableName
                             + " LIKE " + tableName + " ; ";
        
        // execute sql statements
        statement.executeUpdate(sqlCreateTable);
    }
    
    /**
     * Backs up table data in the database
     * @param tableName           the original table name
     * @param backupTableName     the name of the backup table
     * @throws SQLException       can use handleSQLexWithMessageBox method in catch
     */
    public void backupTableData(String tableName, String backupTableName) throws SQLException{
        
        // sql query to backup the table data
        String sqlBackupData =  "INSERT INTO " + backupTableName 
                             + " SELECT * FROM " + tableName +  " ;";
        
        // execute sql statements
        statement.executeUpdate(sqlBackupData);
    }
    
    /**
     * Drops a table in the database
     * @param tableName drop this table name from database
     * @return boolean dropped from database? true or false
     * @throws SQLException can use handleSQLexWithMessageBox method in catch
     */
    public void dropTable(String tableName) throws SQLException{
        
        // sql query to drop the table 
        String sqlCreateTable = "DROP TABLE " + tableName + " ; ";
        
        // execute sql statements
        statement.executeUpdate(sqlCreateTable);
    }
}
