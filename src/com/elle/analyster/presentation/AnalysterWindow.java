package com.elle.analyster.presentation;

import java.sql.SQLException;
import java.sql.Statement;
import com.elle.analyster.database.DBConnection;
import com.elle.analyster.logic.ColumnPopupMenu;
import com.elle.analyster.logic.CreateDocumentFilter;
import com.elle.analyster.logic.EditableTableModel;
import com.elle.analyster.logic.ITableConstants;
import com.elle.analyster.logic.FilePathFormat;
import com.elle.analyster.database.ModifiedData;
import com.elle.analyster.database.ModifiedTableData;
import com.elle.analyster.admissions.Authorization;
import static com.elle.analyster.logic.ITableConstants.ARCHIVE_BATCHEDIT_CB_FIELDS;
import static com.elle.analyster.logic.ITableConstants.ARCHIVE_SEARCH_FIELDS;
import static com.elle.analyster.logic.ITableConstants.ARCHIVE_TABLE_NAME;
import static com.elle.analyster.logic.ITableConstants.ASSIGNMENTS_BATCHEDIT_CB_FIELDS;
import static com.elle.analyster.logic.ITableConstants.ASSIGNMENTS_SEARCH_FIELDS;
import static com.elle.analyster.logic.ITableConstants.ASSIGNMENTS_TABLE_NAME;
import static com.elle.analyster.logic.ITableConstants.COL_WIDTH_PER_ARCHIVE;
import static com.elle.analyster.logic.ITableConstants.COL_WIDTH_PER_ASSIGNMENTS;
import static com.elle.analyster.logic.ITableConstants.COL_WIDTH_PER_REPORTS;
import static com.elle.analyster.logic.ITableConstants.REPORTS_BATCHEDIT_CB_FIELDS;
import static com.elle.analyster.logic.ITableConstants.REPORTS_SEARCH_FIELDS;
import static com.elle.analyster.logic.ITableConstants.REPORTS_TABLE_NAME;
import com.elle.analyster.logic.Tab;
import com.elle.analyster.logic.TableFilter;
import com.elle.analyster.logic.JTableCellRenderer;
import com.elle.analyster.logic.LoggingAspect;
import com.elle.analyster.logic.OpenDocumentTool;
import com.elle.analyster.logic.ShortCutSetting;
import com.elle.analyster.logic.TextCellEditor;
import com.elle.analyster.entities.Analyst;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.ListCellRenderer;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
/**
 * AnalysterWindow
 *
 * @author Carlos Igreja
 * @since June 10, 2015
 * @version 0.6.3
 */
public class AnalysterWindow extends JFrame implements ITableConstants {

    public static String creationDate;  // set automatically from manifest
    public static String version;       // set automatically from manifest

    // attributes
    private Map<String, Tab> tabs; // stores individual tab objects 
    private Map<String, Map<Integer, ArrayList<Object>>> comboBoxForSearchDropDown;
    private static Statement statement;
    private String database;

    // components
    private static AnalysterWindow instance;
    private AddRecordsWindow addRecordsWindow;
    private LogWindow logWindow;
    private LoginWindow loginWindow;
    private BatchEditWindow batchEditWindow;
    private EditDatabaseWindow editDatabaseWindow;
    private ReportWindow reportWindow;
    private ShortCutSetting ShortCut;
    private SqlOutputWindow sqlOutputWindow;

    // colors - Edit mode labels
    private Color editModeDefaultTextColor;
    private Color editModeActiveTextColor;

    private String editingTabName; // stores the name of the tab that is editing
    private String searchValue = "";
    public final static String SEPARATOR = "SEPARATOR";

