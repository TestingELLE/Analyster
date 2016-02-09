package com.elle.analyster.presentation;

import com.elle.analyster.database.BackupDBTables;
import com.elle.analyster.database.DBConnection;
import com.elle.analyster.database.SQL_Commands;
import com.elle.analyster.logic.CheckBoxItem;
import com.elle.analyster.logic.CheckBoxList;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Carlos
 */
public class BackupDBTablesWindow extends javax.swing.JFrame {

    // backup database table information
    // this information should match the database table
    private final String BACKUP_DB_TABLE_NAME = "Table_Backups";
    private final String BACKUP_DB_TABLE_COLUMN_PK = "id";
    private final String BACKUP_DB_TABLE_COLUMN_1 = "tableName";
    private final String BACKUP_DB_TABLE_COLUMN_2 = "backupTableName";
    private final String CHECK_ALL_ITEM_TEXT = "(All)";
    
    // private variables
    private String tableName;
    private String backupTableName;
    private Connection connection;
    private Statement statement;
    private Component parentComponent; // used to display message relative to parent component
    private CheckBoxList checkBoxList;
    private PopupWindow popupWindow;
    private ArrayList<CheckBoxItem> checkBoxItems;
    private SQL_Commands sql_commands;
    
    /**
     * Creates new form BackupDBTablesWindow
     */
    public BackupDBTablesWindow() {
        initComponents();
        
        // this class is being refactord
        // it could use a controller
        // but for now these variables must be set by a static method
        // and they must be set before this constructor is called.
        this.connection = DBConnection.getConnection();  // testing
        this.tableName = "Assignments_Archived";  // testing
        this.parentComponent = null;  // testing
        
        this.backupTableName = null;
        this.sql_commands = new SQL_Commands(connection);
        try {
            this.statement = connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
        }
        
        String title = "Backup " + tableName; // window title
        String message = "Existing Backup Database tables";
        
        setCheckBoxListListener();
        
        // checkbox item array
        checkBoxItems = new ArrayList<>();
        checkBoxItems.add(new CheckBoxItem(CHECK_ALL_ITEM_TEXT));
        checkBoxItems.addAll(getCheckBoxItemsFromDB());
        
        // if checkBoxItems only contains one item (check all) then remove it
        if(checkBoxItems.size() == 1)
            checkBoxItems.clear();
        
        // add CheckBoxItems to CheckBoxList
        checkBoxList.setListData(checkBoxItems.toArray());
        
        // add CheckBoxList to the panel
        ScrollPane scroll = new ScrollPane();
        scroll.add(checkBoxList);
        scroll.setPreferredSize(panelOutput.getPreferredSize());
        panelOutput.setLayout(new BorderLayout());
        panelOutput.add(scroll, BorderLayout.CENTER);
        
        // start the delete button as not enabled
        btnDelete.setEnabled(false);
        
        // show window
        setLocationRelativeTo(parentComponent);
        setVisible(true);
        
    }
    
