
package com.elle.analyster;

import com.elle.analyster.presentation.filter.JTableFilter;
import javax.swing.JTable;

/**
 *
 * @author Carlos Igreja
 * @since  June 25, 2015
 */
public class Tab implements ITableConstants{

    private String tableName; 
    private JTable table;
    private JTable filteredTable;
    private JTableFilter filter;
    private float[] colWidthPercent;
    private int totalRecords;
    private int recordsShown;
    private String[] tableColNames;
    private String[] searchFields;
    
    // these menu items are enabled differently for each tab
    private boolean activateRecordMenuItemEnabled;
    private boolean archiveRecordMenuItemEnabled;
    private boolean addRecordsBtnVisible;

    
    /**
     * 
     */
    public Tab() {
        tableName = "";
        table = new JTable();
        filteredTable = new JTable();
        totalRecords = 0;
        recordsShown = 0;
        activateRecordMenuItemEnabled = false;
        archiveRecordMenuItemEnabled = false;
        addRecordsBtnVisible = false;
        
        // filter is an instance and does not get initialized
    }
    
    /**
     * CONSTRUCTOR
     * This would be the ideal constructor, but there are issues with 
     * the initcomponents in Analyster so the tab must be initialized first
     * then the table can be added
     * @param table 
     */
    public Tab(JTable table) {
        tableName = "";
        this.table = table;
        filteredTable = new JTable();
        totalRecords = 0;
        recordsShown = 0;
        // filter is an instance and does not get initialized
        
        // store the column names for the table
        for (int i = 0; i < table.getColumnCount(); i++) 
            tableColNames[i] = table.getColumnName(i);
    }
    
    /**************************************************************************
     ********************** Setters & Getters *********************************
     **************************************************************************/

    
    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }
    
    public JTable getFilteredTable() {
        return filteredTable;
    }

    public void setFilteredTable(JTable filteredTable) {
        this.filteredTable = filteredTable;
    }

    public JTableFilter getFilter() {
        return filter;
    }

    public void setFilter(JTableFilter filter) {
        this.filter = filter;
    }

    public float[] getColWidthPercent() {
        return colWidthPercent;
    }

    public void setColWidthPercent(float[] colWidthPercent) {
        this.colWidthPercent = colWidthPercent;
    }
    
    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getRecordsShown() {
        return getTable().getRowCount();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isActivateRecordMenuItemEnabled() {
        return activateRecordMenuItemEnabled;
    }

    public void setActivateRecordMenuItemEnabled(boolean activateRecordMenuItemEnabled) {
        this.activateRecordMenuItemEnabled = activateRecordMenuItemEnabled;
    }

    public boolean isArchiveRecordMenuItemEnabled() {
        return archiveRecordMenuItemEnabled;
    }

    public void setArchiveRecordMenuItemEnabled(boolean archiveRecordMenuItemEnabled) {
        this.archiveRecordMenuItemEnabled = archiveRecordMenuItemEnabled;
    }

    public String[] getTableColNames() {
        return tableColNames;
    }

    public void setTableColNames(String[] tableColNames) {
        this.tableColNames = tableColNames;
    }
    
    public void setTableColNames(JTable table) {
        tableColNames = new String[table.getColumnCount()];
        for (int i = 0; i < table.getColumnCount(); i++) 
            tableColNames[i] = table.getColumnName(i);
    }

    public String[] getSearchFields() {
        return searchFields;
    }

    public void setSearchFields(String[] searchFields) {
        this.searchFields = searchFields;
    }

    public boolean isAddRecordsBtnVisible() {
        return addRecordsBtnVisible;
    }

    public void setAddRecordsBtnVisible(boolean addRecordsBtnVisible) {
        this.addRecordsBtnVisible = addRecordsBtnVisible;
    }
    
    

    /**************************************************************************
     *************************** Methods **************************************
     **************************************************************************/
    
    /**
     * This method subtracts an amount from the totalRecords value
     * This is used when records are deleted to update the totalRecords value
     * @param amountOfRecordsDeleted 
     */
    public void subtractFromTotalRowCount(int amountOfRecordsDeleted) {
        this.totalRecords = this.totalRecords - amountOfRecordsDeleted;
    }
    
    /**
     * This method subtracts an amount from the totalRecords value
     * This is used when records are deleted to update the totalRecords value
     * @param amountOfRecordsDeleted 
     */
    public void addToTotalRowCount(int amountOfRecordsAdded) {
        this.totalRecords = this.totalRecords + amountOfRecordsAdded;
    }
    
    /**
     * This method returns a string that displays the records.
     * @return String This returns a string that has the records for both total and shown
     */
    public String getRecordsLabel(){
        
        String output;
        
        switch (getTableName()) {
            case ASSIGNMENTS_TABLE_NAME:
                output = "<html><pre>"
                       + "          Number of records shown: " + getRecordsShown() 
                  + "<br/> Number of records in Assignments: " + getTotalRecords()
                     + "</pre></html>";
                break;
            case REPORTS_TABLE_NAME:
                output = "<html><pre>"
                       + "      Number of records shown: " + getRecordsShown() 
                  + "<br/> Number of records in Reports: " + getTotalRecords() 
                     + "</pre></html>";
                break;
            case ARCHIVE_TABLE_NAME:
                output = "<html><pre>"
                       + "      Number of records shown: " + getRecordsShown() 
                  + "<br/> Number of records in Archive: " + getTotalRecords() 
                     + "</pre></html>";
                break;
            default:
                // this means an invalid table name constant was passed
                // this exception will be handled and thrown here
                // the program will still run and show the stack trace for debugging
                output = "<html><pre>"
                       + "*******ATTENTION*******"
                  + "<br/>Not a valid table name constant entered"
                     + "</pre></html>";
                try {
                    String errorMessage = "ERROR: unknown table";
                    throw new NoSuchFieldException(errorMessage);
                } catch (NoSuchFieldException ex) {
                    ex.printStackTrace();
                    // post to log.txt
                    Analyster.getInstance().getLogwind().sendMessages(ex.getMessage());
                }
        
                break;
        }
        
        return output;
    }
    
}// end Tab
