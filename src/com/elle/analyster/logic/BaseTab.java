package com.elle.analyster.logic;

import com.elle.analyster.controller.DataManager;
import com.elle.analyster.database.ModifiedData;
import com.elle.analyster.database.ModifiedTableData;
import com.elle.analyster.presentation.AnalysterWindow;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 * Tab This class is used to create a tab object. This object contains all the
 * components of the tab on Analyster. Each tab may have its own attributes and
 * that is what this class is for.
 *
 * @author Carlos Igreja
 * @since June 10, 2015
 * @version 0.6.3
 */
public abstract class BaseTab implements ITableConstants {
    
    //reference to PMwindow
    protected AnalysterWindow analysterWindow;
    protected DataManager dataManager;

    // attributes
    protected JTable table;                        // the JTable on the tab
    
    // for pm window display
    protected int totalRecords;                    // total records in table model
    protected int recordsShown;                    // number of records shown on table
    
    //array data members
    protected float[] colWidthPercent;             // column width for each colum
    protected String[] tableColNames;              // column header names
    protected String[] searchFields;               // search combobox options
    protected String[] batchEditFields;            // batch edit combobox options
    
    //object data members
    protected TableFilter filter;                  // filter used for the table
    protected ColumnPopupMenu columnPopupMenu;     // column filter pop up menu
    protected JTableCellRenderer cellRenderer;     // table cell renderer
    protected ModifiedTableData modTableData;         // modified table data object
    
    //Storing Jcomponents states
    /*     addButton visibility, 
           batchEdit visibility,  
           uploadchangebtn visibility, 
           revertchangebtn visibility,
           editmode false
           -----------------
           activateRecordEnabled
           archiveRecordEnabled;
           openDocumentEnabled;
           stripSlashEnabled;
           addSlashEnabled;
    */
    protected ButtonsState state;
    

    /**
     * CONSTRUCTOR Tab This is used if no table is ready such as before
     * initComponents of a frame.
     */
    public BaseTab() {
        
    }

    /**
     * CONSTRUCTOR This would be the ideal constructor, but there are issues
     * with the initcomponents in Analyster so the tab must be initialized first
     * then the table can be added
     *
     * @param table
     */
    public BaseTab(JTable table)  {
        //set up reference to main window
        analysterWindow = AnalysterWindow.getInstance();
        dataManager = DataManager.getInstance();
        //set up table
        this.table = table;  
        //setup tab data
        setUpTabData();
        
    }
    
    /*
    ** Methods needs to be implemented individually in child class
    */
    //abstract function will be implemented individually in child classes.
    /*set search fileds, set button state, set column width, etc */
    protected abstract void initializeTabData();
    //getData from dataManager
    protected abstract List<Object[]> getData();
    //upload to dataManager
    protected abstract void uploadData(List<Object[]> rowsData);
    //get selected data from dataManager
    protected abstract Object[] getSelectedData(int id);
    //only some table support the left ctrl single click
    protected abstract void leftCtrlMouseClick();
    
    
    //make table model editable
    public void makeTableEditable(boolean editable){
        EditableTableModel model = ((EditableTableModel) table.getModel());
        model.setCellEditable(editable);
        
    }
    
    
    protected void setUpTabData()  {
        //tab-specific data, including fields, state, colwidth
        initializeTabData();
     
        setTableColNames(table);
        
         //set up objects
        filter = new TableFilter(this);
        columnPopupMenu = new ColumnPopupMenu(this);
        cellRenderer = new JTableCellRenderer(table);
        
        //initialize counts
        totalRecords = 0;
        recordsShown = 0;
        
        //load table data
        loadTableData();
        
        //after loading table data, set up modeTableData
        modTableData = new ModifiedTableData(table);
        
        
        //set up table listeners
        setTableListeners();
        
        //make two change buttons disabled
        enableChangeButtons(false);
        
      
    }
    
    
    //update table, reload table, load table functions
    protected void loadTableData()  {
         System.out.println("now loading..." + table.getName());
        
         List<Object[]> tableData = getData();
        
        for (Object[] rowData : tableData) {
            insertRow(rowData);
        }
           
      
        //make table editable 
        //everytime ,loading will get new mode
        //thus set up tableModel listeners each time
        EditableTableModel model = new EditableTableModel(listToVector(tableData), arrayToVector(tableColNames));
        table.setModel(model);
        setTableModelListener();
        
        
         // apply filter
        
        if (filter.getFilterItems() == null) {
            filter.initFilterItems();
        }
        filter.applyFilter();
        filter.applyColorHeaders();

        // load all checkbox items for the checkbox column pop up filter
        columnPopupMenu.loadAllCheckBoxItems();

        // set column format
        setColumnFormat();

        // update last time the tableSelected was updated
        setLastUpdateTime();
     
    }
    
