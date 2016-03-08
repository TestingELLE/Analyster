package com.elle.analyster.newBackupDBTableDesign;

import com.elle.analyster.logic.CheckBoxItem;
import com.elle.analyster.logic.CheckBoxList;
import com.elle.analyster.logic.LoggingAspect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * This is a JDialog window for backing up DB tables.
 * @author Carlos Igreja
 * @since  2-11-2016
 */
public class BackupDBTablesDialog extends javax.swing.JPanel {

    // private variables
    private String tableName;
    private String backupTableName;
    //private Connection connection;
    //private Statement statement;
    private Component parentComponent; // used to display noBackupsMsg relative to parent component
    private CheckBoxList checkBoxList;
    private ArrayList<CheckBoxItem> checkBoxItems;
    private Dimension dimension = new Dimension(600,400); // dimension
    private final String CHECK_ALL_ITEM_TEXT = "(All)";
    private BackupDBTableDAO dao;
    
    /**
     * Creates new form BackupDBTablesWindow
     */
    public BackupDBTablesDialog(Connection connection, String tableName, Component parent) {
        initComponents();

        //this.connection = connection;  // testing
        this.tableName = tableName;  // testing
        this.parentComponent = parent;  // testing
        
        this.backupTableName = null;
        this.dao = new BackupDBTableDAO(connection, parent);
        
        setCheckBoxListListener();
        
        // checkbox item array
        checkBoxItems = new ArrayList<>();
        checkBoxItems.add(new CheckBoxItem(CHECK_ALL_ITEM_TEXT));
        checkBoxItems.addAll(dao.getCheckBoxItemsFromDB(getTableName()));
        
        // if checkBoxItems only contains one item (check all) then remove it
        if(checkBoxItems.size() == 1){
            checkBoxItems.clear();
            displayNoBackupsCheckListMsg();
        }
        else{
            // add CheckBoxItems to CheckBoxList
            checkBoxList.setListData(checkBoxItems.toArray());
        }
        
        // add CheckBoxList to the panel
        ScrollPane scroll = new ScrollPane();
        scroll.add(checkBoxList);
        scroll.setPreferredSize(panelOutput.getPreferredSize());
        panelOutput.setLayout(new BorderLayout());
        panelOutput.add(scroll, BorderLayout.CENTER);
        
        // start the delete button as not enabled
        btnDelete.setEnabled(false);
        
        // set title
        String title = "Backup " + tableName; // window title
        //this.setTitle(title);
        
        // show window
        setVisible(true);
        
        // add to a JDialog for modal funtionality
        JDialog dialog = new JDialog((Frame) parent, "Backup " + tableName, true);
        //BackupDBTablesJPanel panel = new BackupDBTablesDialog(con, tableName, parent);
        dialog.add(this);
        dialog.setSize(dimension);
        //dialog.pack();
        dialog.setLocationRelativeTo(parentComponent);
        dialog.setVisible(true);
        
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

    public void deleteSelectedItems(){
        
        for(CheckBoxItem item: checkBoxItems){
            if(item.isSelected()){
                dao.deleteItem(item.getId());
                deleteItem(item.getCapped());
            }
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
            dao.dropTable(tableName);
            return true;
        } catch (SQLException ex) {
            LoggingAspect.afterThrown(ex);
            return false;
        }
    }

    public void reloadCheckList() {
        checkBoxItems.clear();
        checkBoxItems.add(new CheckBoxItem(CHECK_ALL_ITEM_TEXT));
        checkBoxItems.addAll(dao.getCheckBoxItemsFromDB(getTableName()));
        
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
            LoggingAspect.afterThrown(ex);
        }
        
        String server = "jdbc:mysql://" + host +":3306/" + database;
        Connection connection = null;
        
        try {
            // get connection
            connection = DriverManager.getConnection(server, username, password);
        } catch (SQLException ex) {
            LoggingAspect.afterThrown(ex);
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
            LoggingAspect.afterThrown(ex);
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
        
        this.tableName = tableName; // needs to be set for backup complete noBackupsMsg
        
        // create a new backup table name with date
        this.backupTableName = tableName + getTodaysDate();
        
        // execute sql statements
        try {
            
            dao.createTableLike(tableName, backupTableName);
            dao.backupTableData(tableName, backupTableName);
            dao.addBackupRecord(tableName, backupTableName);
            displayBackupCompleteMessage();
            return true;

        } catch (SQLException ex) {

            String message = ex.getMessage();

            // if backup database already exists
            if (message.endsWith("already exists")) {
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
                switch (optionSelected) {
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
                return true;
            } 
            else {
                LoggingAspect.afterThrown(ex);
                return false;
            }
        }
    }
    
    /**
     * Backs up a table in the database
     * @param tableName         the table in the database to backup up (original)
     * @param backupTableName   the name of the new table (the backup table)
     * @return                  boolean returns true if the backup was a success 
     */
    public boolean backupTable(String tableName, String backupTableName){
        
        // these need to be set for the backup complete noBackupsMsg
        this.tableName = tableName;
        this.backupTableName = backupTableName;
        
        try {
            dao.createTableLike(tableName, backupTableName);
            dao.backupTableData(tableName, backupTableName);
            dao.addBackupRecord(tableName, backupTableName);
            displayBackupCompleteMessage();
            return true;
        } catch (SQLException ex) {
            LoggingAspect.afterThrown(ex);
            return false;
        }
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
     * Drops a table and creates a new one if it already exists
     * Drops the backup table and creates a new backup with the table name
     * and today's date.
     */
    public void overwriteBackupDB(){
        
        try {
            dao.dropTable(backupTableName);
            dao.dropBackupRecord(tableName, backupTableName);
            dao.createTableLike(tableName, backupTableName);
            dao.backupTableData(tableName, backupTableName);
            dao.addBackupRecord(tableName, backupTableName);
            displayBackupCompleteMessage();
        } catch (SQLException ex) {
            LoggingAspect.afterThrown(ex);
        }
    }
    
    /**
     * Gets input for the table name from the user using an input noBackupsMsg box
     * @return the input the user entered into the input text box
     */
    public String getInputTableNameFromUser(){
        // input dialog box 
        String message = "Enter the name for the backup";
        return JOptionPane.showInputDialog(parentComponent, message);
    }
    
    /**
     * A noBackupsMsg box that displays when 
 the backup was completed successfully.
     */
    public void displayBackupCompleteMessage(){
        String message = tableName + " was backed up as " + backupTableName;
        JOptionPane.showMessageDialog(parentComponent, message);
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

    public Component getParentComponent() {
        return parentComponent;
    }

    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }
    
    public boolean addBackupRecord(){
        return dao.addBackupRecord(getTableName(), getBackupTableName());
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
        jLabel1 = new JLabel();
        panelButtons = new javax.swing.JPanel();
        btnBackup = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        javax.swing.GroupLayout panelOutputLayout = new javax.swing.GroupLayout(panelOutput);
        panelOutput.setLayout(panelOutputLayout);
        panelOutputLayout.setHorizontalGroup(
            panelOutputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelOutputLayout.setVerticalGroup(
            panelOutputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 322, Short.MAX_VALUE)
        );

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Existing Backup Database tables");
        jLabel1.setRequestFocusEnabled(false);

        btnBackup.setText("Backup");
        btnBackup.setToolTipText("Create a table backup (name defaults with data appended)");
        btnBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackupActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.setToolTipText("Delete selectd table backups");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelButtonsLayout = new javax.swing.GroupLayout(panelButtons);
        panelButtons.setLayout(panelButtonsLayout);
        panelButtonsLayout.setHorizontalGroup(
            panelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelButtonsLayout.createSequentialGroup()
                .addGap(218, 218, 218)
                .addComponent(btnDelete)
                .addGap(20, 20, 20)
                .addComponent(btnBackup)
                .addGap(223, 223, 223))
        );
        panelButtonsLayout.setVerticalGroup(
            panelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelButtonsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDelete)
                    .addComponent(btnBackup))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(panelOutput, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelOutput, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBackup;
    private javax.swing.JButton btnDelete;
    private JLabel jLabel1;
    private javax.swing.JPanel panelButtons;
    private javax.swing.JPanel panelOutput;
    // End of variables declaration//GEN-END:variables

    private void displayNoBackupsCheckListMsg() {
        // add some noBackupsMsg 
        String noBackupsMsg = "no known prior backups";
        JLabel labelCheckListMsg = new JLabel(noBackupsMsg);
        labelCheckListMsg.setHorizontalAlignment(JLabel.CENTER);
        labelCheckListMsg.setHorizontalTextPosition(JLabel.CENTER);
        checkBoxList.setListData(new JLabel[]{labelCheckListMsg});
    }
}