    private boolean isBatchEditWindowShow;
    private boolean comboBoxStartToSearch;
    List<Object> inactiveAnalysts = new ArrayList<Object>();
    
   
    /**
     * CONSTRUCTOR
     */
    public AnalysterWindow() {

        /**
         * Note: initComponents() executes the tabpaneChanged method. Thus, some
         * things need to be before or after the initComponents();
         */
        // the statement is used for sql statements with the database connection
        // the statement is created in LoginWindow and passed to Analyster.
        statement = DBConnection.getStatement();
        database = DBConnection.getDatabase();
        DBConnection.setParentComponent(this);   // show message boxes relative to this component
        instance = this;                         // this is used to call this instance of Analyster 

        // initialize tabs
        tabs = new HashMap();
        comboBoxForSearchDropDown = new HashMap();

        // create tabName objects -> this has to be before initcomponents();
        tabs.put(ASSIGNMENTS_TABLE_NAME, new Tab());
        tabs.put(REPORTS_TABLE_NAME, new Tab());
        tabs.put(ARCHIVE_TABLE_NAME, new Tab());

        // set table names 
        tabs.get(ASSIGNMENTS_TABLE_NAME).setTableName(ASSIGNMENTS_TABLE_NAME);
        tabs.get(REPORTS_TABLE_NAME).setTableName(REPORTS_TABLE_NAME);
        tabs.get(ARCHIVE_TABLE_NAME).setTableName(ARCHIVE_TABLE_NAME);

        // set the search fields for the comboBox for each tabName
        tabs.get(ASSIGNMENTS_TABLE_NAME).setSearchFields(ASSIGNMENTS_SEARCH_FIELDS);
        tabs.get(REPORTS_TABLE_NAME).setSearchFields(REPORTS_SEARCH_FIELDS);
        tabs.get(ARCHIVE_TABLE_NAME).setSearchFields(ARCHIVE_SEARCH_FIELDS);

        // set the search fields for the comboBox for each tabName
        tabs.get(ASSIGNMENTS_TABLE_NAME).setBatchEditFields(ASSIGNMENTS_BATCHEDIT_CB_FIELDS);
        tabs.get(REPORTS_TABLE_NAME).setBatchEditFields(REPORTS_BATCHEDIT_CB_FIELDS);
        tabs.get(ARCHIVE_TABLE_NAME).setBatchEditFields(ARCHIVE_BATCHEDIT_CB_FIELDS);

        // set column width percents to tables of the tabName objects
        tabs.get(ASSIGNMENTS_TABLE_NAME).setColWidthPercent(COL_WIDTH_PER_ASSIGNMENTS);
        tabs.get(REPORTS_TABLE_NAME).setColWidthPercent(COL_WIDTH_PER_REPORTS);
        tabs.get(ARCHIVE_TABLE_NAME).setColWidthPercent(COL_WIDTH_PER_ARCHIVE);

        // set Activate Records menu item enabled for each tabName
        tabs.get(ASSIGNMENTS_TABLE_NAME).setActivateRecordMenuItemEnabled(false);
        tabs.get(REPORTS_TABLE_NAME).setActivateRecordMenuItemEnabled(false);
        tabs.get(ARCHIVE_TABLE_NAME).setActivateRecordMenuItemEnabled(true);

        // set Archive Records menu item enabled for each tabName
        tabs.get(ASSIGNMENTS_TABLE_NAME).setArchiveRecordMenuItemEnabled(true);
        tabs.get(REPORTS_TABLE_NAME).setArchiveRecordMenuItemEnabled(false);
        tabs.get(ARCHIVE_TABLE_NAME).setArchiveRecordMenuItemEnabled(false);

        // set add records button visible for each tabName
        tabs.get(ASSIGNMENTS_TABLE_NAME).setAddRecordsBtnVisible(true);
        tabs.get(REPORTS_TABLE_NAME).setAddRecordsBtnVisible(true);
        tabs.get(ARCHIVE_TABLE_NAME).setAddRecordsBtnVisible(false);

        // set batch edit button visible for each tabName
        tabs.get(ASSIGNMENTS_TABLE_NAME).setBatchEditBtnVisible(true);
        tabs.get(REPORTS_TABLE_NAME).setBatchEditBtnVisible(true);
        tabs.get(ARCHIVE_TABLE_NAME).setBatchEditBtnVisible(false);
        
        initComponents(); // generated code

        // initialize the colors for the edit mode text
        editModeActiveTextColor = new Color(44, 122, 22); //dark green
        editModeDefaultTextColor = labelEditMode.getForeground();

        // set names to tables (this was in tabbedPanelChanged method)
        assignmentTable.setName(ASSIGNMENTS_TABLE_NAME);
        reportTable.setName(REPORTS_TABLE_NAME);
        archiveTable.setName(ARCHIVE_TABLE_NAME);

        // set tables to tabName objects
        tabs.get(ASSIGNMENTS_TABLE_NAME).setTable(assignmentTable);
        tabs.get(REPORTS_TABLE_NAME).setTable(reportTable);
        tabs.get(ARCHIVE_TABLE_NAME).setTable(archiveTable);

        // set array variable of stored column names of the tables
        // this is just to store and use the information
        // to actually change the table names it should be done
        // through properties in the gui design tabName
        tabs.get(ASSIGNMENTS_TABLE_NAME).setTableColNames(assignmentTable);
        tabs.get(REPORTS_TABLE_NAME).setTableColNames(reportTable);
        tabs.get(ARCHIVE_TABLE_NAME).setTableColNames(archiveTable);

        // this sets the KeyboardFocusManger
        setKeyboardFocusManager();

        // show and hide components
        btnUploadChanges.setVisible(false);
        jPanelSQL.setVisible(false);
        btnEnterSQL.setVisible(true);
        btnCancelSQL.setVisible(true);
        btnBatchEdit.setVisible(true);
        jTextAreaSQL.setVisible(true);
        jPanelEdit.setVisible(true);
        btnRevertChanges.setVisible(false);

        // set upload/revert buttons initially disabled
        btnUploadChanges.setEnabled(false);
        btnRevertChanges.setEnabled(false);

        // add filters for each table
        // must be before setting ColumnPopupMenu because this is its parameter
        tabs.get(ASSIGNMENTS_TABLE_NAME).setFilter(new TableFilter(assignmentTable));
        tabs.get(REPORTS_TABLE_NAME).setFilter(new TableFilter(reportTable));
        tabs.get(ARCHIVE_TABLE_NAME).setFilter(new TableFilter(archiveTable));

        // initialize columnPopupMenu 
        // - must be before setTerminalFunctions is called
        // - because the mouslistener is added to the table header
        tabs.get(ASSIGNMENTS_TABLE_NAME)
                .setColumnPopupMenu(new ColumnPopupMenu(tabs.get(ASSIGNMENTS_TABLE_NAME).getFilter()));
        tabs.get(REPORTS_TABLE_NAME)
                .setColumnPopupMenu(new ColumnPopupMenu(tabs.get(REPORTS_TABLE_NAME).getFilter()));
        tabs.get(ARCHIVE_TABLE_NAME)
                .setColumnPopupMenu(new ColumnPopupMenu(tabs.get(ARCHIVE_TABLE_NAME).getFilter()));
        boolean comboBoxStartToSearch = false;
        // load data from database to tables
        loadTables(tabs);

        // set initial record counts of now full tables
        // this should only need to be called once at start up of Analyster.
        // total counts are removed or added in the Tab class
        initTotalRowCounts(tabs);

        // set the cell renderers for each tabName 
        tabs.get(ASSIGNMENTS_TABLE_NAME).setCellRenderer(new JTableCellRenderer(assignmentTable));
        tabs.get(REPORTS_TABLE_NAME).setCellRenderer(new JTableCellRenderer(reportTable));
        tabs.get(ARCHIVE_TABLE_NAME).setCellRenderer(new JTableCellRenderer(archiveTable));

        // set the modified table data objects for each tabName
        tabs.get(ASSIGNMENTS_TABLE_NAME).setTableData(new ModifiedTableData(assignmentTable));
        tabs.get(REPORTS_TABLE_NAME).setTableData(new ModifiedTableData(reportTable));
        tabs.get(ARCHIVE_TABLE_NAME).setTableData(new ModifiedTableData(archiveTable));

        // set all the tabs initially not in editing mode
        tabs.get(ASSIGNMENTS_TABLE_NAME).setEditing(false);
        tabs.get(REPORTS_TABLE_NAME).setEditing(false);
        tabs.get(ARCHIVE_TABLE_NAME).setEditing(false);

        // add copy+paste short cut into table and text Area
        InputMap ip = (InputMap) UIManager.get("TextField.focusInputMap");
        InputMap ip2 = this.jTextAreaSQL.getInputMap();
        ShortCut.copyAndPasteShortCut(ip);
        ShortCut.copyAndPasteShortCut(ip2);

        informationLabel.setText("");
        isBatchEditWindowShow = false;
        String tabName = getSelectedTabName();
       
        String searchContent = comboBoxSearch.getSelectedItem().toString();
        this.updateComboList(searchContent, tabName);
        this.comboBoxValue.setSelectedItem("Enter here");

        // set title of window to Analyster
        this.setTitle("Analyster");
        this.setSize(this.getWidth(), 560);

        setAccessForDeveloper();
        setPanelListeners();

        // authorize user for this component
        Authorization.authorize(this);
        
       inactiveAnalysts = getInactiveAnalysts();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addPanel_control = new javax.swing.JPanel();
        labelTimeLastUpdate = new javax.swing.JLabel();
        searchPanel = new javax.swing.JPanel();
        btnSearch = new javax.swing.JButton();
        comboBoxSearch = new javax.swing.JComboBox();
        btnClearAllFilter = new javax.swing.JButton();
        searchInformationLabel = new javax.swing.JLabel();
        comboBoxValue = new javax.swing.JComboBox();
        labelRecords = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        tabbedPanel = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        assignmentTable = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        reportTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        archiveTable = new javax.swing.JTable();
        jPanelEdit = new javax.swing.JPanel();
        btnBatchEdit = new javax.swing.JButton();
        btnAddRecords = new javax.swing.JButton();
        btnUploadChanges = new javax.swing.JButton();
        labelEditModeState = new javax.swing.JLabel();
        labelEditMode = new javax.swing.JLabel();
        btnRevertChanges = new javax.swing.JButton();
        informationLabel = new javax.swing.JLabel();
        jPanelSQL = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaSQL = new javax.swing.JTextArea();
        btnEnterSQL = new javax.swing.JButton();
        btnCancelSQL = new javax.swing.JButton();
        btnCloseSQL = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuItemVersion = new javax.swing.JMenuItem();
        menuSelectConn = new javax.swing.JMenu();
        menuItemAWSAssign = new javax.swing.JMenuItem();
        menuPrint = new javax.swing.JMenu();
        menuItemPrintGUI = new javax.swing.JMenuItem();
        menuItemPrintDisplay = new javax.swing.JMenuItem();
        menuItemSaveFile = new javax.swing.JMenuItem();
        menuItemLogOff = new javax.swing.JMenuItem();
        menuEdit = new javax.swing.JMenu();
        menuItemManageDBs = new javax.swing.JMenuItem();
        menuItemDeleteRecord = new javax.swing.JMenuItem();
        menuItemArchiveRecord = new javax.swing.JMenuItem();
        menuItemActivateRecord = new javax.swing.JMenuItem();
        menuFind = new javax.swing.JMenu();
        menuReports = new javax.swing.JMenu();
        menuItemOpenDocument = new javax.swing.JMenuItem();
        menuView = new javax.swing.JMenu();
        menuItemLogChkBx = new javax.swing.JCheckBoxMenuItem();
        menuItemSQLCmdChkBx = new javax.swing.JCheckBoxMenuItem();
        menuItemViewAssign = new javax.swing.JMenuItem();
        menuItemViewReports = new javax.swing.JMenuItem();
        menuItemViewAllAssign = new javax.swing.JMenuItem();
        menuItemViewActiveAssign = new javax.swing.JMenuItem();
        menuTools = new javax.swing.JMenu();
        menuItemReloadData = new javax.swing.JMenuItem();
        menuItemTurnEditModeOff = new javax.swing.JMenuItem();
        menuItemBackup = new javax.swing.JMenuItem();
        menuItemAddslash = new javax.swing.JMenuItem();
        menuItemStripslash = new javax.swing.JMenuItem();
        menuHelp = new javax.swing.JMenu();
        menuItemRepBugSugg = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        labelTimeLastUpdate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelTimeLastUpdate.setText("Last updated: ");
        labelTimeLastUpdate.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        comboBoxSearch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "symbol", "analyst" }));
        comboBoxSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxSearchActionPerformed(evt);
            }
        });

        btnClearAllFilter.setText("Clear All Filters");
        btnClearAllFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearAllFilterActionPerformed(evt);
            }
        });

        comboBoxValue.setEditable(true);
        comboBoxValue.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxValue.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        comboBoxValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxValueActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnClearAllFilter)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboBoxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(searchPanelLayout.createSequentialGroup()
                        .addComponent(comboBoxValue, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(370, 370, 370)
                        .addComponent(btnSearch)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(searchPanelLayout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(searchInformationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        searchPanelLayout.setVerticalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSearch)
                    .addComponent(comboBoxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClearAllFilter)
                    .addComponent(comboBoxValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(searchInformationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE)
                .addContainerGap())
        );

        labelRecords.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelRecords.setText("labelRecords");

        javax.swing.GroupLayout addPanel_controlLayout = new javax.swing.GroupLayout(addPanel_control);
        addPanel_control.setLayout(addPanel_controlLayout);
        addPanel_controlLayout.setHorizontalGroup(
            addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addPanel_controlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addPanel_controlLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addPanel_controlLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(labelTimeLastUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        addPanel_controlLayout.setVerticalGroup(
            addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addPanel_controlLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addPanel_controlLayout.createSequentialGroup()
                        .addComponent(labelRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelTimeLastUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tabbedPanel.setPreferredSize(new java.awt.Dimension(800, 584));
        tabbedPanel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbedPanelStateChanged(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        assignmentTable.setAutoCreateRowSorter(true);
        assignmentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "symbol", "analyst", "priority", "dateAssigned", "dateDone", "notes"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        assignmentTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        assignmentTable.setMinimumSize(new java.awt.Dimension(10, 240));
        assignmentTable.setName(""); // NOI18N
        assignmentTable.setRequestFocusEnabled(false);
        jScrollPane1.setViewportView(assignmentTable);

        tabbedPanel.addTab("Assignments", jScrollPane1);

        reportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, "", null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "symbol", "analyst", "analysisDate", "path", "document", "decision", "notes"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        reportTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        reportTable.setMinimumSize(new java.awt.Dimension(10, 240));
        jScrollPane4.setViewportView(reportTable);

        tabbedPanel.addTab("Reports", jScrollPane4);

        archiveTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "dateArchived", "aID", "symbol", "analyst", "priority", "dateAssigned", "dateDone", "notes"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        archiveTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        archiveTable.setAutoscrolls(false);
        archiveTable.setMinimumSize(new java.awt.Dimension(10, 240));
        jScrollPane3.setViewportView(archiveTable);

        tabbedPanel.addTab("Assignments_Archived", jScrollPane3);

        jPanelEdit.setPreferredSize(new java.awt.Dimension(636, 180));

        btnBatchEdit.setText("Batch Edit");
        btnBatchEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatchEditActionPerformed(evt);
            }
        });

        btnAddRecords.setText("Add Record(s)");
        btnAddRecords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRecordsActionPerformed(evt);
            }
        });

        btnUploadChanges.setText("Upload Changes");
        btnUploadChanges.setMaximumSize(new java.awt.Dimension(95, 30));
        btnUploadChanges.setMinimumSize(new java.awt.Dimension(95, 30));
        btnUploadChanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadChangesActionPerformed(evt);
            }
        });

        labelEditModeState.setText("OFF");
        labelEditModeState.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelEditModeStateMouseClicked(evt);
            }
        });

        labelEditMode.setText("Edit Mode:");

        btnRevertChanges.setText("Revert Changes");
        btnRevertChanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRevertChangesActionPerformed(evt);
            }
        });

        informationLabel.setText("jLabel2");

        javax.swing.GroupLayout jPanelEditLayout = new javax.swing.GroupLayout(jPanelEdit);
        jPanelEdit.setLayout(jPanelEditLayout);
        jPanelEditLayout.setHorizontalGroup(
            jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEditLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelEditMode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelEditModeState)
                .addGap(179, 179, 179)
                .addGroup(jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelEditLayout.createSequentialGroup()
                        .addComponent(btnUploadChanges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRevertChanges)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAddRecords)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBatchEdit)
                        .addGap(26, 26, 26))
                    .addGroup(jPanelEditLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(informationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanelEditLayout.setVerticalGroup(
            jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEditLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUploadChanges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelEditMode)
                    .addComponent(labelEditModeState)
                    .addComponent(btnBatchEdit)
                    .addComponent(btnAddRecords)
                    .addComponent(btnRevertChanges))
                .addGap(0, 0, 0)
                .addComponent(informationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jScrollPane2.setBorder(null);

        jTextAreaSQL.setBackground(new java.awt.Color(0, 153, 102));
        jTextAreaSQL.setColumns(20);
        jTextAreaSQL.setLineWrap(true);
        jTextAreaSQL.setRows(5);
        jTextAreaSQL.setText("Please input an SQL statement:\\n>>");
        ((AbstractDocument) jTextAreaSQL.getDocument())
        .setDocumentFilter(new CreateDocumentFilter(33));
        jTextAreaSQL.setWrapStyleWord(true);
        jTextAreaSQL.setMaximumSize(new java.awt.Dimension(1590, 150));
        jTextAreaSQL.setMinimumSize(new java.awt.Dimension(1590, 150));
        jScrollPane2.setViewportView(jTextAreaSQL);

        btnEnterSQL.setText("Enter");
        btnEnterSQL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnterSQLActionPerformed(evt);
            }
        });

        btnCancelSQL.setText("Cancel");
        btnCancelSQL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelSQLActionPerformed(evt);
            }
        });

        btnCloseSQL.setText("Close");
        btnCloseSQL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseSQLActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelSQLLayout = new javax.swing.GroupLayout(jPanelSQL);
        jPanelSQL.setLayout(jPanelSQLLayout);
        jPanelSQLLayout.setHorizontalGroup(
            jPanelSQLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSQLLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanelSQLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCancelSQL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEnterSQL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCloseSQL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(4, 4, 4)
                .addComponent(jScrollPane2))
        );
        jPanelSQLLayout.setVerticalGroup(
            jPanelSQLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSQLLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanelSQLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanelSQLLayout.createSequentialGroup()
                        .addComponent(btnEnterSQL)
                        .addGap(4, 4, 4)
                        .addComponent(btnCancelSQL)
                        .addGap(4, 4, 4)
                        .addComponent(btnCloseSQL)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelEdit, javax.swing.GroupLayout.DEFAULT_SIZE, 1088, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanelSQL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(4, 4, 4))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(tabbedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanelEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanelSQL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tabbedPanel.getAccessibleContext().setAccessibleName("Reports");
        tabbedPanel.getAccessibleContext().setAccessibleParent(tabbedPanel);

        menuFile.setText("File");

        menuItemVersion.setText("Version");
        menuItemVersion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemVersionActionPerformed(evt);
            }
        });
        menuFile.add(menuItemVersion);

        menuSelectConn.setText("Select Connection");

        menuItemAWSAssign.setText("AWS Assignments");
        menuItemAWSAssign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAWSAssignActionPerformed(evt);
            }
        });
        menuSelectConn.add(menuItemAWSAssign);

        menuFile.add(menuSelectConn);

        menuPrint.setText("Print");

        menuItemPrintGUI.setText("Print GUI");
        menuPrint.add(menuItemPrintGUI);

        menuItemPrintDisplay.setText("Print Display Window");
        menuPrint.add(menuItemPrintDisplay);

        menuFile.add(menuPrint);

        menuItemSaveFile.setText("Save File");
        menuFile.add(menuItemSaveFile);

        menuItemLogOff.setText("Log out");
        menuItemLogOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemLogOffActionPerformed(evt);
            }
        });
        menuFile.add(menuItemLogOff);

        menuBar.add(menuFile);

        menuEdit.setText("Edit");

        menuItemManageDBs.setText("Manage databases");
        menuItemManageDBs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemManageDBsActionPerformed(evt);
            }
        });
        menuEdit.add(menuItemManageDBs);

        menuItemDeleteRecord.setText("Delete Records");
        menuItemDeleteRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemDeleteRecordActionPerformed(evt);
            }
        });
        menuEdit.add(menuItemDeleteRecord);

        menuItemArchiveRecord.setText("Copy to Archive");
        menuItemArchiveRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemArchiveRecordActionPerformed(evt);
            }
        });
        menuEdit.add(menuItemArchiveRecord);

        menuItemActivateRecord.setText("Activate Record");
        menuItemActivateRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemActivateRecordActionPerformed(evt);
            }
        });
        menuEdit.add(menuItemActivateRecord);

        menuBar.add(menuEdit);

        menuFind.setText("Find");
        menuBar.add(menuFind);

        menuReports.setText("Reports");

        menuItemOpenDocument.setText("Open Document");
        menuItemOpenDocument.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemOpenDocumentActionPerformed(evt);
            }
        });
        menuReports.add(menuItemOpenDocument);

        menuBar.add(menuReports);

        menuView.setText("View");

        menuItemLogChkBx.setText("Log");
        menuItemLogChkBx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemLogChkBxActionPerformed(evt);
            }
        });
        menuView.add(menuItemLogChkBx);

        menuItemSQLCmdChkBx.setText("SQL Command");
        menuItemSQLCmdChkBx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemSQLCmdChkBxActionPerformed(evt);
            }
        });
        menuView.add(menuItemSQLCmdChkBx);

        menuItemViewAssign.setText("View Assignments Columns");
        menuItemViewAssign.setEnabled(false);
        menuView.add(menuItemViewAssign);

        menuItemViewReports.setText("View Reports Columns");
        menuItemViewReports.setEnabled(false);
        menuView.add(menuItemViewReports);

        menuItemViewAllAssign.setText("View All Assignments");
        menuItemViewAllAssign.setEnabled(false);
        menuItemViewAllAssign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemViewAllAssignActionPerformed(evt);
            }
        });
        menuView.add(menuItemViewAllAssign);

        menuItemViewActiveAssign.setText("View Active Assigments");
        menuItemViewActiveAssign.setEnabled(false);
        menuItemViewActiveAssign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemViewActiveAssignActionPerformed(evt);
            }
        });
        menuView.add(menuItemViewActiveAssign);

        menuBar.add(menuView);

        menuTools.setText("Tools");

        menuItemReloadData.setText("Reload data");
        menuItemReloadData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemReloadDataActionPerformed(evt);
            }
        });
        menuTools.add(menuItemReloadData);

        menuItemTurnEditModeOff.setText("Turn Edit Mode OFF");
        menuItemTurnEditModeOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemTurnEditModeOffActionPerformed(evt);
            }
        });
        menuTools.add(menuItemTurnEditModeOff);

        menuItemBackup.setText("Backup Tables");
        menuItemBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemBackupActionPerformed(evt);
            }
        });
        menuTools.add(menuItemBackup);

        menuItemAddslash.setText("Add / to path");
        menuItemAddslash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAddslashActionPerformed(evt);
            }
        });
        menuTools.add(menuItemAddslash);

        menuItemStripslash.setText("Strip / from path");
        menuItemStripslash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemStripslashActionPerformed(evt);
            }
        });
        menuTools.add(menuItemStripslash);

        menuBar.add(menuTools);

        menuHelp.setText("Help");

        menuItemRepBugSugg.setText("Report a bug/suggestion");
        menuHelp.add(menuItemRepBugSugg);

        menuBar.add(menuHelp);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addPanel_control, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(addPanel_control, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuItemVersionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemVersionActionPerformed

        JOptionPane.showMessageDialog(this, "Creation Date: "
                + creationDate + "\n"
                + "Version: " + version);
    }//GEN-LAST:event_menuItemVersionActionPerformed

    /**
     * This method is called when the search button is pressed
     *
     * @param evt
     */
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        filterBySearch();
    }//GEN-LAST:event_btnSearchActionPerformed

    /**
     * This method is performed when the text field is used to search by either
     * clicking the search button or the Enter key in the text field. This
     * method is called by the searchActionPerformed method and the
     * textForSearchKeyPressed method
     */
    public void filterBySearch() {

        String text = "";

        int count = 0;
        for (Map.Entry<String, Tab> entry : tabs.entrySet()) {
            Tab tab = tabs.get(entry.getKey());
            JTable table = tab.getTable();
            TableModel tableModel = table.getModel();

            String searchColName = comboBoxSearch.getSelectedItem().toString();
            String searchBoxValue = comboBoxValue.getSelectedItem().toString();  // store string from combobox

            // this matches the combobox newValue with the column name newValue to get the column index
            for (int col = 0; col < table.getColumnCount(); col++) {
                String tableColName = tableModel.getColumnName(col);
                if (tableColName.equalsIgnoreCase(searchColName)) {

                    // add item to filter
                    TableFilter filter = tab.getFilter();
                    filter.clearAllFilters();
                    filter.applyFilter();

                    boolean isValueInTable = false;

                    isValueInTable = checkValueInTableCell(col, searchBoxValue, tableModel);
                    filter.addFilterItem(col, searchBoxValue);
                    filter.applyFilter();

                    if (isValueInTable == false) {
                        count++;
                    }

                    // set label record information
                    String recordsLabel = tab.getRecordsLabel();
                    labelRecords.setText(recordsLabel);
                }
            }
            if (count == 3) {
                text = "There is no " + searchBoxValue
                        + " under " + searchColName + " in all tables";
            }
            System.out.println(count);
        }
        if (!text.equals("")) {
            searchInformationLabel.setText(text);
            startCountDownFromNow(10);
        }
    }

    private boolean checkValueInTableCell(int col, String target, TableModel tableModel) {
        //   System.out.println("target is : " + target + " at column " + col);
        int count = 0;

        for (int row = 0; row < tableModel.getRowCount(); row++) {
            String cellValue = "";
            if (tableModel.getValueAt(row, col) != null) {
                cellValue = tableModel.getValueAt(row, col).toString();
            }

            if (cellValue.equalsIgnoreCase(target)) {
                count++;
                System.out.println("found " + target);
            }
        }
        if (count > 0) {
            System.out.println(">0");
            return true;
        } else {
            System.out.println("=0");
            return false;
        }
    }

    // not sure what this is
    private void menuItemAWSAssignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAWSAssignActionPerformed

        loadTable(assignmentTable);
        loadTable(reportTable);


    }//GEN-LAST:event_menuItemAWSAssignActionPerformed

    private void btnUploadChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadChangesActionPerformed

        uploadChanges();
    }//GEN-LAST:event_btnUploadChangesActionPerformed

    /**
     * This uploads changes made by editing and saves the changes by uploading
     * them to the database. This method is called by:
     * btnUploadChangesActionPerformed(java.awt.event.ActionEvent evt) and also
     * a keylistener when editing mode is on and enter is pressed
     */
    public void uploadChanges() {

        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        JTable table = tab.getTable();
        JTableCellRenderer cellRenderer = tab.getCellRenderer();
        ModifiedTableData data = tab.getTableData();

        updateTable(table, data.getNewData());

        loadTable(table); // refresh table

        // clear cellrenderer
        cellRenderer.clearCellRender();

        // reload modified table data with current table model
        data.reloadData();

        data.getNewData().clear();    // reset the arraylist to record future changes
        setLastUpdateTime();          // update time
        makeTableEditable(false);

        // no changes to upload or revert		
        setEnabledEditingButtons(false, false);
        String text = "Edits uploaded successfully!";
        setInformationLabel(text, 5);
        logWindow.addMessageWithDate(text);
        System.out.println(text);

        // no changes to upload or revert
        setEnabledEditingButtons(false, false);
    }

    private void btnEnterSQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnterSQLActionPerformed

        int commandStart = jTextAreaSQL.getText().lastIndexOf(">>") + 2;
        String command = jTextAreaSQL.getText().substring(commandStart);
        if(sqlOutputWindow == null){
            sqlOutputWindow = new SqlOutputWindow(command,this); 
        }
        else{
            sqlOutputWindow.setLocationRelativeTo(this);
            sqlOutputWindow.toFront();
            sqlOutputWindow.setTableModel(command);
            sqlOutputWindow.setVisible(true);
        }
    }//GEN-LAST:event_btnEnterSQLActionPerformed

    /**
     * btnCancelSQLActionPerformed
     *
     * @param evt
     */
    private void btnCancelSQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelSQLActionPerformed
        ((AbstractDocument) jTextAreaSQL.getDocument())
                .setDocumentFilter(new CreateDocumentFilter(0));
        jTextAreaSQL.setText("Please input an SQL statement:\n>>");
        ((AbstractDocument) jTextAreaSQL.getDocument())
                .setDocumentFilter(new CreateDocumentFilter(33));
    }//GEN-LAST:event_btnCancelSQLActionPerformed

    private void btnCloseSQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseSQLActionPerformed

        jPanelSQL.setVisible(false);
        menuItemSQLCmdChkBx.setSelected(false);
    }//GEN-LAST:event_btnCloseSQLActionPerformed

    /**
     * makeTableEditable Make tables editable or non editable
     *
     * @param makeTableEditable // takes boolean true or false to make editable
     */
    public void makeTableEditable(boolean makeTableEditable) {

        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        boolean isAddRecordsBtnVisible = tab.isAddRecordsBtnVisible();
        boolean isBatchEditBtnVisible = tab.isBatchEditBtnVisible();

        if (makeTableEditable) {
            tab.setEditing(true);
            labelEditModeState.setText("ON ");
            btnUploadChanges.setVisible(true);
            btnAddRecords.setVisible(false);
            btnBatchEdit.setVisible(true);
            btnRevertChanges.setVisible(true);
        } else {
            tab.setEditing(false);
            labelEditModeState.setText("OFF");
            btnUploadChanges.setVisible(false);
            btnAddRecords.setVisible(isAddRecordsBtnVisible);
            btnBatchEdit.setVisible(isBatchEditBtnVisible);
            btnRevertChanges.setVisible(false);
        }
        editModeTextColor(tab.isEditing());

        for (Map.Entry<String, Tab> entry : tabs.entrySet()) {
            tab = tabs.get(entry.getKey());
            JTable table = tab.getTable();
            EditableTableModel model = ((EditableTableModel) table.getModel());
            model.setCellEditable(makeTableEditable);

        }
    }

    /**
     * changeTabbedPanelState
     */
    private void changeTabbedPanelState() {

        // get selected tab
        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);

        this.menuItemStripslash.setEnabled(false);
        this.menuItemAddslash.setEnabled(false);

        if (tabName.equals("Reports")) {
            this.menuItemStripslash.setEnabled(true);
            this.menuItemAddslash.setEnabled(true);
            menuItemOpenDocument.setEnabled(true);
        } else {
            menuItemOpenDocument.setEnabled(false);
        }

        // get booleans for the states of the selected tab
        boolean isActivateRecordMenuItemEnabled = tab.isActivateRecordMenuItemEnabled();
        boolean isArchiveRecordMenuItemEnabled = tab.isArchiveRecordMenuItemEnabled();
        boolean isBatchEditBtnEnabled = tab.isBatchEditBtnEnabled();
        boolean isBatchEditWindowOpen = tab.isBatchEditWindowOpen();
        boolean isBatchEditWindowVisible = tab.isBatchEditWindowVisible();

        // this enables or disables the menu components for this tabName
        menuItemActivateRecord.setEnabled(isActivateRecordMenuItemEnabled);
        menuItemArchiveRecord.setEnabled(isArchiveRecordMenuItemEnabled);

        // batch edit button enabled is only allowed for table that is editing
        btnBatchEdit.setEnabled(isBatchEditBtnEnabled);
        if (isBatchEditWindowOpen) {
            batchEditWindow.setVisible(isBatchEditWindowVisible);
        }

        // check whether editing and display accordingly
        boolean editing = tab.isEditing();

        // must be instance of EditableTableModel 
        // this method is called from init componenents before the table model is set
        JTable table = tab.getTable();
        if (table.getModel() instanceof EditableTableModel) {
            makeTableEditable(editing);
        }

        // set the color of the edit mode text
        editModeTextColor(tab.isEditing());

        // set label record information
        String recordsLabel = tab.getRecordsLabel();
        labelRecords.setText(recordsLabel);

        // buttons if in edit mode
        if (labelEditModeState.getText().equals("ON ")) {
            btnAddRecords.setVisible(false);
            btnBatchEdit.setVisible(true);
        }

        // batch edit window visible only on the editing tab
        if (batchEditWindow != null) {
            boolean batchWindowVisible = tab.isBatchEditWindowVisible();
            batchEditWindow.setVisible(batchWindowVisible);
        }

        // if this tab is editing
        if (editing) {

            // if there is no modified data
            if (tab.getTableData().getNewData().isEmpty()) {
                setEnabledEditingButtons(false, false);
            } // there is modified data to upload or revert
            else {
                setEnabledEditingButtons(true, true);
            }

            // set edit mode label
            labelEditMode.setText("Edit Mode: ");
            labelEditModeState.setVisible(true);
            editModeTextColor(true);
        } // else if no tab is editing
        else if (!isTabEditing()) {
            btnAddRecords.setEnabled(true);
            btnBatchEdit.setEnabled(true);

            // set edit mode label
            labelEditMode.setText("Edit Mode: ");
            labelEditModeState.setVisible(true);

            editModeTextColor(false);
        } // else if there is a tab editing but it is not this one
        else if (isTabEditing()) {
            btnAddRecords.setEnabled(false);
            btnBatchEdit.setEnabled(false);

            // set edit mode label
            labelEditMode.setText("Editing " + getEditingTabName() + " ... ");
            labelEditModeState.setVisible(false);
            editModeTextColor(true);
        }

        // authorize user
        Authorization.authorize(this);
    }

    private void btnBatchEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatchEditActionPerformed

        // get selected tab
        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);

        // set the tab to editing
        tab.setEditing(true);
        makeTableEditable(true);

        // set the color of the edit mode text
        editModeTextColor(tab.isEditing());

        // open a batch edit window and make visible only to this tab
        batchEditWindow = new BatchEditWindow();
        batchEditWindow.setVisible(true);
        batchEditWindow.toFront();
        batchEditWindow.requestFocus();
        this.isBatchEditWindowShow = true;
        tab.setBatchEditWindowVisible(true);
        tab.setBatchEditWindowOpen(true);
        tab.setBatchEditBtnEnabled(false);
        setBatchEditButtonStates(tab);

        // show the batch edit window in front of the Main Window
        showWindowInFront(batchEditWindow);

    }//GEN-LAST:event_btnBatchEditActionPerformed

    private void menuItemManageDBsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemManageDBsActionPerformed
        editDatabaseWindow = new EditDatabaseWindow();
        editDatabaseWindow.setLocationRelativeTo(this);
        editDatabaseWindow.setVisible(true);
    }//GEN-LAST:event_menuItemManageDBsActionPerformed

    /**
     * btnAddRecordsActionPerformed
     *
     * @param evt
     */
    private void btnAddRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRecordsActionPerformed

        // if no add records window is open
        if (addRecordsWindow == null || !addRecordsWindow.isDisplayable()) {
            addRecordsWindow = new AddRecordsWindow();
            addRecordsWindow.setVisible(true);
        } // if window is already open then set the focus
        else {
            addRecordsWindow.toFront();
        }

        // update records
        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        String recordsLabel = tab.getRecordsLabel();
        labelRecords.setText(recordsLabel);

    }//GEN-LAST:event_btnAddRecordsActionPerformed

    /**
     * This method listens if the enter key was pressed in the search text box.
     * This allows the newValue to be entered without having to click the search
     * button.
     *
     * @param evt
     */
    /**
     * jMenuItemLogOffActionPerformed Log Off menu item action performed
     *
     * @param evt
     */
    private void menuItemLogOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemLogOffActionPerformed
        Object[] options = {"Reconnect", "Log Out"};  // the titles of buttons

        int n = JOptionPane.showOptionDialog(this, "Would you like to reconnect?", "Log off",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]); //default button title

        switch (n) {
            case 0: {               // Reconnect

                // create a new Login Window
                loginWindow = new LoginWindow();
                loginWindow.setLocationRelativeTo(this);
                loginWindow.setVisible(true);

                // dispose of this Object and return resources
                this.dispose();

                break;
            }
            case 1:
                System.exit(0); // Quit
        }
    }//GEN-LAST:event_menuItemLogOffActionPerformed

    /**
     * menuItemDeleteRecordActionPerformed Delete records menu item action
     * performed
     *
     * @param evt
     */
    private void menuItemDeleteRecordActionPerformed(java.awt.event.ActionEvent evt) {

        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        JTable table = tab.getTable();
        String sqlDelete = deleteRecordsSelected(table);
        logWindow.addMessageWithDate(sqlDelete);
    }

    /**
     * jMenuItemViewAllAssigActionPerformed calls load data method
     *
     * @param evt
     */
    private void menuItemViewAllAssignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemViewAllAssignActionPerformed
        loadData();
    }//GEN-LAST:event_menuItemViewAllAssignActionPerformed

    /**
     * jMenuItemViewActiveAssigActionPerformed load only active data from
     * analyst
     *
     * @param evt
     */
    private void menuItemViewActiveAssignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemViewActiveAssignActionPerformed

        String sqlC = "select A.* from Assignments A left join t_analysts T\n" + "on A.analyst = T.analyst\n" + "where T.active = 1\n" + "order by A.symbol";
        loadTable(sqlC, assignmentTable);

        String tabName = ASSIGNMENTS_TABLE_NAME;
        Tab tab = tabs.get(tabName);
        float[] colWidthPercent = tab.getColWidthPercent();
        JTable table = tab.getTable();
        setColumnFormat(colWidthPercent, table);

        // set label record information
        String recordsLabel = tab.getRecordsLabel();
        labelRecords.setText(recordsLabel);
    }//GEN-LAST:event_menuItemViewActiveAssignActionPerformed

    /**
     * btnClearAllFilterActionPerformed clear all filters
     *
     * @param evt
     */
    private void btnClearAllFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearAllFilterActionPerformed

        String recordsLabel = "";
        // clear all filters
        //      String tabName = getSelectedTabName();
        for (Map.Entry<String, Tab> entry : tabs.entrySet()) {
            Tab tab = tabs.get(entry.getKey());
            TableFilter filter = tab.getFilter();
            filter.clearAllFilters();
            filter.applyFilter();
            filter.applyColorHeaders();
            recordsLabel = recordsLabel + tab.getRecordsLabel() + " \n";

        }

        labelRecords.setText(recordsLabel);
        System.out.println(recordsLabel);
    }//GEN-LAST:event_btnClearAllFilterActionPerformed

    /**
     * jMenuItemOthersLoadDataActionPerformed
     *
     * @param evt
     */
    private void menuItemReloadDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemReloadDataActionPerformed

        reloadDataAction();
        String text = "Data reloaded!";
        setInformationLabel(text, 5);
        logWindow.addMessageWithDate(text);
        System.out.println(text);
    }//GEN-LAST:event_menuItemReloadDataActionPerformed

    // reload the data
    private void reloadDataAction() {
        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        JTableCellRenderer cellRenderer = tab.getCellRenderer();
        ModifiedTableData data = tab.getTableData();

        // reload table from database
        JTable table = tab.getTable();
        loadTable(table);

        // clear cellrenderer
        cellRenderer.clearCellRender();

        // reload modified table data with current table model
        data.reloadData();
        // reload modified table data into dropdown list
        loadData();

        // set label record information
        String recordsLabel = tab.getRecordsLabel();
        labelRecords.setText(recordsLabel);

    }

    /**
     * jArchiveRecordActionPerformed
     *
     * @param evt
     */
    private void menuItemArchiveRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemArchiveRecordActionPerformed

        // get selected rows from the assignments table
        int[] selectedRows = assignmentTable.getSelectedRows(); // array of the rows selected
        int rowCount = selectedRows.length;                     // the number of rows selected
        Object cellValue = null;                                // store cell value
        String insertInto = "";                                 // store insert sql statement
        String values = "";                                     // store values sql statement
        boolean errorOccurred = false;                          // boolean gate for dialog box 

        // initialize the dateArchived with todays date that is used for every inserted record
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date todaysDate = new Date();
        String dateArchived = dateFormat.format(todaysDate);  // dateArchived is todays date

        // no records are selected conditional
        if (rowCount != -1) {
            // create the insert statement for the assignments archived table
            // insert statement for the assignments archived table
            insertInto = "INSERT INTO " + archiveTable.getName() + " (";

            // do not include the primary key
            for (int col = 1; col < archiveTable.getColumnCount(); col++) {
                if (col != archiveTable.getColumnCount() - 1) {
                    insertInto += archiveTable.getColumnName(col) + ", ";
                } else {
                    insertInto += archiveTable.getColumnName(col) + ") ";
                }
            }

            //get all field data from the assignments table
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                int row = selectedRows[rowIndex];

                // create the values string to be inserted in the insert statement
                values = "VALUES ('" + dateArchived + "', ";  // start the values statement
                for (int col = 0; col < assignmentTable.getColumnCount(); col++) {

                    // get cell value
                    cellValue = assignmentTable.getValueAt(row, col);

                    // format the cell value for sql
                    if (cellValue != null) {

                        // if cell is empty it must be null
                        if (cellValue.toString().equals("")) {
                            cellValue = null;
                        } // if the cell is not empty it must have single quotes
                        else {
                            cellValue = "'" + cellValue + "'";
                        }
                    }

                    // add each value for each column to the values statement
                    if (col != assignmentTable.getColumnCount() - 1) {
                        values += cellValue + ", ";
                    } else {
                        values += cellValue + ");";
                    }
                }

                try {
                    // execute the sql statement
                    if (!values.equals("VALUES (")) {      //skip if nothing was added
                        DBConnection.close();
                        DBConnection.open();
                        statement = DBConnection.getStatement();
                        statement.executeUpdate(insertInto + values);
                    }
                } catch (SQLException sqlException) {
                    LoggingAspect.afterThrown(sqlException);
                    break; // break because error occurred
                }
            }

            // if no error occured then display the amount of records archived dialog box
            if (!errorOccurred) {
                String text = rowCount + " record(s) archived!";
                this.setInformationLabel(text, 5);
                logWindow.addMessageWithDate(text);
                System.out.println(text);

                // load the assignments archived table to refresh with new data
                loadTable(archiveTable);

                // update records shown for archive table tab
                Tab archiveTab = tabs.get(ARCHIVE_TABLE_NAME);
                archiveTab.addToTotalRowCount(rowCount);

            }

        } else {
            // no records are selected information to user
            String text = "No records are selected in assignments";
            this.setInformationLabel(text, 5);
            logWindow.addMessageWithDate(text);
            System.out.println(text);
        }

    }//GEN-LAST:event_menuItemArchiveRecordActionPerformed

    /**
     * tabbedPanelStateChanged
     *
     * @param evt
     */
    private void tabbedPanelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbedPanelStateChanged

        changeTabbedPanelState();

        // this changes the search fields for the comboBox for each tabName
        // this event is fired from initCompnents hence the null condition
        String entryValue = comboBoxValue.getSelectedItem().toString();
        String searchCol = comboBoxSearch.getSelectedItem().toString();
        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        String[] searchFields = tab.getSearchFields();
        System.out.println(tab.getTableName());
        if (searchFields != null) {
            comboBoxSearch.setModel(new DefaultComboBoxModel(searchFields));
//           if(tab.getTableName().equalsIgnoreCase("Assignments")){
//           comboBoxSearch.setModel(new DefaultComboBoxModel(ASSIGNMENTS_SEARCH_FIELDS));
//           }else if(tab.getTableName().equalsIgnoreCase("Reports")){
//           comboBoxSearch.setModel(new DefaultComboBoxModel(REPORTS_SEARCH_FIELDS));    
//           }else if(tab.getTableName().equalsIgnoreCase("Assignments_Archived")){
//           comboBoxSearch.setModel(new DefaultComboBoxModel(ARCHIVE_SEARCH_FIELDS));    
//           }
        }
        comboBoxSearch.setSelectedItem(searchCol);
        updateComboList(searchCol, tabName);
        comboBoxValue.setSelectedItem(entryValue);
    }//GEN-LAST:event_tabbedPanelStateChanged

    /**
     * jCheckBoxMenuItemViewLogActionPerformed
     *
     * @param evt
     */
    private void menuItemLogChkBxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemLogChkBxActionPerformed

        if (menuItemLogChkBx.isSelected()) {

            logWindow.setLocationRelativeTo(this);
            logWindow.setVisible(true); // show log window

            // sets the location of the Log Window to the top right corner
            Rectangle rect = this.getBounds();
            int x = (int) rect.getMaxX() - this.logWindow.getWidth();
            int y = (int) rect.getMaxY() - this.logWindow.getHeight();
            this.logWindow.setLocation(x, y);

            // remove check if window is closed from the window
            logWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    menuItemLogChkBx.setSelected(false);
                }
            });
        } else {
            // hide log window
            logWindow.setVisible(false);
        }


    }//GEN-LAST:event_menuItemLogChkBxActionPerformed

    /**
     * jCheckBoxMenuItemViewSQLActionPerformed
     *
     * @param evt
     */
    private void menuItemSQLCmdChkBxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemSQLCmdChkBxActionPerformed

        /**
         * ************* Strange behavior ************************* The
         * jPanelSQL.getHeight() is the height before the
         * jCheckBoxMenuItemViewSQLActionPerformed method was called.
         *
         * The jPanelSQL.setVisible() does not change the size of the sql panel
         * after it is executed.
         *
         * The jPanel size will only change after the
         * jCheckBoxMenuItemViewSQLActionPerformed is finished.
         *
         * That is why the the actual integer is used rather than getHeight().
         *
         * Example: jPanelSQL.setVisible(true); jPanelSQL.getHeight(); // this
         * returns 0
         */
        if (menuItemSQLCmdChkBx.isSelected()) {

            // show sql panel
            jPanelSQL.setVisible(true);
            this.setSize(this.getWidth(), 560 + 112);

        } else {

            // hide sql panel
            jPanelSQL.setVisible(false);
            this.setSize(this.getWidth(), 560);
        }
    }//GEN-LAST:event_menuItemSQLCmdChkBxActionPerformed

    private void btnRevertChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRevertChangesActionPerformed

        revertChanges();
    }//GEN-LAST:event_btnRevertChangesActionPerformed

    /**
     * menuItemBackupActionPerformed
     *
     * @param evt This is the menu item backup that is used to back up the
     * database table.
     */
    private void menuItemBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemBackupActionPerformed

        // open new connection
        DBConnection.close(); // connection might be timed out on server
        if (DBConnection.open()) {  // open a new connection
            BackupDBTablesDialog backupDBTables = new BackupDBTablesDialog(this);
        } else {
            JOptionPane.showMessageDialog(this, "Could not connect to Database");
        }


    }//GEN-LAST:event_menuItemBackupActionPerformed

    private void menuItemTurnEditModeOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemTurnEditModeOffActionPerformed
        makeTableEditable(false);
        reloadDataAction();
        String text = "Edit mode turned off!";
        setInformationLabel(text, 5);
        logWindow.addMessageWithDate(text);
        System.out.println(text);
    }//GEN-LAST:event_menuItemTurnEditModeOffActionPerformed

    /**
     * jActivateRecordActionPerformed
     *
     * @param evt
     */
    private void menuItemActivateRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemActivateRecordActionPerformed

        int rowSelected = archiveTable.getSelectedRows().length;
        int[] rowsSelected = archiveTable.getSelectedRows();
        // Archive Selected Records in Assignments Archive
        if (rowSelected != -1) {

            for (int i = 0; i < rowSelected; i++) {
                String sqlInsert = "INSERT INTO " + database + "." + assignmentTable.getName() + "(symbol, analyst, priority, dateAssigned,dateDone,notes) VALUES ( ";
                int numRow = rowsSelected[i];
                for (int j = 1; j < archiveTable.getColumnCount() - 1; j++) {
                    if (archiveTable.getValueAt(numRow, j) == null) {
                        sqlInsert += null + ",";
                    } else {
                        sqlInsert += "'" + archiveTable.getValueAt(numRow, j) + "',";
                    }
                }
                if (archiveTable.getValueAt(numRow, archiveTable.getColumnCount() - 1) == null) {
                    sqlInsert += null + ")";
                } else {
                    sqlInsert += "'" + archiveTable.getValueAt(numRow, archiveTable.getColumnCount() - 1) + "')";
                }
                try {
                    statement.executeUpdate(sqlInsert);
                    //                    ana.getLogWindow().addMessageWithDate(sqlInsert);
                } catch (SQLException e) {
                    LoggingAspect.afterThrown(e);
                }
            }

            archiveTable.setRowSelectionInterval(rowsSelected[0], rowsSelected[0]);
            loadTable(archiveTable);
            loadTable(assignmentTable);

            String text = rowSelected + " Record(s) Activated!";
            this.setInformationLabel(text, 5);
        } else {
            String text = "Please, select one task!";
            this.setInformationLabel(text, 5);
        }
    }//GEN-LAST:event_menuItemActivateRecordActionPerformed

    private void comboBoxSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxSearchActionPerformed
        comboBoxStartToSearch = false;
        String searchColName = comboBoxSearch.getSelectedItem().toString();
        searchValue = comboBoxValue.getSelectedItem().toString();
        String tabName = getSelectedTabName();

        updateComboList(searchColName, tabName);

        comboBoxValue.setSelectedItem(searchValue);


    }//GEN-LAST:event_comboBoxSearchActionPerformed

    public void comboBoxForSearchMouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            comboBoxValue.getEditor().selectAll();
        } else if (e.isControlDown()) {
            comboBoxValue.showPopup();

        }
    }
    private void menuItemStripslashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemStripslashActionPerformed
        // Pick out the selected tab
        String tabName = getSelectedTabName();
        int sqlChangeNum = 0;
        //      String sqlChange = "";
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        Tab tab = tabs.get(tabName);
        // Pick out the table of selected tab
        JTable table = tab.getTable();
        for (int j = 0; j < table.getColumnCount(); j++) {
            // Locate columns under "path"
            String columnName = table.getColumnName(j);
            if (columnName.equalsIgnoreCase("path")) {
                for (int i = 0; i < table.getRowCount(); i++) {
                    if (table.getValueAt(i, 4) != null) {
                        String str = table.getValueAt(i, 4).toString();
                        //Identify the column startwith and endwith "/" 
                        while (str.startsWith("/")) {
                            //Continue strip "/" until the correct format

                            str = str.substring(1, str.length());
                            sqlChangeNum++;

                        }
                        while (str.endsWith("/") || str.endsWith(" ")) {
                            //Continue strip "/" until the correct format

                            str = str.substring(0, str.length() - 1);
                            sqlChangeNum++;

                        }
                        String id = table.getValueAt(i, 0).toString();
//                            sqlChange = "UPDATE " + tabName + " SET " + columnName
//                                    + " = '" + newstr + "' WHERE ID = '" + id + "' \n" + sqlChange;
                        sql1 = "UPDATE Reports SET path = CASE ID ";
                        sql2 += "WHEN '" + id + "' THEN '" + str + "' \n";
                        sql3 += "'" + id + "',";

                        // Set new value back to table
                        // table.setValueAt(str, i, 4);
                    }
                }
                // Make btn grey when table is not "Report";
                this.makeTableEditable(true);
            }
        }
        if (sqlChangeNum > 0) {
            sql3 = sql3.substring(0, sql3.length() - 1);
            String sql = sql1 + sql2 + " END \n" + "WHERE ID IN (" + sql3 + ");";
            System.out.println(sql);
            System.out.println(sqlChangeNum);
            DBConnection.close();
            if (DBConnection.open()) {

                statement = DBConnection.getStatement();
                try {

                    statement.executeUpdate(sql);
                    LoggingAspect.afterReturn(sql);
                } catch (SQLException e) {
                    LoggingAspect.afterThrown(e);

                }

            } else {
                // connection failed
                LoggingAspect.afterReturn("Failed to connect");
            }
            DBConnection.close();

            JTableCellRenderer cellRenderer = tab.getCellRenderer();
            ModifiedTableData data = tab.getTableData();

            // reload table from database
            loadTable(table);

            // clear cellrenderer
            cellRenderer.clearCellRender();

            // reload modified table data with current table model
            data.reloadData();
            LoggingAspect.afterReturn("Slash are stripped from path");

        } else {
            LoggingAspect.afterReturn("Slash are already stripped from path");

        }
        // reload modified table data into dropdown list
        loadData();


    }//GEN-LAST:event_menuItemStripslashActionPerformed

    private void menuItemAddslashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAddslashActionPerformed

        // Pick out the selected tab
        String tabName = getSelectedTabName();
        int sqlChangeNum = 0;
        //      String sqlChange = "";
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        Tab tab = tabs.get(tabName);
        // Pick out the table of selected tab
        JTable table = tab.getTable();

        for (int j = 0; j < table.getColumnCount(); j++) {
            // Locate columns under "path"
            String columnName = table.getColumnName(j);

            if (columnName.equalsIgnoreCase("path")) {
                for (int i = 0; i < table.getRowCount(); i++) {

                    if (table.getValueAt(i, j) != null) {
                        // Identify which startwith and endwith "/", if not, add it on;
                        String str = table.getValueAt(i, j).toString();
                        if (!str.startsWith("/") && !str.endsWith("/")) {

                            String newstr = "/" + str + "/";
                            // Set new value back to table
//                            table.setValueAt(newstr, i, j);
//                            
                            String id = table.getValueAt(i, 0).toString();
//                            sqlChange = "UPDATE " + tabName + " SET " + columnName
//                                    + " = '" + newstr + "' WHERE ID = '" + id + "' \n" + sqlChange;
                            sql1 = "UPDATE Reports SET path = CASE ID ";
                            sql2 += "WHEN '" + id + "' THEN '" + newstr + "' \n";
                            sql3 += "'" + id + "',";
                            sqlChangeNum++;

//
                        }
                    }
                }

                // Make btn grey except "Report" table;
                this.makeTableEditable(true);
            }

        }
        if (sqlChangeNum > 0) {
            sql3 = sql3.substring(0, sql3.length() - 1);
            String sql = sql1 + sql2 + " END \n" + "WHERE ID IN (" + sql3 + ");";
            System.out.println(sql);
            DBConnection.close();
            if (DBConnection.open()) {

                statement = DBConnection.getStatement();
                try {

                    statement.executeUpdate(sql);
                    LoggingAspect.afterReturn(sql);
                } catch (SQLException e) {
                    LoggingAspect.afterThrown(e);

                }

            } else {
                // connection failed
                LoggingAspect.afterReturn("Failed to connect");
            }
            DBConnection.close();

            JTableCellRenderer cellRenderer = tab.getCellRenderer();
            ModifiedTableData data = tab.getTableData();

            // reload table from database
            loadTable(table);

            // clear cellrenderer
            cellRenderer.clearCellRender();

            // reload modified table data with current table model
            data.reloadData();
            LoggingAspect.afterReturn("Slash are added to path");

        } else {
            LoggingAspect.afterReturn("Slash are already stripped from path");

        }
        // reload modified table data into dropdown list
        loadData();


    }//GEN-LAST:event_menuItemAddslashActionPerformed

    private void menuItemOpenDocumentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemOpenDocumentActionPerformed
        openDocumentTool();
    }//GEN-LAST:event_menuItemOpenDocumentActionPerformed

    private void comboBoxValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxValueActionPerformed
        if (!comboBoxValue.getSelectedItem().toString().equals(searchValue)) {
            if (comboBoxStartToSearch) {
                if (comboBoxSearch.getSelectedItem().toString().equalsIgnoreCase("Analyst")
                        || comboBoxSearch.getSelectedItem().toString().equalsIgnoreCase("Priority")
                        || comboBoxSearch.getSelectedItem().toString().equalsIgnoreCase("Path")) {
                    if (!comboBoxValue.getSelectedItem().toString().startsWith("Enter")
                            || !comboBoxValue.getSelectedItem().toString().endsWith("here")) {
//            
                        filterBySearch();
//
                    }
//
////           
                } else {

                }

            }
        }
        comboBoxValue.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                if (ke.getKeyChar() == KeyEvent.VK_ENTER) {
                    filterBySearch();
                    System.out.println("key");

                }
            }

        });


    }//GEN-LAST:event_comboBoxValueActionPerformed

    private void labelEditModeStateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelEditModeStateMouseClicked
        if (labelEditModeState.getText().equals("ON ")) {
            this.revertChanges();
        }
    }//GEN-LAST:event_labelEditModeStateMouseClicked

    // menu item open document tool 
    public void openDocumentTool() {
        // must be on reports tab
        if (getSelectedTable() == reportTable) {
            JTable table = getSelectedTable();
            int row = table.getSelectedRow();
            // a row must be selected
            if (row != -1) {
                boolean elleFolderFound = false;
                boolean isWindows = FilePathFormat.isWindows();
                // commented out devs because they are both the same
                // however if they change the code is still here
                String forTesters = FilePathFormat.convert("../ELLE ANALYSES", isWindows);// for testers - jar in same folder
                //String forDevs = FilePathFormat.convert("../ELLE ANALYSES", isWindows); // for developers

                String elle_folder = "";
                if (new File(forTesters).exists()) {
                    elle_folder = forTesters;
                    String msg = "path found = " + (new File(forTesters).getAbsolutePath());
                    elleFolderFound = true;
                }
//                else if (new File(forDevs).exists()) {
//                    elle_folder = forDevs;
//                    elleFolderFound = true;
//                }
                if (elleFolderFound == false) {
                    JOptionPane.showMessageDialog(this, "ELLE ANALYSES folder not found.");
                }
                if (elleFolderFound) {
                    Object pathToDoc = table.getValueAt(row, 4); // path column
                    Object document = table.getValueAt(row, 5); // document column
                    if (document == null) {
                        JOptionPane.showMessageDialog(this, "No document in selected row");
                    } else {
                        OpenDocumentTool docTool = new OpenDocumentTool(elle_folder, pathToDoc.toString(), document.toString());
                        docTool.setParent(this);
                        if (!docTool.open()) {
                            JOptionPane.showMessageDialog(this, "Could not open file!");
                        }
                    }
                    String text = "Opening " + document.toString() + " from path: " + pathToDoc.toString();
                    setInformationLabel(text, 10);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No row was selected.");
            }
        }
//        else {
//            JOptionPane.showMessageDialog(this, "Must be on Reports tab.");
//        }
    }

    //set the timer for information Label show
    public static void startCountDownFromNow(int waitSeconds) {
        Timer timer = new Timer(waitSeconds * 1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                informationLabel.setText("");
                searchInformationLabel.setText("");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * loadData
     */
    public void loadData() {
        loadTables(tabs);
    }

    /**
     * setTableListeners This adds mouselisteners and keylisteners to tables.
     *
     * @param table
     */
    public void setTableListeners(final JTable table) {

        // this adds a mouselistener to the table header
        JTableHeader header = table.getTableHeader();
        if (header != null) {
            header.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    if (e.getClickCount() == 2) {
                        clearFilterDoubleClick(e, table);

                    }

                }

                /**
                 * Popup menus are triggered differently on different platforms
                 * Therefore, isPopupTrigger should be checked in both
                 * mousePressed and mouseReleased events for proper
                 * cross-platform functionality.
                 *
                 * @param e
                 */
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        // this calls the column popup menu
                        tabs.get(table.getName())
                                .getColumnPopupMenu().showPopupMenu(e);
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        // this calls the column popup menu
                        tabs.get(table.getName())
                                .getColumnPopupMenu().showPopupMenu(e);
                    }
                }
            });
        }

        // add mouselistener to the table
        table.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        // if left mouse clicks
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            if (e.getClickCount() == 2) {
                                if (e.isControlDown()) {
                                    filterByDoubleClick(table);

                                } else {
                                    Tab tab = tabs.get(table.getName());

                                    // check if this tab is editing or if allowed editing
                                    boolean thisTabIsEditing = tab.isEditing();
                                    boolean noTabIsEditing = !isTabEditing();

                                    if (thisTabIsEditing || noTabIsEditing) {

                                        // set the states for this tab
                                        tab.setEditing(true);
                                        makeTableEditable(true);
                                        setEnabledEditingButtons(true, true);
                                        setBatchEditButtonStates(tab);

                                        // set the color of the edit mode text
                                        editModeTextColor(tab.isEditing());

                                        // get selected cell for editing
                                        int columnIndex = table.columnAtPoint(e.getPoint()); // this returns the column index
                                        int rowIndex = table.rowAtPoint(e.getPoint()); // this returns the rowIndex index
                                        if (rowIndex != -1 && columnIndex != -1) {

                                            // make it the active editing cell
                                            table.changeSelection(rowIndex, columnIndex, false, false);

                                            selectAllText(e);

                                            // if cell is being edited
                                            // cannot cancel or upload or revert
                                            setEnabledEditingButtons(false, false);

                                        } // end not null condition

                                    } // end of is tab editing conditions
                                }
                            } else if (e.getClickCount() == 1) {
                                // do nothing
                                // used to select rows or cells
                                //if edit mode on, it select all the text in it
                                if (labelEditModeState.getText().equals("ON ")) {
                                    selectAllText(e);
                                }
                                if (e.isControlDown()) {

                                    openDocumentTool();

                                }
                            }
                        } // end if left mouse clicks
                        // if right mouse clicks
                        else if (SwingUtilities.isRightMouseButton(e)) {
                            if (e.getClickCount() == 2) {

                            } // end if 2 clicks 
                        } // end if right mouse clicks

                    }// end mouseClicked

                    private void selectAllText(MouseEvent e) {// Select all text inside jTextField

                        JTable table = (JTable) e.getComponent();
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
                }
        );

        table.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();

                if (getSelectedTable() == reportTable) {
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());

                    if (table.getLocation().y < 1 && table.getLocation().y > -8155) {
                        if (col > 3 && col < 6) {
                            table.clearSelection();
                            table.setRowSelectionInterval(row, row);
                            Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
                            setCursor(cursor);

                        } else {
                            Cursor cursor = Cursor.getDefaultCursor();
                            setCursor(cursor);

                        }
                    } else {
                        Cursor cursor = Cursor.getDefaultCursor();
                        setCursor(cursor);
                    }
                }

            }
        });

        // add table model listener
        table.getModel().addTableModelListener(new TableModelListener() {  // add table model listener every time the table model reloaded
            @Override
            public void tableChanged(TableModelEvent e) {

                int row = e.getFirstRow();
                int col = e.getColumn();
                String tab = getSelectedTabName();
                JTable table = tabs.get(tab).getTable();
                ModifiedTableData data = tabs.get(tab).getTableData();
                Object oldValue = data.getOldData()[row][col];
                Object newValue = table.getModel().getValueAt(row, col);
//                System.out.println("table changed: " + oldValue + " " + newValue);

                // check that data is different
                if (!newValue.equals(oldValue)) {

                    String tableName = table.getName();
                    String columnName = table.getColumnName(col);
                    int id = (Integer) table.getModel().getValueAt(row, 0);

                    data.getNewData().add(new ModifiedData(tableName, columnName, newValue, id));

                    // color the cell
                    JTableCellRenderer cellRender = tabs.get(tab).getCellRenderer();
                    cellRender.getCells().get(col).add(row);
                    table.getColumnModel().getColumn(col).setCellRenderer(cellRender);

                    // can upload or revert changes
                    setEnabledEditingButtons(true, true);
                } // if modified data then cancel button not enabled
                else if (!data.getNewData().isEmpty()) {
                    // can upload or revert changes
                    setEnabledEditingButtons(true, true);
                } // there is no new modified data
                else {
                    // no changes to upload or revert (these options disabled)
                    setEnabledEditingButtons(false, false);
                }
            }
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

                    // I believe this is meant to toggle edit mode
                    // so I passed the conditional
                    makeTableEditable(labelEditModeState.getText().equals("ON ") ? false : true);
                }
            }
        });
    }

    /**
     * setTableListeners This method overloads the seTerminalFunctions to take
     * tabs instead of a single table
     *
     * @param tabs
     * @return
     */
    public Map<String, Tab> setTableListeners(Map<String, Tab> tabs) {

        for (Map.Entry<String, Tab> entry : tabs.entrySet()) {
            setTableListeners(tabs.get(entry.getKey()).getTable());
        }
        return tabs;
    }

    public void setPanelListeners() {

        tabbedPanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                Cursor cursor = Cursor.getDefaultCursor();
                setCursor(cursor);
            }

        });
        addPanel_control.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                Cursor cursor = Cursor.getDefaultCursor();
                setCursor(cursor);
            }

        });
        jPanelEdit.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                Cursor cursor = Cursor.getDefaultCursor();
                setCursor(cursor);
            }

        });
    }

    /**
     * filterByDoubleClick this selects the item double clicked on to be
     * filtered
     *
     * @param table
     */
    public void filterByDoubleClick(JTable table) {

        int columnIndex = table.getSelectedColumn(); // this returns the column index
        int rowIndex = table.getSelectedRow(); // this returns the rowIndex index
        if (rowIndex != -1) {
            Object selectedField = table.getValueAt(rowIndex, columnIndex);
            String tabName = getSelectedTabName();
            Tab tab = tabs.get(tabName);
            TableFilter filter = tab.getFilter();
            filter.addFilterItem(columnIndex, selectedField);
            filter.applyFilter();
            String recordsLabel = tab.getRecordsLabel();
            labelRecords.setText(recordsLabel);
        }
    }

    /**
     * clearFilterDoubleClick This clears the filters for that column by double
     * clicking on that column header.
     */
    private void clearFilterDoubleClick(MouseEvent e, JTable table) {

        int columnIndex = table.getColumnModel().getColumnIndexAtX(e.getX());
        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        TableFilter filter = tab.getFilter();
        filter.clearColFilter(columnIndex);
        filter.applyFilter();

        // update records label
        String recordsLabel = tab.getRecordsLabel();
        labelRecords.setText(recordsLabel);
    }

    /**
     * setColumnFormat sets column format for each table
     *
     * @param width
     * @param table
     */
    public void setColumnFormat(float[] colWidths, JTable table) {

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
            int colWidth = (int) colWidths[index];
            TableColumn column = table.getColumnModel().getColumn(index);

            // notes should fill the remainder of allocated width available
            String columnName = table.getColumnName(index);
            if (columnName.equals("notes")) {
                column.setMinWidth((int) colWidth);
            } else {
                column.setPreferredWidth(colWidth);
                column.setMinWidth(colWidth);
            }
        }
        for (int j = 0; j < assignmentTable.getColumnCount(); j++) {

            String columnName = assignmentTable.getColumnName(j);
            // Locate columns under "priority"
            if (columnName.equalsIgnoreCase("priority")) {
                // center alignment
                assignmentTable.getColumnModel().getColumn(j).setCellRenderer(centerRenderer);
            }
        }
        for (int j = 0; j < archiveTable.getColumnCount(); j++) {

            String columnName = archiveTable.getColumnName(j);
            //Locate columns under "priority"
            if (columnName.equalsIgnoreCase("priority")) {
                //center alignment
                archiveTable.getColumnModel().getColumn(j).setCellRenderer(centerRenderer);
            }
        }
    }

    /**
     * updateTable Updates database with edited data This is called from batch
     * edit & uploadChanges
     *
     * @param table
     * @param modifiedDataList
     */
    public void updateTable(JTable table, List<ModifiedData> modifiedDataList) {
        boolean updateSuccessful = true;

        // should probably not be here
        // this method is to update the database, that is all it should do.
        table.getModel().addTableModelListener(table);

        //String uploadQuery = uploadRecord(table, modifiedDataList);
        String sqlChange = "";

        // open database connection
        DBConnection.close();
        if (DBConnection.open()) {

            statement = DBConnection.getStatement();

            for (ModifiedData modifiedData : modifiedDataList) {

                String tableName = modifiedData.getTableName();
                String columnName = modifiedData.getColumnName();
                Object value = modifiedData.getValue();
                int id = modifiedData.getId();

                try {

                    if (value.equals("")) {
                        value = null;
                        sqlChange = "UPDATE " + tableName + " SET " + columnName
                                + " = " + value + " WHERE ID = " + id + ";";
                    } else {
                        sqlChange = "UPDATE " + tableName + " SET " + columnName
                                + " = '" + value + "' WHERE ID = " + id + ";";
                    }

                    statement.executeUpdate(sqlChange);
                    LoggingAspect.afterReturn(sqlChange);

                } catch (SQLException e) {
                    LoggingAspect.afterThrown(e);
                    updateSuccessful = false;
                }
            }

            if (updateSuccessful) {
                LoggingAspect.afterReturn(("Edits uploaded successfully!"));
            }
        } else {
            // connection failed
            LoggingAspect.afterReturn("Failed to connect");
        }

        // finally close connection
        DBConnection.close();
    }

    /**
     * getSelectedTable gets the selected tabName
     *
     * @return
     */
    public JTable getSelectedTable() {  //get JTable by  selected Tab
        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        JTable table = tab.getTable();
        return table;
    }

    /**
     * setLastUpdateTime sets the last update time label
     */
    public void setLastUpdateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(new Date());
        labelTimeLastUpdate.setText("Last updated: " + time);
    }

    public void setIsBatchEditWindowShow(boolean a) {
        this.isBatchEditWindowShow = a;
    }

    public boolean getIsBatchEditWindowShow() {
        return this.isBatchEditWindowShow;
    }

    /**
     * setKeyboardFocusManager sets the keyboard focus manager
     */
    private void setKeyboardFocusManager() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (labelEditModeState.getText().equals("ON ")) {

                    JTable table = getSelectedTable();
                    int row = table.getSelectedRow();
                    int column = table.getSelectedColumn();
                    int columnCount = table.getColumnCount();
                    int rowCount = table.getRowCount();
                    if (e.getKeyCode() == KeyEvent.VK_TAB) {
                        if (e.getComponent() instanceof JTable) {
                            if (column == table.getRowCount() || column == 0) {
                                return false;
                            } else {
                                tableCellSelection(e, table, row, column);
                            }

                            // if table cell is editing 
                            // then the editing buttons should not be enabled
                            if (table.isEditing()) {
                                setEnabledEditingButtons(false, false);
                            }

                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        if (e.getID() == KeyEvent.KEY_RELEASED) {
                            if (e.getComponent() instanceof JTable) {

//                                System.out.println("left table at: " + row + " " + column );
                                if (column == table.getRowCount()) {
                                    return false;
                                } else {
                                    if (column == 0) {
                                        if (row == 0) {
                                            return false;
                                        } else {
                                            row = row - 1;
                                            column = columnCount - 1;
                                        }
                                    }
                                    tableCellSelection(e, table, row, column);
                                }

                            } else {
//                                System.out.println("left at: " + row + " " + column );
                                if (column != 0) {
                                    column = column - 1;
                                }
//                                System.out.println("left now at: " + row + " " + column );
                                table.changeSelection(row, column, false, false);
                                tableCellSelection(e, table, row, column);
                            }
                            // if table cell is editing 
                            // then the editing buttons should not be enabled
                            if (table.isEditing()) {
                                setEnabledEditingButtons(false, false);
                            }
                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        if (e.getID() == KeyEvent.KEY_RELEASED) {
                            if (e.getComponent() instanceof JTable) {

//                                System.out.println("right we are at: " + row + " " + column);
                                if (column == table.getRowCount() || column == columnCount - 1) {
//                                    System.out.println("right first columnt");
                                    return false;
                                } else {
                                    tableCellSelection(e, table, row, column);
                                }
                            } else {
//                                System.out.println("right we are at: " + row + " " + column + " " + columnCount);
                                if (column == columnCount - 1) {
                                    if (row == rowCount - 1) {
//                                        System.out.println("right first columnt");
                                        return false;
                                    } else {
                                        row = row + 1;
                                        column = 0;
                                    }
                                } else {
                                    column = column + 1;
                                }
//                                System.out.println("right we are now at: " + row + " " + column);
                                table.changeSelection(row, column, false, false);
                                tableCellSelection(e, table, row, column);
                                // if table cell is editing 
                                // then the editing buttons should not be enabled
                                if (table.isEditing()) {
                                    setEnabledEditingButtons(false, false);
                                }
                            }
                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        if (e.getID() == KeyEvent.KEY_RELEASED) {
                            if (e.getComponent() instanceof JTable) {
                                if (column == table.getRowCount()) {
                                    return false;
                                } else {
                                    tableCellSelection(e, table, row, column);
                                }
                            } else {
                                if (row == 0 || column == 0) {
                                    return false;
                                } else {
                                    row = row - 1;
                                }
                                table.changeSelection(row, column, false, false);
                                tableCellSelection(e, table, row, column);
                                // if table cell is editing 
                                // then the editing buttons should not be enabled
                                if (table.isEditing()) {
                                    setEnabledEditingButtons(false, false);
                                }
                            }
                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        if (e.getID() == KeyEvent.KEY_RELEASED) {
                            if (e.getComponent() instanceof JTable) {
                                if (column == table.getRowCount() || column == 0) {
                                    return false;
                                } else {
                                    tableCellSelection(e, table, row, column);
                                }
                            } else {
                                if (row == rowCount - 1 || column == 0) {
                                    return false;
                                } else {
                                    row = row + 1;
                                }
                                table.changeSelection(row, column, false, false);
                                tableCellSelection(e, table, row, column);
                                // if table cell is editing 
                                // then the editing buttons should not be enabled
                                if (table.isEditing()) {
                                    setEnabledEditingButtons(false, false);
                                }
                            }
                        }
                    }
                    if (e.getKeyCode() == KeyEvent.VK_Z && e.isMetaDown()) {

                    }
                }
                if (!isBatchEditWindowShow) {
                    if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {
                        if (labelEditModeState.getText().equals("ON ")) { 
                            JTable table = (JTable) e.getComponent().getParent();
                            int column = table.getSelectedColumn();
                            if (table.getColumnName(column).toLowerCase().contains("date")) {
                                System.out.println("date");
                                if (e.getID() != 401) { // 401 = key down, 402 = key released
                                    return false;
                                } else {
                                    JTextField selectCom = (JTextField) e.getComponent();
                                    selectCom.requestFocusInWindow();
                                    selectCom.selectAll();
                                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = new Date();
                                    String today = dateFormat.format(date);
                                    selectCom.setText(today);

                                }
                            }
                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (e.getComponent() instanceof JTable) {
                            JTable table = (JTable) e.getComponent();

                            // make sure in editing mode
                            if (labelEditModeState.getText().equals("ON ")
                                    && !table.isEditing()
                                    && e.getID() == KeyEvent.KEY_PRESSED) {

                                // only show popup if there are changes to upload or revert
                                if (btnUploadChanges.isEnabled() || btnRevertChanges.isEnabled()) {
                                    // if finished display dialog box
                                    // Upload Changes? Yes or No?
                                    Object[] options = {"Commit", "Revert"};  // the titles of buttons

                                    // store selected rowIndex before the table is refreshed
                                    int rowIndex = table.getSelectedRow();

                                    int selectedOption = JOptionPane.showOptionDialog(AnalysterWindow.getInstance(),
                                            "Would you like to upload changes?", "Upload Changes",
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.QUESTION_MESSAGE,
                                            null, //do not use a custom Icon
                                            options, //the titles of buttons
                                            options[0]); //default button title

                                    switch (selectedOption) {
                                        case 0:
                                            // if Commit, upload changes and return to editing
                                            uploadChanges();  // upload changes to database
                                            break;
                                        case 1:
                                            // if Revert, revert changes
                                            revertChanges(); // reverts the model back
                                            break;
                                        default:
                                            // do nothing -> cancel
                                            break;
                                    }

                                    // highlight previously selected rowIndex
                                    if (rowIndex != -1) {
                                        table.setRowSelectionInterval(rowIndex, rowIndex);
                                    }
                                } else {
                                    //if nothing edit, then click enter exit edit mode
                                    makeTableEditable(false);
                                }
                            }

                        }

                    } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {

                        if (e.getComponent() instanceof JTable) {
                            makeTableEditable(false);
                        }
                    }
                }

                return false;
            }

            private void tableCellSelection(KeyEvent e, JTable table, int row, int column) {

                table.getComponentAt(row, column).requestFocus();
                if (column != 0) {
                    table.editCellAt(row, column);
                    JTextField selectCom = (JTextField) table.getEditorComponent();
                    selectCom.requestFocusInWindow();
                    selectCom.selectAll();
                }

            }
        });
    }

    public static AnalysterWindow getInstance() {
        return instance;
    }

    public JLabel getRecordsLabel() {
        return labelRecords;
    }

    public LogWindow getLogWindow() {
        return logWindow;
    }

    public Map<String, Tab> getTabs() {
        return tabs;
    }

    public String getSelectedTabName() {
        return tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex());
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setLogWindow(LogWindow logWindow) {
        this.logWindow = logWindow;
    }

    public Statement getStatement() {
        return statement;
    }

    public JLabel getInformationLabel() {
        return this.informationLabel;
    }

    public void setInformationLabel(String inf, int second) {
        this.informationLabel.setText(inf);
        startCountDownFromNow(second);
    }

    /**
     * initTotalRowCounts called once to initialize the total rowIndex counts of
     * each tabs table
     *
     * @param tabs
     * @return
     */
    public Map<String, Tab> initTotalRowCounts(Map<String, Tab> tabs) {

        int totalRecords;

        boolean isFirstTabRecordLabelSet = false;

        for (Map.Entry<String, Tab> entry : tabs.entrySet()) {
            Tab tab = tabs.get(entry.getKey());
            JTable table = tab.getTable();
            totalRecords = table.getRowCount();
            tab.setTotalRecords(totalRecords);

            if (isFirstTabRecordLabelSet == false) {
                String recordsLabel = tab.getRecordsLabel();
                labelRecords.setText(recordsLabel);
                isFirstTabRecordLabelSet = true; // now its set
            }
        }

        return tabs;
    }

    /**
     * loadTables This method takes a tabs Map and loads all the tabs/tables
     *
     * @param tabs
     * @return
     */
    public Map<String, Tab> loadTables(Map<String, Tab> tabs) {
        for (Map.Entry<String, Tab> entry : tabs.entrySet()) {
            Tab tab = tabs.get(entry.getKey());
            JTable table = tab.getTable();
            loadTable(table);
            informationLabel.setText("Table loaded succesfully");
            startCountDownFromNow(10);
            setTableListeners(table);

            String[] colNames = tab.getTableColNames();
            Map tableComboBoxForSearchDropDownList = this.loadingDropdownListToTable(table, colNames);
            this.comboBoxForSearchDropDown.put(entry.getKey(), tableComboBoxForSearchDropDownList);

        }
        setLastUpdateTime();

        return tabs;
    }

    /**
     * loadTable This method takes a table and loads it Does not need to pass
     * the table back since it is passed by reference However, it can make the
     * code clearer and it's good practice to return
     *
     * @param table
     */
    public JTable loadTable(JTable table) {

        // open connection because might time out
        DBConnection.close();
        DBConnection.open();
        statement = DBConnection.getStatement();
        String sql = "SELECT * FROM " + table.getName() + " ORDER BY symbol ASC";
        loadTable(sql, table);

        return table;
    }

    public JTable loadTable(String sql, JTable table) {

        Vector data = new Vector();
        Vector columnNames = new Vector();
        Vector columnClass = new Vector();
        int columns;

        ResultSet rs = null;
        ResultSetMetaData metaData = null;
        try {
            rs = statement.executeQuery(sql);
            metaData = rs.getMetaData();
        } catch (Exception ex) {
            LoggingAspect.afterThrown(ex);
        }
        try {
            columns = metaData.getColumnCount();
            for (int i = 1; i <= columns; i++) {
                columnClass.addElement(metaData.getColumnClassName(i));
                columnNames.addElement(metaData.getColumnName(i));
            }
            while (rs.next()) {
                Vector row = new Vector(columns);
                for (int i = 1; i <= columns; i++) {
                    row.addElement(rs.getObject(i));
                }
                data.addElement(row);
            }
            rs.close();

        } catch (SQLException ex) {
            LoggingAspect.afterThrown(ex);
        }

        //EditableTableModel model = new EditableTableModel(data, columnNames, columnClass);

        // this has to be set here or else I get errors
        // I tried passing the model to the filter and setting it there
        // but it caused errors
        //table.setModel(model);

        // check that the filter items are initialized
        String tabName = table.getName();
        Tab tab = tabs.get(tabName);
        //setTableCellEditor
        table.setDefaultEditor(Object.class, new TextCellEditor());

        // apply filter
        TableFilter filter = tab.getFilter();
        if (filter.getFilterItems() == null) {
            filter.initFilterItems();
        }
        filter.applyFilter();
        filter.applyColorHeaders();

        // load all checkbox items for the checkbox column pop up filter
        ColumnPopupMenu columnPopupMenu = tab.getColumnPopupMenu();
        columnPopupMenu.loadAllCheckBoxItems();

        // set column format
        float[] colWidthPercent = tab.getColWidthPercent();
        setColumnFormat(colWidthPercent, table);

        // set the listeners for the table
        setTableListeners(table);

        // update last time the table was updated
        setLastUpdateTime();

        informationLabel.setText("Table loaded succesfully");
        startCountDownFromNow(10);

        return table;
    }

    private Map loadingDropdownListToTable(JTable table, String[] colNames) {
        //create empty value List to store drop down list value
        Map<Integer, ArrayList<Object>> valueListMap = new HashMap();
        for (int col = 0; col < table.getColumnCount(); col++) {
            String colName = colNames[col];

            ArrayList valueList = new ArrayList<Object>();

            if (colName.equalsIgnoreCase("symbol") || colName.equalsIgnoreCase("notes") || colName.equalsIgnoreCase("document")) {
                valueList.add("");
            } else {
                //  valueList.add("Enter " + colName + " here");
                Object cellValue = table.getValueAt(0, col);
                Object newValue;
                if (cellValue != null) {
                    valueList.add(cellValue);
                }
                for (int row = 0; row < table.getRowCount(); row++) {
                    newValue = table.getValueAt(row, col);

                    //get distinct value 
                    if (newValue != null) {
                        if (cellValue == null) {
                            valueList.add(" ");
                            cellValue = newValue;
                        } else {

                            cellValue = newValue;
                            valueList.add(cellValue);

                        }
                    }
                }

            }
            Set<Object> uniqueValue = new HashSet<Object>(valueList);
            ArrayList uniqueList = new ArrayList<Object>(uniqueValue);

            valueListMap.put(col, uniqueList);
        }

        return valueListMap;

    }
    public ArrayList getInactiveAnalysts(){
        String sql = "SELECT * FROM Analyst WHERE Status_analyst = 'INACTIVE'";
        ResultSet rs = null;
        ArrayList <Object> inactiveAnalysts = new ArrayList<Object>();
        try {

            DBConnection.close();
            DBConnection.open();
            rs = DBConnection.getStatement().executeQuery(sql);
            
            while(rs.next()){
                inactiveAnalysts.add(rs.getObject("Name_analyst"));
            }          
        } 
        catch (SQLException e) {
            LoggingAspect.afterThrown(e);
        }
        
        return inactiveAnalysts;
    }
    
    public class AnalystComparator implements Comparator<Object>{                
        public int compare(Object Analyst1, Object Analyst2) {
            int statusComparisonResult = 0;
            if (inactiveAnalysts.contains(Analyst1) && inactiveAnalysts.contains(Analyst2)){
                statusComparisonResult = 0;
            }
            else if (!inactiveAnalysts.contains(Analyst1) && inactiveAnalysts.contains(Analyst2)){
                statusComparisonResult = -1;
            }
            else if (inactiveAnalysts.contains(Analyst1) && !inactiveAnalysts.contains(Analyst2)){
                statusComparisonResult = 1;
            }
                        
            if(0 == statusComparisonResult && Analyst1 != null && Analyst2 != null){
                     return Analyst1.toString().compareTo(Analyst2.toString());
            }else{
                return statusComparisonResult;  
            }
        }
    }
    public Set getAnalystSet(JTable table){
        Set <Object> analysts = new HashSet <> ();
        for (int col = 0; col < table.getColumnCount(); col++) {
            if ((table.getColumnName(col).equalsIgnoreCase("analyst"))){
                for(int row = 0; row < table.getRowCount(); row++){
                    if (table.getValueAt(row, col) != null){
                    analysts.add(table.getValueAt(row, col));
                    }
                }
            }
        }
        return analysts;
    } 
    
    private void updateComboList(String colName, String tableName) {
       DefaultComboBoxModel comboBoxSearchModel = new DefaultComboBoxModel();
       comboBoxValue.setModel(comboBoxSearchModel);
        Map comboBoxForSearchValue = this.comboBoxForSearchDropDown.get(tableName);
        ArrayList<Object> dropDownList = new ArrayList<>();
        
            if (colName.equalsIgnoreCase("analyst")) { //Here create dropList of unique table values, add separator, renderer,and sort
                JTable assignmentsTable = tabs.get("Assignments").getTable();
                JTable assignments_ArchivedTable = tabs.get("Assignments_Archived").getTable();
                JTable reportsTable = tabs.get("Reports").getTable();
                Set <Object> uniqueAnalysts = new HashSet<>(); 
                
                uniqueAnalysts.addAll(getAnalystSet(assignmentsTable));
                uniqueAnalysts.addAll(getAnalystSet(assignments_ArchivedTable));
                uniqueAnalysts.addAll(getAnalystSet(reportsTable));
                
                dropDownList = new ArrayList<>(uniqueAnalysts);
                Collections.sort(dropDownList, new AnalystComparator());
                int listLength = dropDownList.size();
                    for (int i = 0; i < listLength; i++)
                    { 
                        if (dropDownList.get(i) != null){
                        String currentAnalyst = dropDownList.get(i).toString();
                            if(inactiveAnalysts.contains(currentAnalyst))
                            {
                              dropDownList.add(i, SEPARATOR);
                               break;
                            }
                        }
                    } 
            
            comboBoxValue.setRenderer(new ComboBoxRenderer());
            comboBoxValue.addActionListener(new BlockComboListener(comboBoxValue));
            }
          
            else{
                JTable table = tabs.get(tableName).getTable();
                for (int col = 0; col < table.getColumnCount(); col++) {
                    if (table.getColumnName(col).equalsIgnoreCase(colName)) {
                        dropDownList = (ArrayList<Object>) comboBoxForSearchValue.get(col);
                        Collections.sort(dropDownList, new Comparator<Object>() {
                            public int compare(Object o1, Object o2) {
                            return o1.toString().compareTo(o2.toString());
                            }
                        });
                    }
                }
            }
           

            
                
                
               /* if (colName.equalsIgnoreCase("priority")) {
                    ArrayList<Integer> dropDownList1 = (ArrayList<Integer>) comboBoxForSearchValue.get(col);
                    System.out.println(dropDownList1 + "1");*/
                
                
                comboBoxStartToSearch = false;

                for (Object item : dropDownList) {
                    if (colName.equalsIgnoreCase("path")) {
                        String str = item.toString();
                        //Identify the column startwith and endwith "/" 

                        while (str.startsWith("/")) {
                            //Continue strip "/" until the correct format

                            str = str.substring(1, str.length());

                        }
                        while (str.endsWith("/") || str.endsWith(" ")) {
                            //Continue strip "/" until the correct format

                            str = str.substring(0, str.length() - 1);

                        }
                        comboBoxSearchModel.addElement(str);
                    } else {

                        comboBoxSearchModel.addElement(item);
                    }
                }
                    
                comboBoxStartToSearch = true;
        
    }
   
    class ComboBoxRenderer extends BasicComboBoxRenderer implements ListCellRenderer {
    JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
    
    final ListCellRenderer<? super Object> original = new JComboBox<Object>()
            .getRenderer();

    public Component getListCellRendererComponent(JList list, Object value,
        int index, boolean isSelected, boolean cellHasFocus) { 
      Component renderComponent = null;
       String str = (value == null) ? "" : value.toString();
        if (SEPARATOR.equals(str)) {
          renderComponent = separator;
       }
      else{
        renderComponent = original.getListCellRendererComponent(list,
                    str, index, isSelected, cellHasFocus);
        }
      return renderComponent;      
    }
    }
    class BlockComboListener implements ActionListener {
    JComboBox comboBoxValue;

    Object currentItem;

    BlockComboListener(JComboBox comboBoxValue) {
      this.comboBoxValue = comboBoxValue;
      currentItem = comboBoxValue.getSelectedItem();
    }

    public void actionPerformed(ActionEvent e) {
      //String tempItem = (String) comboBoxValue.getSelectedItem();
      String tempItem = String.valueOf(comboBoxValue.getSelectedItem());
      if (SEPARATOR.equals(tempItem)) {
        comboBoxValue.setSelectedItem(currentItem);
      } else {
        currentItem = tempItem;
      }
    }
    }
   
    /**
     * deleteRecordsSelected deletes the selected records
     *
     * @param table
     * @return
     * @throws HeadlessException
     */
    public String deleteRecordsSelected(JTable table) throws HeadlessException {

        String sqlDelete = ""; // String for the SQL Statement
        String tableName = table.getName(); // name of the table

        int[] selectedRows = table.getSelectedRows(); // array of the rows selected
        int rowCount = selectedRows.length; // the number of rows selected
        if (rowCount != -1) {
            for (int i = 0; i < rowCount; i++) {
                int row = selectedRows[i];
                Integer selectedID = (Integer) table.getValueAt(row, 0); // Add Note to selected taskID

                if (i == 0) // this is the first rowIndex
                {
                    sqlDelete += "DELETE FROM " + database + "." + tableName
                            + " WHERE " + table.getColumnName(0) + " IN (" + selectedID; // 0 is the first column index = primary key
                } else // this adds the rest of the rows
                {
                    sqlDelete += ", " + selectedID;
                }

            }

            // close the sql statement
            sqlDelete += ");";

            try {

                // delete records from database
                DBConnection.close();
                DBConnection.open();
                statement = DBConnection.getStatement();
                statement.executeUpdate(sqlDelete);

                // refresh table and retain filters
                loadTable(table);

                // show information that a record was deleted 
                String text = rowCount + " Record(s) Deleted!";
                informationLabel.setText(text);
                startCountDownFromNow(10);
                logWindow.addMessageWithDate(text);
                System.out.println(text);

                // set label record information
                String tabName = getSelectedTabName();
                Tab tab = tabs.get(tabName);
                tab.subtractFromTotalRowCount(rowCount); // update total rowIndex count
                String recordsLabel = tab.getRecordsLabel();
                labelRecords.setText(recordsLabel); // update label

            } catch (SQLException e) {
                LoggingAspect.afterThrown(e);
            }
        }
        return sqlDelete;
    }

    public JPanel getAddPanel_control() {
        return addPanel_control;
    }

    public JPanel getjPanel5() {
        return jPanel5;
    }

    public JPanel getjPanelEdit() {
        return jPanelEdit;
    }

    public JPanel getjPanelSQL() {
        return jPanelSQL;
    }

    public JPanel getSearchPanel() {
        return searchPanel;
    }

    public Map<String, Map<Integer, ArrayList<Object>>> getComboBoxForSearchDropDown() {
        return comboBoxForSearchDropDown;
    }

    /**
     * setBatchEditButtonStates Sets the batch edit button enabled if editing
     * allowed for that tab and disabled if editing is not allowed for that tab
     *
     * @param selectedTab // this is the editing tab
     */
    private void setBatchEditButtonStates(Tab selectedTab) {

        for (Map.Entry<String, Tab> entry : tabs.entrySet()) {
            Tab tab = tabs.get(entry.getKey());

            // if selectedTab is editing, that means the switch button was pressed
            if (selectedTab.isEditing()) {
                if (tab == selectedTab) {
                    if (selectedTab.isBatchEditWindowOpen()) {
                        btnBatchEdit.setEnabled(false);
                    } else {
                        tab.setBatchEditBtnEnabled(true);
                    }
                } else {
                    tab.setBatchEditBtnEnabled(false);
                }
            } else {
                tab.setBatchEditBtnEnabled(true);
            }
        }
    }

    /**
     * getBtnBatchEdit
     *
     * @return
     */
    public JButton getBtnBatchEdit() {
        return btnBatchEdit;
    }

    public BatchEditWindow getBatchEditWindow() {
        return batchEditWindow;
    }

    public JComboBox getComboBoxForSearch() {
        return comboBoxValue;
    }

    /**
     * isTabEditing This method returns true or false whether a tab is in
     * editing mode or not
     *
     * @return boolean isEditing
     */
    public boolean isTabEditing() {

        boolean isEditing = false;

        for (Map.Entry<String, Tab> entry : tabs.entrySet()) {
            Tab tab = tabs.get(entry.getKey());
            isEditing = tab.isEditing();

            // if editing break and return true
            if (isEditing) {
                editingTabName = entry.getKey();
                break;
            }
        }

        return isEditing;
    }

    /**
     * setEnabledEditingButtons sets the editing buttons enabled
     *
     * @param switchBtnEnabled
     * @param uploadEnabled
     * @param revertEnabled
     */
    public void setEnabledEditingButtons(boolean uploadEnabled, boolean revertEnabled) {

        // the two editing buttons (upload, revert)
        btnUploadChanges.setEnabled(uploadEnabled);
        btnRevertChanges.setEnabled(revertEnabled);
    }

    /**
     * revertChanges used to revert changes of modified data to original data
     */
    public void revertChanges() {

        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        JTable table = tab.getTable();
        ModifiedTableData modifiedTableData = tab.getTableData();
        modifiedTableData.getNewData().clear();  // clear any stored changes (new data)
        loadTable(table); // reverts the model back
        modifiedTableData.reloadData();  // reloads data of new table (old data) to compare with new changes (new data)

        // no changes to upload or revert
        setEnabledEditingButtons(false, false);
        
        makeTableEditable(false);

        // set the color of the edit mode text
        editModeTextColor(tab.isEditing());

        String text = "Reverted! Nothing has been changed!";
        setInformationLabel(text, 5);
        logWindow.addMessageWithDate(text);
        System.out.println(text);
    }

    /**
     * editModeTextColor This method changes the color of the edit mode text If
     * edit mode is active then the text is green and if it is not active then
     * the text is the default color (black)
     */
    public void editModeTextColor(boolean editing) {

        // if editing
        if (editing) {
            labelEditMode.setForeground(editModeActiveTextColor);
            labelEditModeState.setForeground(editModeActiveTextColor);
        } // else not editing
        else {
            labelEditMode.setForeground(editModeDefaultTextColor);
            labelEditModeState.setForeground(editModeDefaultTextColor);
        }
    }

    /**
     * showWindowInFront This shows the component in front of the Main Window
     *
     * @param c Any component that needs to show on top of the Main window
     */
    public void showWindowInFront(Component c) {

        ((Window) (c)).setAlwaysOnTop(true);

    }

    public String getEditingTabName() {
        return editingTabName;
    }

    public AddRecordsWindow getAddRecordsWindow() {
        return addRecordsWindow;
    }

    public LoginWindow getLoginWindow() {
        return loginWindow;
    }

    public void setLoginWindow(LoginWindow loginWindow) {
        this.loginWindow = loginWindow;
    }

    public EditDatabaseWindow getEditDatabaseWindow() {
        return editDatabaseWindow;
    }

    public void setEditDatabaseWindow(EditDatabaseWindow editDatabaseWindow) {
        this.editDatabaseWindow = editDatabaseWindow;
    }

    public ReportWindow getReportWindow() {
        return reportWindow;
    }

    public void setReportWindow(ReportWindow reportWindow) {
        this.reportWindow = reportWindow;
    }

    public ShortCutSetting getShortCut() {
        return ShortCut;
    }

    public void setShortCut(ShortCutSetting ShortCut) {
        this.ShortCut = ShortCut;
    }

    public Color getEditModeDefaultTextColor() {
        return editModeDefaultTextColor;
    }

    public void setEditModeDefaultTextColor(Color editModeDefaultTextColor) {
        this.editModeDefaultTextColor = editModeDefaultTextColor;
    }

    public Color getEditModeActiveTextColor() {
        return editModeActiveTextColor;
    }

    public void setEditModeActiveTextColor(Color editModeActiveTextColor) {
        this.editModeActiveTextColor = editModeActiveTextColor;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public boolean isComboBoxStartToSearch() {
        return comboBoxStartToSearch;
    }

    public void setComboBoxStartToSearch(boolean comboBoxStartToSearch) {
        this.comboBoxStartToSearch = comboBoxStartToSearch;
    }

    public JTable getArchiveTable() {
        return archiveTable;
    }

    public void setArchiveTable(JTable archiveTable) {
        this.archiveTable = archiveTable;
    }

    public JTable getAssignmentTable() {
        return assignmentTable;
    }

    public void setAssignmentTable(JTable assignmentTable) {
        this.assignmentTable = assignmentTable;
    }

    public JButton getBtnAddRecords() {
        return btnAddRecords;
    }

    public void setBtnAddRecords(JButton btnAddRecords) {
        this.btnAddRecords = btnAddRecords;
    }

    public JButton getBtnCancelSQL() {
        return btnCancelSQL;
    }

    public void setBtnCancelSQL(JButton btnCancelSQL) {
        this.btnCancelSQL = btnCancelSQL;
    }

    public JButton getBtnClearAllFilter() {
        return btnClearAllFilter;
    }

    public void setBtnClearAllFilter(JButton btnClearAllFilter) {
        this.btnClearAllFilter = btnClearAllFilter;
    }

    public JButton getBtnCloseSQL() {
        return btnCloseSQL;
    }

    public void setBtnCloseSQL(JButton btnCloseSQL) {
        this.btnCloseSQL = btnCloseSQL;
    }

    public JButton getBtnEnterSQL() {
        return btnEnterSQL;
    }

    public void setBtnEnterSQL(JButton btnEnterSQL) {
        this.btnEnterSQL = btnEnterSQL;
    }

    public JButton getBtnRevertChanges() {
        return btnRevertChanges;
    }

    public void setBtnRevertChanges(JButton btnRevertChanges) {
        this.btnRevertChanges = btnRevertChanges;
    }

    public JButton getBtnSearch() {
        return btnSearch;
    }

    public void setBtnSearch(JButton btnSearch) {
        this.btnSearch = btnSearch;
    }

    public JButton getBtnUploadChanges() {
        return btnUploadChanges;
    }

    public void setBtnUploadChanges(JButton btnUploadChanges) {
        this.btnUploadChanges = btnUploadChanges;
    }

    public JComboBox getComboBoxSearch() {
        return comboBoxSearch;
    }

    public void setComboBoxSearch(JComboBox comboBoxSearch) {
        this.comboBoxSearch = comboBoxSearch;
    }

    public JScrollPane getjScrollPane1() {
        return jScrollPane1;
    }

    public void setjScrollPane1(JScrollPane jScrollPane1) {
        this.jScrollPane1 = jScrollPane1;
    }

    public JScrollPane getjScrollPane2() {
        return jScrollPane2;
    }

    public void setjScrollPane2(JScrollPane jScrollPane2) {
        this.jScrollPane2 = jScrollPane2;
    }

    public JScrollPane getjScrollPane3() {
        return jScrollPane3;
    }

    public void setjScrollPane3(JScrollPane jScrollPane3) {
        this.jScrollPane3 = jScrollPane3;
    }

    public JScrollPane getjScrollPane4() {
        return jScrollPane4;
    }

    public void setjScrollPane4(JScrollPane jScrollPane4) {
        this.jScrollPane4 = jScrollPane4;
    }

    public JTextArea getjTextAreaSQL() {
        return jTextAreaSQL;
    }

    public void setjTextAreaSQL(JTextArea jTextAreaSQL) {
        this.jTextAreaSQL = jTextAreaSQL;
    }

    public JLabel getLabelEditMode() {
        return labelEditMode;
    }

    public void setLabelEditMode(JLabel labelEditMode) {
        this.labelEditMode = labelEditMode;
    }

    public JLabel getLabelEditModeState() {
        return labelEditModeState;
    }

    public void setLabelEditModeState(JLabel labelEditModeState) {
        this.labelEditModeState = labelEditModeState;
    }

    public JLabel getLabelRecords() {
        return labelRecords;
    }

    public void setLabelRecords(JLabel labelRecords) {
        this.labelRecords = labelRecords;
    }

    public JLabel getLabelTimeLastUpdate() {
        return labelTimeLastUpdate;
    }

    public void setLabelTimeLastUpdate(JLabel labelTimeLastUpdate) {
        this.labelTimeLastUpdate = labelTimeLastUpdate;
    }

    public void setMenuBar(JMenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public JMenu getMenuEdit() {
        return menuEdit;
    }

    public void setMenuEdit(JMenu menuEdit) {
        this.menuEdit = menuEdit;
    }

    public JMenu getMenuFile() {
        return menuFile;
    }

    public void setMenuFile(JMenu menuFile) {
        this.menuFile = menuFile;
    }

    public JMenu getMenuFind() {
        return menuFind;
    }

    public void setMenuFind(JMenu menuFind) {
        this.menuFind = menuFind;
    }

    public JMenu getMenuHelp() {
        return menuHelp;
    }

    public void setMenuHelp(JMenu menuHelp) {
        this.menuHelp = menuHelp;
    }

    public JMenuItem getMenuItemAWSAssign() {
        return menuItemAWSAssign;
    }

    public void setMenuItemAWSAssign(JMenuItem menuItemAWSAssign) {
        this.menuItemAWSAssign = menuItemAWSAssign;
    }

    public JMenuItem getMenuItemActivateRecord() {
        return menuItemActivateRecord;
    }

    public void setMenuItemActivateRecord(JMenuItem menuItemActivateRecord) {
        this.menuItemActivateRecord = menuItemActivateRecord;
    }

    public JMenuItem getMenuItemAddslash() {
        return menuItemAddslash;
    }

    public void setMenuItemAddslash(JMenuItem menuItemAddslash) {
        this.menuItemAddslash = menuItemAddslash;
    }

    public JMenuItem getMenuItemArchiveRecord() {
        return menuItemArchiveRecord;
    }

    public void setMenuItemArchiveRecord(JMenuItem menuItemArchiveRecord) {
        this.menuItemArchiveRecord = menuItemArchiveRecord;
    }

    public JMenuItem getMenuItemBackup() {
        return menuItemBackup;
    }

    public void setMenuItemBackup(JMenuItem menuItemBackup) {
        this.menuItemBackup = menuItemBackup;
    }

    public JMenuItem getMenuItemDeleteRecord() {
        return menuItemDeleteRecord;
    }

    public void setMenuItemDeleteRecord(JMenuItem menuItemDeleteRecord) {
        this.menuItemDeleteRecord = menuItemDeleteRecord;
    }

    public JCheckBoxMenuItem getMenuItemLogChkBx() {
        return menuItemLogChkBx;
    }

    public void setMenuItemLogChkBx(JCheckBoxMenuItem menuItemLogChkBx) {
        this.menuItemLogChkBx = menuItemLogChkBx;
    }

    public JMenuItem getMenuItemLogOff() {
        return menuItemLogOff;
    }

    public void setMenuItemLogOff(JMenuItem menuItemLogOff) {
        this.menuItemLogOff = menuItemLogOff;
    }

    public JMenuItem getMenuItemManageDBs() {
        return menuItemManageDBs;
    }

    public void setMenuItemManageDBs(JMenuItem menuItemManageDBs) {
        this.menuItemManageDBs = menuItemManageDBs;
    }

    public JMenuItem getMenuItemOpenDocument() {
        return menuItemOpenDocument;
    }

    public void setMenuItemOpenDocument(JMenuItem menuItemOpenDocument) {
        this.menuItemOpenDocument = menuItemOpenDocument;
    }

    public JMenuItem getMenuItemPrintDisplay() {
        return menuItemPrintDisplay;
    }

    public void setMenuItemPrintDisplay(JMenuItem menuItemPrintDisplay) {
        this.menuItemPrintDisplay = menuItemPrintDisplay;
    }

    public JMenuItem getMenuItemPrintGUI() {
        return menuItemPrintGUI;
    }

    public void setMenuItemPrintGUI(JMenuItem menuItemPrintGUI) {
        this.menuItemPrintGUI = menuItemPrintGUI;
    }

    public JMenuItem getMenuItemReloadData() {
        return menuItemReloadData;
    }

    public void setMenuItemReloadData(JMenuItem menuItemReloadData) {
        this.menuItemReloadData = menuItemReloadData;
    }

    public JMenuItem getMenuItemRepBugSugg() {
        return menuItemRepBugSugg;
    }

    public void setMenuItemRepBugSugg(JMenuItem menuItemRepBugSugg) {
        this.menuItemRepBugSugg = menuItemRepBugSugg;
    }

    public JCheckBoxMenuItem getMenuItemSQLCmdChkBx() {
        return menuItemSQLCmdChkBx;
    }

    public void setMenuItemSQLCmdChkBx(JCheckBoxMenuItem menuItemSQLCmdChkBx) {
        this.menuItemSQLCmdChkBx = menuItemSQLCmdChkBx;
    }

    public JMenuItem getMenuItemSaveFile() {
        return menuItemSaveFile;
    }

    public void setMenuItemSaveFile(JMenuItem menuItemSaveFile) {
        this.menuItemSaveFile = menuItemSaveFile;
    }

    public JMenuItem getMenuItemStripslash() {
        return menuItemStripslash;
    }

    public void setMenuItemStripslash(JMenuItem menuItemStripslash) {
        this.menuItemStripslash = menuItemStripslash;
    }

    public JMenuItem getMenuItemTurnEditModeOff() {
        return menuItemTurnEditModeOff;
    }

    public void setMenuItemTurnEditModeOff(JMenuItem menuItemTurnEditModeOff) {
        this.menuItemTurnEditModeOff = menuItemTurnEditModeOff;
    }

    public JMenuItem getMenuItemVersion() {
        return menuItemVersion;
    }

    public void setMenuItemVersion(JMenuItem menuItemVersion) {
        this.menuItemVersion = menuItemVersion;
    }

    public JMenuItem getMenuItemViewActiveAssign() {
        return menuItemViewActiveAssign;
    }

    public void setMenuItemViewActiveAssign(JMenuItem menuItemViewActiveAssign) {
        this.menuItemViewActiveAssign = menuItemViewActiveAssign;
    }

    public JMenuItem getMenuItemViewAllAssign() {
        return menuItemViewAllAssign;
    }

    public void setMenuItemViewAllAssign(JMenuItem menuItemViewAllAssign) {
        this.menuItemViewAllAssign = menuItemViewAllAssign;
    }

    public JMenuItem getMenuItemViewAssign() {
        return menuItemViewAssign;
    }

    public void setMenuItemViewAssign(JMenuItem menuItemViewAssign) {
        this.menuItemViewAssign = menuItemViewAssign;
    }

    public JMenuItem getMenuItemViewReports() {
        return menuItemViewReports;
    }

    public void setMenuItemViewReports(JMenuItem menuItemViewReports) {
        this.menuItemViewReports = menuItemViewReports;
    }

    public JMenu getMenuPrint() {
        return menuPrint;
    }

    public void setMenuPrint(JMenu menuPrint) {
        this.menuPrint = menuPrint;
    }

    public JMenu getMenuReports() {
        return menuReports;
    }

    public void setMenuReports(JMenu menuReports) {
        this.menuReports = menuReports;
    }

    public JMenu getMenuSelectConn() {
        return menuSelectConn;
    }

    public void setMenuSelectConn(JMenu menuSelectConn) {
        this.menuSelectConn = menuSelectConn;
    }

    public JMenu getMenuTools() {
        return menuTools;
    }

    public void setMenuTools(JMenu menuTools) {
        this.menuTools = menuTools;
    }

    public JMenu getMenuView() {
        return menuView;
    }

    public void setMenuView(JMenu menuView) {
        this.menuView = menuView;
    }

    public JTable getReportTable() {
        return reportTable;
    }

    public void setReportTable(JTable reportTable) {
        this.reportTable = reportTable;
    }

    public static JLabel getSearchInformationLabel() {
        return searchInformationLabel;
    }

    public static void setSearchInformationLabel(JLabel searchInformationLabel) {
        AnalysterWindow.searchInformationLabel = searchInformationLabel;
    }

    public JTabbedPane getTabbedPanel() {
        return tabbedPanel;
    }

    public void setTabbedPanel(JTabbedPane tabbedPanel) {
        this.tabbedPanel = tabbedPanel;
    }

    // @formatter:off
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel addPanel_control;
    private javax.swing.JTable archiveTable;
    private javax.swing.JTable assignmentTable;
    private javax.swing.JButton btnAddRecords;
    private javax.swing.JButton btnBatchEdit;
    private javax.swing.JButton btnCancelSQL;
    private javax.swing.JButton btnClearAllFilter;
    private javax.swing.JButton btnCloseSQL;
    private javax.swing.JButton btnEnterSQL;
    private javax.swing.JButton btnRevertChanges;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUploadChanges;
    private javax.swing.JComboBox comboBoxSearch;
    private javax.swing.JComboBox comboBoxValue;
    public static javax.swing.JLabel informationLabel;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelEdit;
    private javax.swing.JPanel jPanelSQL;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextAreaSQL;
    private javax.swing.JLabel labelEditMode;
    private javax.swing.JLabel labelEditModeState;
    private javax.swing.JLabel labelRecords;
    private javax.swing.JLabel labelTimeLastUpdate;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuEdit;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenu menuFind;
    private javax.swing.JMenu menuHelp;
    private javax.swing.JMenuItem menuItemAWSAssign;
    private javax.swing.JMenuItem menuItemActivateRecord;
    private javax.swing.JMenuItem menuItemAddslash;
    private javax.swing.JMenuItem menuItemArchiveRecord;
    private javax.swing.JMenuItem menuItemBackup;
    private javax.swing.JMenuItem menuItemDeleteRecord;
    private javax.swing.JCheckBoxMenuItem menuItemLogChkBx;
    private javax.swing.JMenuItem menuItemLogOff;
    private javax.swing.JMenuItem menuItemManageDBs;
    private javax.swing.JMenuItem menuItemOpenDocument;
    private javax.swing.JMenuItem menuItemPrintDisplay;
    private javax.swing.JMenuItem menuItemPrintGUI;
    private javax.swing.JMenuItem menuItemReloadData;
    private javax.swing.JMenuItem menuItemRepBugSugg;
    private javax.swing.JCheckBoxMenuItem menuItemSQLCmdChkBx;
    private javax.swing.JMenuItem menuItemSaveFile;
    private javax.swing.JMenuItem menuItemStripslash;
    private javax.swing.JMenuItem menuItemTurnEditModeOff;
    private javax.swing.JMenuItem menuItemVersion;
    private javax.swing.JMenuItem menuItemViewActiveAssign;
    private javax.swing.JMenuItem menuItemViewAllAssign;
    private javax.swing.JMenuItem menuItemViewAssign;
    private javax.swing.JMenuItem menuItemViewReports;
    private javax.swing.JMenu menuPrint;
    private javax.swing.JMenu menuReports;
    private javax.swing.JMenu menuSelectConn;
    private javax.swing.JMenu menuTools;
    private javax.swing.JMenu menuView;
    private javax.swing.JTable reportTable;
    public static javax.swing.JLabel searchInformationLabel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTabbedPane tabbedPanel;
    // End of variables declaration//GEN-END:variables
    // @formatter:on

    private void setAccessForDeveloper() {
        if (database.equalsIgnoreCase("pupone_dummy")) {
            menuItemLogChkBx.setEnabled(true);
            menuItemSQLCmdChkBx.setEnabled(true);
            menuItemManageDBs.setEnabled(true);
            menuSelectConn.setEnabled(true);
            menuPrint.setEnabled(true);
            menuItemSaveFile.setEnabled(true);
        } else {
            menuItemLogChkBx.setEnabled(false);
            menuItemSQLCmdChkBx.setEnabled(false);
            menuItemManageDBs.setEnabled(false);
            menuSelectConn.setEnabled(false);
            menuPrint.setEnabled(false);
            menuItemSaveFile.setEnabled(false);
        }

    }

    /**
     * CLASS
     */
    class AlignmentTableHeaderCellRenderer implements TableCellRenderer {

        private final TableCellRenderer wrappedRenderer;
        private final JLabel label;

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
            if (table.getName().equals(REPORTS_TABLE_NAME)) {

                if (column < table.getColumnCount() - 4) {
                    label.setHorizontalAlignment(JLabel.CENTER);
                    return label;
                } else {
                    label.setHorizontalAlignment(JLabel.LEFT);
                    return label;
                }
            }

            label.setHorizontalAlignment(column == table.getColumnCount() - 1 ? JLabel.LEFT : JLabel.CENTER);
            return label;

        }

    }
}
