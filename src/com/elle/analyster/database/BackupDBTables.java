
package com.elle.analyster.database;

import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * BackupDBTables
 * This class creates backup tables of the tables in the database.
 * 
 * @author     Carlos Igreja
 * @since      December 21, 2015
 */
public class BackupDBTables {
    
    private String tableName;
    private String backupTableName;
    private Connection connection;
    private Statement statement;
    private Component parentComponent; // used to display message relative to parent component

    /**
     * Creates a BackupDBTables object instance 
     * @param connection connection to the database
     */
    public BackupDBTables(Connection connection){
        this.tableName = null;
        this.backupTableName = null;
        this.parentComponent = null;
        this.connection = connection;
        try {
            this.statement = connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
        }
    }
    
    /**
     * Creates a BackupDBTables object instance 
     * @param connection connection to the database
     * @param parentComponent component other components will appear relative to
     */
    public BackupDBTables(Connection connection, Component parentComponent){
        this.tableName = null;
        this.backupTableName = null;
        this.parentComponent = parentComponent;
        this.connection = connection;
        try {
            this.statement = connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
        }
    }
    
    /**
     * Creates a BackupDBTables instance 
     * @param statement statement object created from the connection to the database
     */
    public BackupDBTables(Statement statement){
        this.tableName = null;
        this.backupTableName = null;
        this.parentComponent = null;
        try {
            this.connection = statement.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
        }
        this.statement = statement;
    }
    
    /**
     * Creates a BackupDBTables instance 
     * @param statement statement object created from the connection to the database
     * @param parentComponent component other components will appear relative to
     */
    public BackupDBTables(Statement statement, Component parentComponent){
        this.tableName = null;
        this.backupTableName = null;
        this.parentComponent = parentComponent;
        try {
            this.connection = statement.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
        }
        this.statement = statement;
    }
    
    /**
     * Creates a BackupDBTables instance and creates a database connection
     * @param host        the website host or localhost ( ex. website.com or localhost)
     * @param database    database to connect to
     * @param username    user name to connect to the database
     * @param password    user password to connect to the database
     */
    public BackupDBTables(String host, String database, String username, String password){
        this.tableName = null;
        this.backupTableName = null;
        this.parentComponent = null;
        this.connection = createConnection(host, database, username, password);
        if(connection != null){
            this.statement = createStatement(connection);
        }
    }
    
    /**
     * Creates a BackupDBTables instance and creates a database connection
     * @param host        the website host or localhost ( ex. website.com or localhost)
     * @param database    database to connect to
     * @param username    user name to connect to the database
     * @param password    user password to connect to the database
     * @param parentComponent displays other components relative to this component
     */
    public BackupDBTables(String host, String database, String username, String password, Component parentComponent){
        this.tableName = null;
        this.backupTableName = null;
        this.parentComponent = parentComponent;
        this.connection = createConnection(host, database, username, password);
        if(connection != null){
            this.statement = createStatement(connection);
        }
    }
    
    /**
     * creates a database connection
     * @param host        the website host or localhost ( ex. website.com or localhost)
     * @param database    database to connect to
     * @param username    user name to connect to the database
     * @param password    user password to connect to the database
     * @return Connection connection to the database
     */
    public Connection createConnection(String host, String database, String username, String password){
        
        try {
            //Accessing driver from the JAR file
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        
        String server = "jdbc:mysql://" + host +":3306/" + database;
        Connection connection = null;
        
        try {
            // get connection
            connection = DriverManager.getConnection(server, username, password);
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
        }
        
        return connection;
    }
    
    /**
     * creates a Statement object from a Connection object
     * @param connection  connection object to create a statement object
     * @return statement  statement object created from connection object
     */
    public Statement createStatement(Connection connection){
        
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
        }
        return statement;
    }
    