    protected Vector<Object> arrayToVector(Object[] input){
        Vector<Object> output = new Vector(input.length);
        for(Object item : input) {
            output.add(item);
        }
        return output;
    }
    
    protected Vector<Vector> listToVector(List<Object[]> input) {
        Vector<Vector> output = new Vector(input.size());
        for(Object[] item : input) {
            Vector temp = new Vector(item.length);
            for(Object obj: item) {
                temp.add(obj);
                
            }
            output.add(temp);
        }
        return output;
    }
    
    //upload changes
    public void uploadChanges() {
        //get changed rows
        ArrayList<Object[]> changedRowsData = new ArrayList();
        List<ModifiedData> modifiedDataList = modTableData.getNewData();

        //loop the modified data list
        for (ModifiedData modifiedData : modifiedDataList) {
            int id = modifiedData.getId();
            
            //get the row data
            int rowIndex = getRowIndex(id);
            if (rowIndex != -1) {
                Object[] rowData = new Object[table.getColumnCount()];
                for (int i = 0; i < table.getColumnCount(); i++) {
                   
                    rowData[i] = table.getValueAt(rowIndex, i);
                    
                }
                changedRowsData.add(rowData);
            }
        }

        //upload changes   
        uploadData(changedRowsData);
        
        //original code includes reloading the table, however, the tabledata is already updated, no need to reload.
        
        // clear cellrenderer
        cellRenderer.clearCellRender();
        
        //clear selection
        table.getSelectionModel().clearSelection();

        // reload modified tableSelected data with current tableSelected model
        //reload also clears its newData 
        modTableData.reloadData();

        //makeTableEditable(labelEditModeState.getText().equals("OFF") ? true : false);
        // reset the arraylist to record future changes
        setLastUpdateTime();          // update time

        //set up btton state
        state.enableEdit(false);
        //change table mode
        makeTableEditable(false);
        analysterWindow.changeTabbedPanelState(this);
        
        
    }
    
    //get rowIndex for a particular issue id
    protected int getRowIndex(int id) {
        int tableRowsCnt = table.getRowCount();
        for(int i = 0; i < tableRowsCnt; i++) {
            int field0 = (int) table.getValueAt(i, 0);
            if (field0 == id) {
                return i;
            }
        }
        return -1;
    }
    
    public void revertChanges() {
       
        
        modTableData.getNewData().clear();  // clear any stored changes (new data)
        reloadTable();
        makeTableEditable(true);

        LoggingAspect.afterReturn("Nothing has been Changed!");

        modTableData.reloadData();  // reloads data of new table (old data) to compare with new changes (new data)
        //disable buttons
        enableChangeButtons(false);
    
    }
    
    public void reloadTable(){
        
        DefaultTableModel dm = (DefaultTableModel)table.getModel();
        //clear all current rows
        while (dm.getRowCount() > 0) {
            dm.removeRow(0);
        }
        //reset the total records count to 0
        setTotalRecords(0);
        
        
        //if table is sorted, save the info -Yi
        List<RowSorter.SortKey> keys = (List<RowSorter.SortKey>)table.getRowSorter().getSortKeys();
     
        
        reloadData();
        
        
        //reset the sorter key -Yi
        table.getRowSorter().setSortKeys(keys);
        

        LoggingAspect.afterReturn(table.getName() + " is reloading");

    }
    
    
    //reload selected data
    public void reloadSelectedData() {
        int row = table.getSelectedRow();
        if(row == -1){
           
        }
        else{
            int id = (int)table.getValueAt(row, 0);
            Object[] rowData = null;
            rowData = getSelectedData(id);
            
            updateRow(rowData);
            LoggingAspect.afterReturn("Selected record #" + id + " is reloading");
          
       }
        
        
        
    }
    
    
    //reload the current tab data in table
    protected void reloadData() {
        
        
        // reload tableSelected from database
        loadTableData();

        // clear cellrenderer
        cellRenderer.clearCellRender();

        // reload modified tableSelected data with current tableSelected model
        modTableData.reloadData();

        // set label record information
        setLabelRecords();

    }
    
