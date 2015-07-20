
/**
 * User: danielabecker
 * 
 * -Refactored
 * @author Carlos Igreja
 * @since 6-29-2015
 * @version 0.6.5b
 */

package com.elle.analyster;

import com.elle.analyster.presentation.filter.DistinctColumnItem;
import com.elle.analyster.presentation.filter.JTableFilter;
import com.elle.analyster.presentation.filter.TableRowFilterSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Collection;

import static com.elle.analyster.service.Connection.connection;
import java.util.Map;


public class LoadTables implements ITableConstants{

    private Analyster ana =Analyster.getInstance();
    JLabel recordsLabel = ana.getRecordsLabel();

    Logger log = LoggerFactory.getLogger(LoadTables.class);
    
    Map<String,Tab> tabs = ana.getTabs(); // to store ne tab objects
    
    JTable assignmentTable = tabs.get(ASSIGNMENTS_TABLE_NAME).getTable();
    JTable reportTable = tabs.get(REPORTS_TABLE_NAME).getTable();
    JTable archiveAssignTable = tabs.get(ARCHIVE_TABLE_NAME).getTable();
    
    TableRowFilterSupport tableRowFilterSupport;


    /**
     *  called once to initialize the total row counts of each tabs table
     * @param tabs
     * @return 
     */
    public Map<String,Tab> initTotalRowCounts(Map<String,Tab> tabs) {
        
        int totalRecords;
 
        boolean isFirstTabRecordLabelSet = false;
        
        for (Map.Entry<String, Tab> entry : tabs.entrySet())
        {
            totalRecords = tabs.get(entry.getKey()).getTable().getRowCount();
            tabs.get(entry.getKey()).setTotalRecords(totalRecords);
            
            if(isFirstTabRecordLabelSet == false){
                ana.getRecordsLabel()
                        .setText(tabs.get(entry.getKey()).getRecordsLabel());
                isFirstTabRecordLabelSet = true; // now its set
            }
        }

        return tabs;
    }
    
    
    /**
     * This method takes a tabs Map and loads all the tabs/tables
     * @param tabs
     * @return 
     */
    public Map<String,Tab> loadTables(Map<String,Tab> tabs) {
        
        for (Map.Entry<String, Tab> entry : tabs.entrySet())
        {
            loadTable(tabs.get(entry.getKey()).getTable());
            ana.setTerminalsFunction(tabs.get(entry.getKey()).getTable());
        }

        ana.setLastUpdateTime();
        
        return tabs;
    }
    

      /**
       * This method takes a table and loads it
       * Does not need to pass the table back since it is passed by reference
       * However, it can make the code clearer and it's good practice to return
       * @param table 
       */
    public JTable loadTable(JTable table) {
        
        // make sure column percents are set in tabs first
        
        try {
            connection(ana.sqlQuery(table.getName()), table);
        } catch (SQLException e) {
            log.error("Error", e);
        }
        ana.setColumnFormat(ana.getTabs().get(table.getName()).getColWidthPercent(), table);
          
        // new tableRowFilterSupport instance and takes table to set filter
        tableRowFilterSupport = new TableRowFilterSupport(tabs.get(table.getName()).getTable());

        // set actions visible to true
        tableRowFilterSupport.setActionsVisible(true);

        // apply changes to tableRowFilterSupport
        // This method still needs refactoring -> legacy code
        tableRowFilterSupport.apply();

        // set filter to tabs table
        tabs.get(table.getName()).setFilter(tableRowFilterSupport.getFilter());

        // set filtered table in tabs using the filter to filter and return table
        tabs.get(table.getName()).setFilteredTable(tabs.get(table.getName()).getFilter().getTable());
            
        // this enables or disables the menu components for this tab
        ana.getjActivateRecord().setEnabled(tabs.get(table.getName()).isActivateRecordMenuItemEnabled()); 
        ana.getjArchiveRecord().setEnabled(tabs.get(table.getName()).isArchiveRecordMenuItemEnabled()); 
        
        return table;
    }
    
    
    /**
     * This method is called by LoadPrevious method in Analyster class
     * @param columnIndex
     * @param filterCriteria 
     */
    public void loadAssignmentTableWithFilter(int columnIndex, Collection<DistinctColumnItem> filterCriteria) {

        try {
            connection(ana.sqlQuery(Analyster.getAssignmentsTableName()), assignmentTable);
        } catch (SQLException e) {
            log.error("Error", e);
        }
        ana.setColumnFormat(ana.getTabs().get(ASSIGNMENTS_TABLE_NAME).getColWidthPercent(), assignmentTable);
        
        // new tableRowFilterSupport instance and takes table to set filter
        tableRowFilterSupport = new TableRowFilterSupport(tabs.get(assignmentTable.getName()).getTable());

        // set actions visible to true
        tableRowFilterSupport.setActionsVisible(true);

        // apply changes to tableRowFilterSupport
        // This method still needs refactoring -> legacy code
        tableRowFilterSupport.apply();
        
        // get filter and store it in a new variable
        JTableFilter filter = tableRowFilterSupport.getFilter();
        
        // apply filter changes -> not sure / legacy code / still refactoring
        ana.setFilterTempAssignment(filter);
        ana.getFilterTempAssignment().getTable();   // create filter when the table is loaded.
        //ana.setNumberAssignmentInit(assignmentTable.getRowCount());
        ana.getjActivateRecord().setEnabled(false);
        ana.getjArchiveRecord().setEnabled(true);


        // testing, looks like just filter and number
        tabs.get("Assignments").setFilter(filter);
        //tabs.get("Assignments").setTotalRecords(assignmentTable.getRowCount());

        // set label record information -> this should not be done here : only in Analyster
        //recordsLabel.setText(tabs.get(ASSIGNMENTS_TABLE_NAME).getRecordsLabel()); 

        // why is this code here?
        filter.apply(columnIndex, filterCriteria);
        filter.saveFilterCriteria(filterCriteria);
        filter.setColumnIndex(columnIndex);


    }
      
} // end LoadTables