    /**
     * Creates a backup table with the same table name and today's 
     * date appended to the end. 
     * @param tableName table name in the database to backup
     * @return boolean true if backup successful and false if error exception
     */
    public boolean backupDBTableWithDate(String tableName) {
        
        this.tableName = tableName; // needs to be set for backup complete message
        
        // create a new backup table name with date
        this.backupTableName = tableName + getTodaysDate();
        
        // execute sql statements
        try {
            
            createTableLike(tableName, backupTableName);
            backupTableData(tableName, backupTableName);
            displayBackupCompleteMessage();
            return true;

        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            
            handleSQLexWithMessageBox(ex);
            return false;
        }
    }
    
    /**
     * Backs up a table in the database
     * @param tableName         the table in the database to backup up (original)
     * @param backupTableName   the name of the new table (the backup table)
     * @return                  boolean returns true if the backup was a success 
     */
    public boolean backupTable(String tableName, String backupTableName){
        
        // these need to be set for the backup complete message
        this.tableName = tableName;
        this.backupTableName = backupTableName;
        
        try {
            createTableLike(tableName, backupTableName);
            backupTableData(tableName, backupTableName);
            displayBackupCompleteMessage();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
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

    /**
     * Gets todays date
     * @return today's date (ex. _2015_12_21)
     * Returns today's date in a format to append to a table name for backup.
     */
    public String getTodaysDate(){
        
        // get today's date
        Calendar calendar = Calendar.getInstance();
        int year =  calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        return "_" + year + "_" + month + "_" + day;
    }
    
    /**
     * Handles sql exceptions with a message box to notify the user
     * @param ex the sql exception that was thrown
     */
    public void handleSQLexWithMessageBox(SQLException ex){
        System.out.println(ex.getMessage());
        
        String message = ex.getMessage();
        
        // if backup database already exists
        if (message.endsWith("already exists")){
            // option dialog box
            message = "Backup database " + backupTableName + " already exists";
            String title = "Backup already exists";
            int optionType = JOptionPane.YES_NO_CANCEL_OPTION;
            int messageType = JOptionPane.QUESTION_MESSAGE;
            Object[] options = {"Overwrite", "Create a new one", "Cancel"};
            int optionSelected = JOptionPane.showOptionDialog(parentComponent, 
                                        message, 
                                        title, 
                                        optionType, 
                                        messageType, 
                                        null, 
                                        options, 
                                        null);
            
            // handle option selected
            switch(optionSelected){
                case 0:
                    overwriteBackupDB();
                    break;
                case 1:
                    backupTableName = getInputTableNameFromUser();
                    backupTable(tableName, backupTableName);
                    break;
                default:
                    break;
            }
        }
        
        // display message to user
        else{
            
            // message dialog box 
            String title = "Error";
            int messageType = JOptionPane.ERROR_MESSAGE;
            JOptionPane.showMessageDialog(parentComponent, message, title, messageType);
        }
    }
    
    /**
     * Drops a table and creates a new one if it already exists
     * Drops the backup table and creates a new backup with the table name
     * and today's date.
     */
    public void overwriteBackupDB(){
        
        try {
            dropTable(backupTableName);
            createTableLike(tableName, backupTableName);
            backupTableData(tableName, backupTableName);
            displayBackupCompleteMessage();
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
        }
    }
    
    /**
     * Gets input for the table name from the user using an input message box
     * @return the input the user entered into the input text box
     */
    public String getInputTableNameFromUser(){
        // input dialog box 
        String message = "Enter the name for the backup";
        return JOptionPane.showInputDialog(parentComponent, message);
    }
    
    /**
     * A message box that displays when 
     * the backup was completed successfully.
     */
    public void displayBackupCompleteMessage(){
        String message = tableName + " was backed up as " + backupTableName;
        JOptionPane.showMessageDialog(parentComponent, message);
    }

    /**
     * This can be used to check that the connection is open and not null
     * @return database connection
     */
    public Connection getConnection() {
        return connection;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getBackupTableName() {
        return backupTableName;
    }

    public void setBackupTableName(String backupTableName) {
        this.backupTableName = backupTableName;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public Component getParentComponent() {
        return parentComponent;
    }

    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }
    
    
}