    /*
    ** insert, update, and delete row
    */
    public void insertRow(Object[] rowData) {
        
        if (rowData != null) {
            ((DefaultTableModel)table.getModel()).addRow(rowData);
            addToTotalRowCount(1);
        
        }
   
        
    }
    
    public void updateRow(Object[] rowData) {
        int row = findTableModelRow(rowData);
        if(row != -1 && rowData != null){
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            
            // remove table listeners because this listens for changes in 
            // table and changes the cell green for upload changes and revert
            // changes. So I remove them and then put them back.
            TableModelListener[] listeners = model.getTableModelListeners();
            for(int i = 0; i < listeners.length; i++){
                model.removeTableModelListener(listeners[i]);
            }

            // update -> no need for id
            for(int i = 1; i < rowData.length; i++) {
                model.setValueAt(rowData[i], row, i);
            }
            
            
            // add back the table listeners
            for(int i = 0; i < listeners.length; i++){
                model.addTableModelListener(listeners[i]);
            }
            
            table.repaint();
        }
        else{
            //offline update , insert new row
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.addRow(rowData);
        }
    }

    public void deleteRow(int rowIndex){
        
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        //need to convert to model index
        int index  = table.convertRowIndexToModel(rowIndex);
        
        model.removeRow(index);
        subtractFromTotalRowCount(1);
        
    }
    
    
    
    /*
    **register table listeners
    */
    
    protected void setTableListeners() {

        setTableHeaderListeners();
        setTableBodyListeners();
        
    }
       
