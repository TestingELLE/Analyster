package com.elle.analyster.database;

import java.awt.Component;
import java.sql.Connection;
import javax.swing.JOptionPane;
import com.elle.analyster.logic.CheckBoxList;
import com.elle.analyster.logic.CheckBoxItem;
import com.elle.analyster.presentation.PopupWindow;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;

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
    
    // backup database table information
    // this information should match the database table
    private final String BACKUP_DB_TABLE_NAME = "Table_Backups";
    private final String BACKUP_DB_TABLE_COLUMN_PK = "id";
    private final String BACKUP_DB_TABLE_COLUMN_1 = "tableName";
    private final String BACKUP_DB_TABLE_COLUMN_2 = "backupTableName";
    private final String CHECK_ALL_ITEM_TEXT = "(All)";
    
    // private variables
    private CheckBoxList checkBoxList;
    private PopupWindow popupWindow;
    private ArrayList<CheckBoxItem> checkBoxItems;

    public BackupDBTableOrganizer(Connection connection, String tablename) {
        super(connection);
        setTableName(tablename);
        initComponents();
    }

    public BackupDBTableOrganizer(Connection connection, String tablename, Component parentComponent) {
        super(connection, parentComponent);
        setTableName(tablename);
        initComponents();
    }

    public BackupDBTableOrganizer(Statement statement, String tablename) {
        super(statement);
        setTableName(tablename);
        initComponents();
    }

    public BackupDBTableOrganizer(Statement statement, String tablename, Component parentComponent) {
        super(statement, parentComponent);
        setTableName(tablename);
        initComponents();
    }

    public BackupDBTableOrganizer(String host, String database, String username, String password, String tablename) {
        super(host, database, username, password);
        setTableName(tablename);
        initComponents();
    }

    public BackupDBTableOrganizer(String host, String database, String username, String password, String tablename, Component parentComponent) {
        super(host, database, username, password, parentComponent);
        setTableName(tablename);
        initComponents();
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
    
    private void initComponents(){
        
        String title = "title here";
        String message = "message here";
        
        setCheckBoxListListener();
        
        // checkbox item array
        checkBoxItems = new ArrayList<>();
        checkBoxItems.add(new CheckBoxItem(CHECK_ALL_ITEM_TEXT));
        checkBoxItems.addAll(getCheckBoxItemsFromDB());
        
        // add CheckBoxItems to CheckBoxList
        checkBoxList.setListData(checkBoxItems.toArray());
        
        // add CheckBoxList to a scrollpane
        ScrollPane scroll = new ScrollPane();
        scroll.add(checkBoxList);
        
        // buttons
        // create Delete button
        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSelectedItems();
            }
        });
        
        // create Cancel button
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //setVisible(false);
            }
        });
        
        // add buttons to the buttons array
        JButton[] buttons = new JButton[]{btnDelete, btnCancel};
        
        // dimension
        Dimension dimension = new Dimension(500,500);
        
        // Create a popup window 
        PopupWindow popup = new PopupWindow(title, message, scroll, buttons, dimension);
        popup.setLocationRelativeTo(null);
        popup.setVisible(true);

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
    }

    public ArrayList<CheckBoxItem> getCheckBoxItemsFromDB() {
        
        // check box items array list to return
        ArrayList<CheckBoxItem> items = new ArrayList<>();
        
        // sql query to return a result set
        String sql = 
                "SELECT " + BACKUP_DB_TABLE_COLUMN_PK + "," + BACKUP_DB_TABLE_COLUMN_2 +
               " FROM " + BACKUP_DB_TABLE_NAME +
               " WHERE " + BACKUP_DB_TABLE_COLUMN_1 + " = '" + getTableName() + "';";
        
        try {
            //Here we create our query
            PreparedStatement statement = getConnection().prepareStatement(sql);

            //Creating a variable to execute query
            ResultSet result = statement.executeQuery();

            // create checkbox items from result set and load up array list
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
            handleSQLexWithMessageBox(ex);
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
        
        reloadCheckList();
            
    }
    
    /**
     * Deletes record of the backup table (not the actual backup table)
     * @param id id of record in database table
     * @return boolean true if successful and false if sql error occurred 
     */
    public boolean deleteItem(int id) {
        
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
        checkBoxItems.addAll(getCheckBoxItemsFromDB());
    }
    
}
