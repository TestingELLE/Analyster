
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
 * @Author     Carlos Igreja
 * @CreatedOn  December 21, 2015
 */
public class BackupDBTables {
    
    private String tableName;
    private String backupTableName;
    private Connection connection;
    private Statement statement;
    private Component parentComponent; // used to display message relative to parent component

    /**
     * CONSTRUCTOR
     * BackupDBTables
     * @param connection 
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
     * CONSTRUCTOR
     * BackupDBTables
     * @param connection
     * @param parentComponent 
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
     * CONSTRUCTOR
     * BackupDBTables
     * @param statement 
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
     * CONSTRUCTOR
     * BackupDBTables
     * @param statement
     * @param parentComponent 
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
     * CONSTRUCTOR
     * BackupDBTables
     * creates a database connection
     * @param host        the website host or localhost ( ex. website.com or localhost)
     * @param database    database to connect to
     * @param username    user name to connect to the database
     * @param password    user password to connect to the database
     */
    public BackupDBTables(String host, String database, String username, String password){
        this.tableName = null;
        this.backupTableName = null;
        this.parentComponent = null;
        Connection connection = createConnection(host, database, username, password);
        if(connection != null){
            this.statement = createStatement(connection);
        }
    }
    
    /**
     * CONSTRUCTOR
     * BackupDBTables
     * creates a database connection
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
        Connection connection = createConnection(host, database, username, password);
        if(connection != null){
            this.statement = createStatement(connection);
        }
    }
    
    /**
     * createConnection
     * creates a database connection
     * @param host        the website host or localhost ( ex. website.com or localhost)
     * @param database    database to connect to
     * @param username    user name to connect to the database
     * @param password    user password to connect to the database
     * @return Connection connection to the database
     */
    private Connection createConnection(String host, String database, String username, String password){
        
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
     * createStatement
     * creates a Statement object from a Connection object
     * @param connection  connection object to create a statement object
     * @return statement  statement object created from connection object
     */
    private Statement createStatement(Connection connection){
        
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
     * backupDBTableWithDate
     * @param tableName table name in the database to backup
     * This method creates a backup table with the same table name and today's 
     * date appended to the end. 
     */
    public void backupDBTableWithDate(String tableName) {
        
        this.tableName = tableName; // needs to be set for backup complete message
        
        // create a new backup table name with date
        this.backupTableName = tableName + getTodaysDate();
        
        // execute sql statements
        try {
            
            createTable(tableName, backupTableName);
            backupTableData(tableName, backupTableName);
            displayBackupCompleteMessage();

        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            
            handleSQLexWithMessageBox(ex);
        }
    }
    
    /**
     * backupTable
     * @param tableName         the table in the database to backup up (original)
     * @param backupTableName   the name of the new table (the backup table)
     * @return                  boolean returns true if the backup was a success 
     */
    public boolean backupTable(String tableName, String backupTableName){
        
        // these need to be set for the backup complete message
        this.tableName = tableName;
        this.backupTableName = backupTableName;
        
        try {
            createTable(tableName, backupTableName);
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
     * createTable
     * @param tableName           the original table name
     * @param backupTableName     the name of the backup table
     * @throws SQLException       can use handleSQLexWithMessageBox method in catch
     */
    private void createTable(String tableName, String backupTableName) throws SQLException{
        
        // sql query to create the table 
        String sqlCreateTable = "CREATE TABLE " + backupTableName
                             + " LIKE " + tableName + " ; ";
        
        // execute sql statements
        statement.executeUpdate(sqlCreateTable);
    }
    
    /**
     * backupTableData
     * @param tableName           the original table name
     * @param backupTableName     the name of the backup table
     * @throws SQLException       can use handleSQLexWithMessageBox method in catch
     */
    private void backupTableData(String tableName, String backupTableName) throws SQLException{
        
        // sql query to backup the table data
        String sqlBackupData =  "INSERT INTO " + backupTableName 
                             + " SELECT * FROM " + tableName +  " ;";
        
        // execute sql statements
        statement.executeUpdate(sqlBackupData);
    }
    
    /**
     * dropTable
     * @param tableName drop this table name from database
     * @return boolean dropped from database? true or false
     * @throws SQLException can use handleSQLexWithMessageBox method in catch
     */
    private void dropTable(String tableName) throws SQLException{
        
        // sql query to drop the table 
        String sqlCreateTable = "DROP TABLE " + tableName + " ; ";
        
        // execute sql statements
        statement.executeUpdate(sqlCreateTable);
    }

    /**
     * getTodaysDate
     * @return today's date (ex. _2015_12_21)
     * Returns today's date in a format to append to a table name for backup.
     */
    private String getTodaysDate(){
        
        // get today's date
        Calendar calendar = Calendar.getInstance();
        int year =  calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        return "_" + year + "_" + month + "_" + day;
    }
    
    /**
     * handleSQLexWithMessageBox
     * @param ex the sql exception that was thrown
     */
    private void handleSQLexWithMessageBox(SQLException ex){
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
     * overwriteBackupDB
     * Drops the backup table and creates a new backup with the table name
     * and today's date.
     */
    private void overwriteBackupDB(){
        
        try {
            dropTable(backupTableName);
            createTable(tableName, backupTableName);
            backupTableData(tableName, backupTableName);
            displayBackupCompleteMessage();
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
        }
    }
    
    /**
     * getInputTableNameFromUser
     * @return the input the user entered into the input text box
     * This uses an input dialog box to get the name that will be used for the 
     * table from the user with an input text box.
     */
    private String getInputTableNameFromUser(){
        // input dialog box 
        String message = "Enter the name for the backup";
        return JOptionPane.showInputDialog(parentComponent, message);
    }
    
    /**
     * displayBackupCompleteMessage
     * This is a message box that displays when 
     * the backup was completed successfully.
     */
    private void displayBackupCompleteMessage(){
        String message = tableName + " was backed up as " + backupTableName;
        JOptionPane.showMessageDialog(parentComponent, message);
    }
    
    /**
     * getConnection
     * This can be used to check that the connection is open and not null
     * @return Connection database connection
     */
    public Connection getConnection() {
        return connection;
    }
}