    protected void setTableHeaderListeners() {
        // this adds a mouselistener to the tableSelected header
        JTableHeader header = table.getTableHeader();
        //disable default mouse listeners
        MouseListener[] listeners = header.getMouseListeners();
        
        for (MouseListener ml: listeners)
        {
            String className = ml.getClass().toString();
            System.out.println(className);
            if (className.contains("BasicTableHeaderUI$MouseInputHandler")){
                header.removeMouseListener(ml);
                
            }
                
        }

        //add customized mouselistener
     
        if (header != null) {
            header.addMouseListener(new MouseAdapter() {
                
                @Override
                public void mouseClicked(MouseEvent e) {
                    //double click header : clear filter for the column
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        if (e.getClickCount() == 2) {
                            int columnIndex = table.getColumnModel().getColumnIndexAtX(e.getX());
                            clearColumnFilter(columnIndex);
                            
                        }
                        
                        else {
                            if (e.getClickCount() == 1 ) {
                                
                                if(e.isControlDown())
                                    columnPopupMenu.showPopupMenu(e);
                                else {
                                    int columnIndex = header.columnAtPoint(e.getPoint());
                                    if (columnIndex != -1) {
                                        columnIndex = table.convertColumnIndexToModel(columnIndex);
                                        table.getRowSorter().toggleSortOrder(columnIndex);
                                    }
                                    
                                }
                        
                            }
                        
                        }
           
                    }
              
                }

                //right mouse click for showing popupmenu
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        columnPopupMenu.showPopupMenu(e);
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                   
                    if (e.isPopupTrigger()) {
                        // this calls the column popup menu
                        columnPopupMenu.showPopupMenu(e);
                    }
                }
            });
        }     
        
    }
    
    protected void setTableBodyListeners() {
        BaseTab localTab = this;
        
        table.addMouseListener(new MouseAdapter() {
        
            @Override
            public void mouseClicked(MouseEvent e) {
               
                
                // if left mouse clicks
                if (SwingUtilities.isLeftMouseButton(e)) {
                    
                    if (e.getClickCount() == 2) {
                        e.consume();
                        //select a row, then ctrl + double click  : filter based on selected value
                        if (e.isControlDown()) {
                            
                            filterByDoubleClick();
                        } else {
                            //double click : enable edit
                            if (e.getComponent() instanceof JTable) {
                                //if not in edit mode, then swith to edit mode
                                if (!state.isEditMode()) {
                                    //change button states
                                    state.enableEdit(true);
                                    analysterWindow.changeTabbedPanelState(localTab);
                                    //make table editable
                                    makeTableEditable(true);
                                    
                                    //check if modtabledata has new data already
                                    if(modTableData.getNewData().size() > 0) 
                                        enableChangeButtons(true);
                                    else enableChangeButtons(false);
                                    
                                }
                                
                                
                                // get selected cell for editing
                                int columnIndex = table.columnAtPoint(e.getPoint()); // this returns the column index
                                int rowIndex = table.rowAtPoint(e.getPoint()); // this returns the rowIndex index

                                table.changeSelection(rowIndex, columnIndex, false, false);
                                
                                selectAllText();

                            }
                        }
                    } else {
                        if (e.getClickCount() == 1) {
                            e.consume();
                            if (state.isEditMode()) {
                                    selectAllText();
                                }
                            if (e.isControlDown()) {
                                //this part is only for some table
                                //need to implement individually
                                leftCtrlMouseClick();

                            }
                        }
                        
                    }
                } // end if left mouse clicks
                // if right mouse clicks
                else if (SwingUtilities.isRightMouseButton(e)) {
                    if (e.getClickCount() == 2) {
                        
                    
                    } // end of is tab editing conditions

                } // end if 2 clicks 
            } // end if right mouse clicks
       
        });
        
        // add keyListener to the table
        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                // selects all the row of a table if Ctrl-A (Cmd-A in Mac)
                //   is pressed
                if ((ke.getKeyCode() == KeyEvent.VK_A)
                        && ((ke.getModifiers() & Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) != 0)) {
                    table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    table.setRowSelectionAllowed(true);
                    table.setRowSelectionInterval(0, table.getRowCount() - 1);
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_F2) {
                    //toggle on/off edit mode
                    //if it is not in edit mode
                    if (!state.isEditMode()) {
                        //change button states
                        state.enableEdit(true);
                        analysterWindow.changeTabbedPanelState(localTab);
                        //make table editable
                        makeTableEditable(true);

                        //check if modtabledata has new data already
                        if (modTableData.getNewData().size() > 0) {
                            enableChangeButtons(true);
                        } else {
                            enableChangeButtons(false);
                        }
                   
                    }
                    //if it is already in edit mode
                    else {
                        state.enableEdit(false);
                        analysterWindow.changeTabbedPanelState(localTab);
                        //make table editable
                        makeTableEditable(false);
                        
                    }
                }
            }
        });
        
    }
    
    
    protected void selectAllText() {// Select all text inside jTextField

        int row = table.getSelectedRow();
        int column = table.getSelectedColumn();
        if (column != 0) {
            table.getComponentAt(row, column).requestFocus();
            table.editCellAt(row, column);
            JTextField selectCom = (JTextField) table.getEditorComponent();
            if (selectCom != null) {
                selectCom.requestFocusInWindow();
                selectCom.selectAll();
            }
        }

    }
    
    protected void setTableModelListener() {
        
        table.getModel().addTableModelListener(new TableModelListener() {  // add tableSelected model listener every time the tableSelected model reloaded
            @Override
            public void tableChanged(TableModelEvent e) {
                System.out.println("table changed");
                int row = e.getFirstRow();
                int col = e.getColumn();

                ModifiedTableData data = modTableData;

                if (col != -1) {
                    Object oldValue = data.getOldData()[row][col];
                    Object newValue = table.getModel().getValueAt(row, col);

                    // check that data is different
                    if (!newValue.equals(oldValue)) {

                        String tableName = table.getName();
                        String columnName = table.getColumnName(col);
                        int id = (Integer) table.getModel().getValueAt(row, 0);

                        data.addNewData(new ModifiedData(tableName, columnName, newValue, id));
                        
                        //enable two buttons
                        enableChangeButtons(true);
                        // color the cell
                        cellRenderer.getCells().get(col).add(row);
                        table.getColumnModel().getColumn(col).setCellRenderer(cellRenderer);

                    } 
                    
                }
            }
        });
    }
    
    
    private void enableChangeButtons(boolean option) {
        analysterWindow.getBtnUploadChanges().setEnabled(option);
        analysterWindow.getBtnRevertChanges().setEnabled(option);
    }
    
    /*end of registering table listeners*/
   
    
    /* flitering related methods */
    //clear filter for a column
    protected void clearColumnFilter(int columnIndex) {
        
        filter.clearColFilter(columnIndex);
        filter.applyFilter();
        
    }
    
    //filter a cell value by double click
    protected void filterByDoubleClick() {

        int columnIndex = table.getSelectedColumn(); // this returns the column index
        int rowIndex = table.getSelectedRow(); // this returns the rowIndex index
        if (rowIndex != -1) {
            Object selectedField = table.getValueAt(rowIndex, columnIndex);
  
            filter.addFilterItem(columnIndex, selectedField);
            filter.applyFilter();
            
            
        }
    }
    
    
    
    /*
    ** updating main window jcomponents elements
    */
    
    //set up table last update time
    protected void setLastUpdateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(new Date());
        analysterWindow.getLabelTimeLastUpdate().setText("Last updated: " + time);
    }
    
    //set up labelRecoreds
    public void setLabelRecords() {
        analysterWindow.getLabelRecords().setText(getRecordsLabel());
    } 
    
    
    
    
    /*
    **table-related helper functions 
    */
    
    //find issue in table
    protected int findTableModelRow(Object[] rowData) {
        int rowCount = table.getModel().getRowCount();
        TableModel model = table.getModel();
        for(int rowIndex = 0; rowIndex < rowCount; rowIndex++){
            int rowId = Integer.parseInt(model.getValueAt(rowIndex, 0).toString());
            if(rowId == (int)rowData[0]){
                return rowIndex;
            }
        }
        return -1; // rowIndex not found
    }
    
    
    /*
    ** table layout
    */
    //set up table column layout
    protected void setColumnFormat() {
        

        // Center column content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        //LEFT column content
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        //Center column header
        JTableHeader header = table.getTableHeader();
        if (!(header.getDefaultRenderer() instanceof AlignmentTableHeaderCellRenderer)) {
            header.setDefaultRenderer(new AlignmentTableHeaderCellRenderer(header.getDefaultRenderer()));
        }
        
        for (int index = 0; index < table.getColumnCount(); index++) {
            int colWidth = (int) colWidthPercent[index];
            TableColumn column = table.getColumnModel().getColumn(index);

            // notes should fill the remainder of allocated width available
            String columnName = table.getColumnName(index);
            if (columnName.equals("notes")) {
                column.setMinWidth((int) colWidth);
            } else {
                column.setPreferredWidth(colWidth);
                column.setMinWidth(colWidth);
                column.setMaxWidth(colWidth);
            }
            
            if (columnName.equalsIgnoreCase("priority")) {
                // center alignment
                table.getColumnModel().getColumn(index).setCellRenderer(centerRenderer);
            }
            
        }

        

      
    }
    
    protected String convertStreamToString(InputStream is) throws IOException {
        // To convert the InputStream to String we use the
        // Reader.read(char[] buffer) method. We iterate until the
        // Reader return -1 which means there's no more data to
        // read. We use the StringWriter class to produce the string.
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader;
                reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        }
        return "";
    }
    
 
    /**
     * This method subtracts an amount from the totalRecords value This is used
     * when records are deleted to update the totalRecords value
     *
     * @param amountOfRecordsDeleted
     */
    public void subtractFromTotalRowCount(int amountOfRecordsDeleted) {
        totalRecords = totalRecords - amountOfRecordsDeleted;
    }

    /**
     * This method subtracts an amount from the totalRecords value This is used
     * when records are deleted to update the totalRecords value
     *
     * @param amountOfRecordsAdded
     */
    public void addToTotalRowCount(int amountOfRecordsAdded) {
        totalRecords = totalRecords + amountOfRecordsAdded;
    }

    /**
     * This method returns a string that displays the records.
     *
     * @return String This returns a string that has the records for both total
     * and shown
     */
    public String getRecordsLabel() {

        String output;
        
        switch (table.getName()) {
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
                    // post to log.txt
                    LoggingAspect.afterThrown(ex);
                    ex.printStackTrace();
                }
        
                break;
        }
        
        return output;
    }
    
    //get the unique values for search columns
    public Map loadingDropdownList() {

        Map<Integer, ArrayList<Object>> valueListMap = new HashMap();

        for (String searchField : searchFields) {

            for (int i = 0; i < table.getColumnCount(); i++) {
                if (table.getColumnName(i).equalsIgnoreCase(searchField)) {
                    valueListMap.put(i, new ArrayList<Object>());
                }
            }
        }
        for (int col : valueListMap.keySet()) {
            //for each search item, create a new drop down list
            ArrayList DropDownListValueForEachColumn = new ArrayList<Object>();

            String[] columnNames = tableColNames;
            TableModel tableModel = table.getModel();
            String colName = columnNames[col].toLowerCase();

            switch (colName) {
                case "symbol":
                case "notes":
                case "document":
                    DropDownListValueForEachColumn.add("");
                    break;
                default:
                    Object valueAddToDropDownList;
                    for (int row = 0; row < tableModel.getRowCount(); row++) {
                        valueAddToDropDownList = tableModel.getValueAt(row, col);

                        if (valueAddToDropDownList != null) {
                            // add to drop down list
                            DropDownListValueForEachColumn.add(valueAddToDropDownList);
                        } else {
                            DropDownListValueForEachColumn.add("");
                        }
                    }
                    break;
            }

            //make every item in drop down list unique
            Set<Object> uniqueValue = new HashSet<Object>(DropDownListValueForEachColumn);
            ArrayList uniqueList = new ArrayList<Object>(uniqueValue);
//                System.out.println(col + " " + uniqueList);
            valueListMap.put(col, uniqueList);
        }

        return valueListMap;

    }
    
     public void moveSelectedRowsToTheEnd() {
         
        int[] rows = table.getSelectedRows();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rowNum = table.getRowCount();
        int count = 0;
        for (int row : rows) {
            row = row - count;

            model.moveRow(row, row, rowNum - 1);
            count++;
        }
        table.setRowSelectionInterval(rowNum - count, rowNum - 1);
    }
     
    public Object[] getRowData(int rowIndex) {
        int columnCnt = table.getColumnModel().getColumnCount();
        Object[] rowData = new Object[columnCnt];
        
        for(int i = 0; i < columnCnt; i++) {
            rowData[i] = table.getValueAt(rowIndex, i);
        }
        
        return rowData;
    }

    /**
     * ************************************************************************
     ********************** Setters & Getters *********************************
     * ************************************************************************
     */
    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public TableFilter getFilter() {
        return filter;
    }

    public void setFilter(TableFilter filter) {
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
//        System.out.println("table name: " + getTableName() + " row count " +  getTable().getRowCount());
        return getTable().getRowCount();
    }

    public String[] getTableColNames() {
        return tableColNames;
    }

    public void setTableColNames(String[] tableColNames) {
        this.tableColNames = tableColNames;
    }

    public void setTableColNames(JTable table) {
        tableColNames = new String[table.getColumnCount()];
        for (int i = 0; i < table.getColumnCount(); i++) {
            tableColNames[i] = table.getColumnName(i);
        }
    }

    public String[] getSearchFields() {
        return searchFields;
    }

    public void setSearchFields(String[] searchFields) {
        this.searchFields = searchFields;
    }
    
    

    public ColumnPopupMenu getColumnPopupMenu() {
        return columnPopupMenu;
    }

    public void setColumnPopupMenu(ColumnPopupMenu ColumnPopupMenu) {
        this.columnPopupMenu = ColumnPopupMenu;
    }

    public String[] getBatchEditFields() {
        return batchEditFields;
    }

    public void setBatchEditFields(String[] batchEditFields) {
        this.batchEditFields = batchEditFields;
    }

    

    public JTableCellRenderer getCellRenderer() {
        return cellRenderer;
    }

    public void setCellRenderer(JTableCellRenderer cellRenderer) {
        this.cellRenderer = cellRenderer;
    }

    public ModifiedTableData getTableData() {
        return modTableData;
    }

    public void setTableData(ModifiedTableData tableData) {
        this.modTableData = tableData;
    }

    public ButtonsState getState() {
        return state;
    }

    public void setState(ButtonsState state) {
        this.state = state;
    }
    
    





/**
     * CLASS
     */
    class AlignmentTableHeaderCellRenderer implements TableCellRenderer {

        protected final TableCellRenderer wrappedRenderer;
        protected final JLabel label;

        public AlignmentTableHeaderCellRenderer(TableCellRenderer wrappedRenderer) {
            if (!(wrappedRenderer instanceof JLabel)) {
                throw new IllegalArgumentException("The supplied renderer must inherit from JLabel");
            }
            this.wrappedRenderer = wrappedRenderer;
            this.label = (JLabel) wrappedRenderer;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            wrappedRenderer.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);
            

            label.setHorizontalAlignment(column == table.getColumnCount() - 1 ? JLabel.LEFT : JLabel.CENTER);
            return label;

        }

    }

}