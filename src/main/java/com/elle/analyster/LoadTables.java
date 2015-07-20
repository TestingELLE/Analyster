
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
import com.elle.analyster.presentation.filter.TableFilterColumnPopup;
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
    
    JTableFilter jTableFilter;
    TableFilterColumnPopup filterPopup;


    
      
} // end LoadTables