    public void setCheckBoxListListener(){
        
        // create the checkbox JList 
        checkBoxList = new CheckBoxList(); // JList
        
        // add mouseListener to the list
        checkBoxList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e)
            {
                // get the checkbox item index
               int index = checkBoxList.locationToIndex(e.getPoint());

               // index cannot be null
               if (index != -1) {
                   
                   // get the check box item at this index
                  JCheckBox checkbox = (JCheckBox)
                              checkBoxList.getModel().getElementAt(index);
                  
                  // check if the (All) selection was checked
                  if(checkbox.getText().equals(CHECK_ALL_ITEM_TEXT)){
                      if(checkbox.isSelected()){
                          removeAllChecks();
                      }
                      else{
                          checkAllItems();
                      }
                  }
                  else{
                      // toogle the check for the checkbox item
                      checkbox.setSelected(!checkbox.isSelected());
                  }
                  checkBoxList.repaint(); // redraw graphics
                  // enable buttons
                  if(isACheckBoxChecked()){
                      btnDelete.setEnabled(true);
                      btnBackup.setEnabled(false);
                  }
                  else{
                      btnDelete.setEnabled(false);
                      btnBackup.setEnabled(true);
                  }
               }
            }
        });
    }
    
    /**
     * removeAllChecks
     */
    public void removeAllChecks(){

        for(CheckBoxItem item: checkBoxItems)
            item.setSelected(false);
    }
    
    /**
     * checkAll
     */
    public void checkAllItems(){
        
        for(CheckBoxItem item: checkBoxItems)
            item.setSelected(true);
    }
    
    public void createDBTableToStoreBackupsInfo(){
        
        String createTableQuery = 
                "CREATE TABLE " + BACKUP_DB_TABLE_NAME +
                "(" +
                BACKUP_DB_TABLE_COLUMN_PK + " int(4) PRIMARY KEY AUTO_INCREMENT, " +
                BACKUP_DB_TABLE_COLUMN_1 + " VARCHAR(50) NOT NULL, " +
                BACKUP_DB_TABLE_COLUMN_2 + " VARCHAR(50) NOT NULL " +
                ");";
        
        // TODO - execute sql query
        if(!sql_commands.updateQuery(createTableQuery))
            JOptionPane.showMessageDialog(parentComponent, "unable to create table " + BACKUP_DB_TABLE_NAME );
    }

    public ArrayList<CheckBoxItem> getCheckBoxItemsFromDB() {
        
        // check box items array list to return
        ArrayList<CheckBoxItem> items = new ArrayList<>();
        
        // sql query to return a result set
        String sql = 
                "SELECT " + BACKUP_DB_TABLE_COLUMN_PK + "," + BACKUP_DB_TABLE_COLUMN_2 +
               " FROM " + BACKUP_DB_TABLE_NAME +
               " WHERE " + BACKUP_DB_TABLE_COLUMN_1 + " = '" + getTableName() + "' ;";
        
        ResultSet result = null;
        
        try {
            //Here we create our query
            PreparedStatement statement = getConnection().prepareStatement(sql);

            //Creating a variable to execute query
            result = statement.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            // if table doesn't exist and needs to be created
            if(ex.getMessage().endsWith("exist")){
                System.out.println("ENTERED !!!!!!!!!!!!!!!!!!!!!");
                createDBTableToStoreBackupsInfo();
                result = sql_commands.executeQuery(sql);
            }else{
                handleSQLexWithMessageBox(ex); // all other error messages
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
                    Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace();
                    handleSQLexWithMessageBox(ex); // any errors 
                }
            }
        }
        
        return items;
    }
    
    public void deleteSelectedItems(){
        
        for(CheckBoxItem item: checkBoxItems){
            if(item.isSelected()){
                deleteItem(item.getId());
                deleteItem(item.getCapped());
            }
        }
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
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
            return false;
        }
    }
    
    /**
     * Deletes the actual backup table (not the record of the backup table)
     * @param tableName tableName to be dropped from the database
     * @return boolean true if successful and false if sql error occurred 
     */
    public boolean deleteItem(String tableName) {
        
        if(tableName == CHECK_ALL_ITEM_TEXT)
            return false;
        
        try {
            dropTable(tableName);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
            return false;
        }
    }

    public void reloadCheckList() {
        checkBoxItems.clear();
        checkBoxItems.add(new CheckBoxItem(CHECK_ALL_ITEM_TEXT));
        checkBoxItems.addAll(getCheckBoxItemsFromDB());
        
        // if checkBoxItems only contains one item (check all) then remove it
        if(checkBoxItems.size() == 1)
            checkBoxItems.clear();
        
        // add CheckBoxItems to CheckBoxList
        checkBoxList.setListData(checkBoxItems.toArray());
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
            addBackupRecord(tableName, backupTableName);
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
            addBackupRecord(tableName, backupTableName);
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
                    reloadCheckList();
                    break;
                case 1:
                    backupTableName = getInputTableNameFromUser();
                    backupTable(tableName, backupTableName);
                    reloadCheckList();
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
            dropBackupRecord(tableName, backupTableName);
            createTableLike(tableName, backupTableName);
            backupTableData(tableName, backupTableName);
            addBackupRecord(tableName, backupTableName);
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
    
    public boolean addBackupRecord(){
        return addBackupRecord(getTableName(), getBackupTableName());
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
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
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
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
            return false;
        }
    }
    
    /**
     * checks if a checkbox is checked
     * @return boolean if a checkbox is checked (true) or not (false)
     */
    public boolean isACheckBoxChecked(){
        
        // check if a checkbox is checked
        for(CheckBoxItem item: checkBoxItems){
            if(item.isSelected())
                return true;
        }
        return false;
    }
    
    public void addCheckBoxAllCheckBoxItem(){
        checkBoxItems.add(new CheckBoxItem(CHECK_ALL_ITEM_TEXT));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelOutput = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        btnBackup = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelOutput.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panelOutputLayout = new javax.swing.GroupLayout(panelOutput);
        panelOutput.setLayout(panelOutputLayout);
        panelOutputLayout.setHorizontalGroup(
            panelOutputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelOutputLayout.setVerticalGroup(
            panelOutputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 185, Short.MAX_VALUE)
        );

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnBackup.setText("Backup");
        btnBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelOutput, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(btnDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                .addComponent(btnBackup)
                .addGap(108, 108, 108))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDelete)
                    .addComponent(btnBackup))
                .addGap(26, 26, 26))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackupActionPerformed
        // defaults to backup with the date appended
        backupDBTableWithDate(getTableName());
        reloadCheckList();
    }//GEN-LAST:event_btnBackupActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        deleteSelectedItems();
        reloadCheckList();
        // if no checkbox items are left
        if(checkBoxItems.isEmpty()){
            btnDelete.setEnabled(false);
            btnBackup.setEnabled(true);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BackupDBTablesWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BackupDBTablesWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BackupDBTablesWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BackupDBTablesWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BackupDBTablesWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBackup;
    private javax.swing.JButton btnDelete;
    private javax.swing.JPanel panelOutput;
    // End of variables declaration//GEN-END:variables
}
