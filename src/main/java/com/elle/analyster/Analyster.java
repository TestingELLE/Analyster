package com.elle.analyster;

import com.elle.analyster.domain.ModifiedData;
import com.elle.analyster.presentation.filter.CreateDocumentFilter;
import com.elle.analyster.presentation.filter.JTableFilter;
import com.elle.analyster.presentation.filter.TableRowFilterSupport;
import static com.elle.analyster.service.Connection.connection;
import com.elle.analyster.service.DeleteRecord;
import com.elle.analyster.service.TableService;
import com.elle.analyster.service.UploadRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Analyster extends JFrame implements ITableNameConstants, IColumnConstants{
    
    // Edit the version and date it was created for new archives and jars
    private final String CREATION_DATE = "2015-07-13";  
    private final String VERSION = "0.6.6d";   
    
    Map<String,Tab> tabs = new HashMap<>(); // stores individual tab information

    private TableService tableService = new TableService();
    private TableRowFilterSupport tableRowFilterSupport;
    private LoadTables loadTables;
    private JTableHeader header;
    private DeleteRecord deleteRecord;
    
    private static Analyster instance;
    private Logger log = LoggerFactory.getLogger(Analyster.class);
    private LogWindow logwind = new LogWindow(); 
    
    public EnterButton enterButton = new EnterButton();
    protected static boolean isFiltering = true;
    private List<ModifiedData> modifiedDataList = new ArrayList<>();    // record the locations of changed cell
    
    @Autowired
    private UploadRecord uploadRecordService;
    
    LoginWindow loginWindow;
    
    /**
     * CONSTRUCTOR
     */
    public Analyster() {
        
        instance = this; // this is used to call an instance of Analyster  

        // create tab objects -> this has to be before initcomponents();
        tabs.put(ASSIGNMENTS_TABLE_NAME, new Tab());
        tabs.put(REPORTS_TABLE_NAME, new Tab());
        tabs.put(ARCHIVE_TABLE_NAME, new Tab());
        
        // set table names -> this has to be before initcomponents();
        tabs.get(ASSIGNMENTS_TABLE_NAME).setTableName(ASSIGNMENTS_TABLE_NAME);
        tabs.get(REPORTS_TABLE_NAME).setTableName(REPORTS_TABLE_NAME);
        tabs.get(ARCHIVE_TABLE_NAME).setTableName(ARCHIVE_TABLE_NAME);
        
        initComponents(); // generated code
        
        // set names to tables (this was in tabbedPanelChanged method)
        assignmentTable.setName(ASSIGNMENTS_TABLE_NAME);
        reportTable.setName(REPORTS_TABLE_NAME);
        archiveTable.setName(ARCHIVE_TABLE_NAME);
        
        // I COMMENTED THESE OUT AND IT WORKED FINE !!
        tableService.setAssignmentTable(assignmentTable);
        tableService.setReportTable(reportTable);
        tableService.setArchiveAssignTable(archiveTable);
        tableService.setViewerTable(assignmentTable);
        
        // set tables to tab objects
        tabs.get(ASSIGNMENTS_TABLE_NAME).setTable(assignmentTable);
        tabs.get(REPORTS_TABLE_NAME).setTable(reportTable);
        tabs.get(ARCHIVE_TABLE_NAME).setTable(archiveTable);
        
        // set table states to tab objects
        tabs.get(ASSIGNMENTS_TABLE_NAME).setTableState(new TableState(assignmentTable));
        tabs.get(REPORTS_TABLE_NAME).setTableState(new TableState(reportTable));
        tabs.get(ARCHIVE_TABLE_NAME).setTableState(new TableState(archiveTable));
        
        // set column width percents to tables of the tab objects
        tabs.get(ASSIGNMENTS_TABLE_NAME).setColWidthPercent(COL_WIDTH_PER_ASSIGNMENTS);
        tabs.get(REPORTS_TABLE_NAME).setColWidthPercent(COL_WIDTH_PER_REPORTS);
        tabs.get(ARCHIVE_TABLE_NAME).setColWidthPercent(COL_WIDTH_PER_ARCHIVE);
        
        // set Activate Records menu item enabled for each tab
        tabs.get(ASSIGNMENTS_TABLE_NAME).setActivateRecordMenuItemEnabled(false);
        tabs.get(REPORTS_TABLE_NAME).setActivateRecordMenuItemEnabled(false);
        tabs.get(ARCHIVE_TABLE_NAME).setActivateRecordMenuItemEnabled(true);
        
        // set Archive Records menu item enabled for each tab
        tabs.get(ASSIGNMENTS_TABLE_NAME).setArchiveRecordMenuItemEnabled(true);
        tabs.get(REPORTS_TABLE_NAME).setArchiveRecordMenuItemEnabled(false);
        tabs.get(ARCHIVE_TABLE_NAME).setArchiveRecordMenuItemEnabled(false);
        
        setKeyboardFocusManager();

        // show and hide components
        btnUploadChanges.setVisible(false);
        jPanelSQL.setVisible(false); 
        btnEnterSQL.setVisible(true);
        btnCancelSQL.setVisible(true);
        btnCancelEditMode.setVisible(false);
        btnBatchEdit.setVisible(true);
        jTextAreaSQL.setVisible(true);

        // set title of window to Analyster
        this.setTitle("Analyster");
        
        // initialize loadTables
        loadTables = new LoadTables();
        
        // create a login window instance
        loginWindow = new LoginWindow(this);
        
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
        textFieldForSearch = new javax.swing.JTextField();
        comboBoxSearch = new javax.swing.JComboBox();
        btnClearAllFilter = new javax.swing.JButton();
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
        btnCancelEditMode = new javax.swing.JButton();
        btnSwitchEditMode = new javax.swing.JButton();
        jLabelEdit = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanelSQL = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaSQL = new javax.swing.JTextArea();
        btnEnterSQL = new javax.swing.JButton();
        btnCancelSQL = new javax.swing.JButton();
        btnCloseSQL = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemFileVersion = new javax.swing.JMenuItem();
        jMenuSelectConn = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuPrint = new javax.swing.JMenu();
        jMenuItemPrintGUI = new javax.swing.JMenuItem();
        jMenuItemPrintDisplay = new javax.swing.JMenuItem();
        jMenuItemSaveFile = new javax.swing.JMenuItem();
        jMenuItemLogOff = new javax.swing.JMenuItem();
        jMenuEdit = new javax.swing.JMenu();
        jMenuEditDB = new javax.swing.JMenuItem();
        jDeleteRecord = new javax.swing.JMenuItem();
        jArchiveRecord = new javax.swing.JMenuItem();
        jActivateRecord = new javax.swing.JMenuItem();
        jMenuFind = new javax.swing.JMenu();
        jMenuReport = new javax.swing.JMenu();
        jMenuView = new javax.swing.JMenu();
        jMenuItemViewAssig = new javax.swing.JMenuItem();
        jMenuItemViewReports = new javax.swing.JMenuItem();
        jMenuItemViewAllAssig = new javax.swing.JMenuItem();
        jMenuItemViewActiveAssig = new javax.swing.JMenuItem();
        jMenuOther = new javax.swing.JMenu();
        jMenuItemOthersLoadData = new javax.swing.JMenuItem();
        jCheckBoxMenuItemViewLog = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItemViewSQL = new javax.swing.JCheckBoxMenuItem();
        jMenuHelp = new javax.swing.JMenu();
        jMenuItemOtherReport = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(894, 560));

        labelTimeLastUpdate.setText("Last updated: ");

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        textFieldForSearch.setText("Enter Symbol name");
        textFieldForSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                textFieldForSearchMouseClicked(evt);
            }
        });
        textFieldForSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textFieldForSearchKeyPressed(evt);
            }
        });

        comboBoxSearch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "symbol", "analyst" }));

        btnClearAllFilter.setText("Clear All Filters");
        btnClearAllFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearAllFilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(searchPanelLayout.createSequentialGroup()
                        .addComponent(btnClearAllFilter)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBoxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(searchPanelLayout.createSequentialGroup()
                        .addGap(202, 202, 202)
                        .addComponent(textFieldForSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSearch)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        searchPanelLayout.setVerticalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldForSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(comboBoxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClearAllFilter))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        labelRecords.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelRecords.setText("labelRecords");

        javax.swing.GroupLayout addPanel_controlLayout = new javax.swing.GroupLayout(addPanel_control);
        addPanel_control.setLayout(addPanel_controlLayout);
        addPanel_controlLayout.setHorizontalGroup(
            addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addPanel_controlLayout.createSequentialGroup()
                .addGroup(addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addPanel_controlLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addPanel_controlLayout.createSequentialGroup()
                        .addGap(258, 258, 258)
                        .addComponent(labelTimeLastUpdate)))
                .addContainerGap(84, Short.MAX_VALUE))
        );
        addPanel_controlLayout.setVerticalGroup(
            addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addPanel_controlLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelTimeLastUpdate)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

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
                "ID", "symbol", "author", "analysisDate", "path", "document", "notes", "notesL"
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

        btnCancelEditMode.setText("Cancel");
        btnCancelEditMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelEditModeActionPerformed(evt);
            }
        });

        btnSwitchEditMode.setText("Switch");
        btnSwitchEditMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSwitchEditModeActionPerformed(evt);
            }
        });

        jLabelEdit.setText("OFF");

        jLabel2.setText("Edit Mode:");

        javax.swing.GroupLayout jPanelEditLayout = new javax.swing.GroupLayout(jPanelEdit);
        jPanelEdit.setLayout(jPanelEditLayout);
        jPanelEditLayout.setHorizontalGroup(
            jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEditLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSwitchEditMode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelEditMode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUploadChanges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAddRecords)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBatchEdit)
                .addGap(26, 26, 26))
        );
        jPanelEditLayout.setVerticalGroup(
            jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEditLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUploadChanges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(btnSwitchEditMode)
                    .addComponent(jLabelEdit)
                    .addComponent(btnCancelEditMode)
                    .addComponent(btnBatchEdit)
                    .addComponent(btnAddRecords))
                .addGap(4, 4, 4))
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 810, Short.MAX_VALUE))
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
                .addGap(4, 4, 4))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 894, Short.MAX_VALUE)
            .addComponent(jPanelEdit, javax.swing.GroupLayout.DEFAULT_SIZE, 894, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanelSQL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(4, 4, 4))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(tabbedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jPanelSQL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPanel.getAccessibleContext().setAccessibleName("Reports");
        tabbedPanel.getAccessibleContext().setAccessibleParent(tabbedPanel);

        jMenuFile.setText("File");

        jMenuItemFileVersion.setText("Version");
        jMenuItemFileVersion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFileVersionActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemFileVersion);

        jMenuSelectConn.setText("Select Connection");

        jMenuItem3.setText("AWS Assignments");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenuSelectConn.add(jMenuItem3);

        jMenuFile.add(jMenuSelectConn);

        jMenuPrint.setText("Print");

        jMenuItemPrintGUI.setText("Print GUI");
        jMenuPrint.add(jMenuItemPrintGUI);

        jMenuItemPrintDisplay.setText("Print Display Window");
        jMenuPrint.add(jMenuItemPrintDisplay);

        jMenuFile.add(jMenuPrint);

        jMenuItemSaveFile.setText("Save File");
        jMenuFile.add(jMenuItemSaveFile);

        jMenuItemLogOff.setText("Log out");
        jMenuItemLogOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLogOffActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemLogOff);

        menuBar.add(jMenuFile);

        jMenuEdit.setText("Edit");

        jMenuEditDB.setText("Manage databases");
        jMenuEditDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuEditDBActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuEditDB);

        jDeleteRecord.setText("Delete Record");
        jDeleteRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDeleteRecordActionPerformed(evt);
            }
        });
        jMenuEdit.add(jDeleteRecord);

        jArchiveRecord.setText("Archive Record");
        jArchiveRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jArchiveRecordActionPerformed(evt);
            }
        });
        jMenuEdit.add(jArchiveRecord);

        jActivateRecord.setText("Activate Record");
        jActivateRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jActivateRecordActionPerformed(evt);
            }
        });
        jMenuEdit.add(jActivateRecord);

        menuBar.add(jMenuEdit);

        jMenuFind.setText("Find");
        menuBar.add(jMenuFind);

        jMenuReport.setText("Reports");
        menuBar.add(jMenuReport);

        jMenuView.setText("View");

        jMenuItemViewAssig.setText("View Assignments Columns");
        jMenuItemViewAssig.setEnabled(false);
        jMenuView.add(jMenuItemViewAssig);

        jMenuItemViewReports.setText("View Reports Columns");
        jMenuItemViewReports.setEnabled(false);
        jMenuView.add(jMenuItemViewReports);

        jMenuItemViewAllAssig.setText("View All Assignments");
        jMenuItemViewAllAssig.setEnabled(false);
        jMenuItemViewAllAssig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemViewAllAssigActionPerformed(evt);
            }
        });
        jMenuView.add(jMenuItemViewAllAssig);

        jMenuItemViewActiveAssig.setText("View Active Assigments");
        jMenuItemViewActiveAssig.setEnabled(false);
        jMenuItemViewActiveAssig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemViewActiveAssigActionPerformed(evt);
            }
        });
        jMenuView.add(jMenuItemViewActiveAssig);

        menuBar.add(jMenuView);

        jMenuOther.setText("Tools");

        jMenuItemOthersLoadData.setText("Reload data");
        jMenuItemOthersLoadData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOthersLoadDataActionPerformed(evt);
            }
        });
        jMenuOther.add(jMenuItemOthersLoadData);

        jCheckBoxMenuItemViewLog.setText("Log");
        jCheckBoxMenuItemViewLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemViewLogActionPerformed(evt);
            }
        });
        jMenuOther.add(jCheckBoxMenuItemViewLog);

        jCheckBoxMenuItemViewSQL.setText("SQL Command");
        jCheckBoxMenuItemViewSQL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemViewSQLActionPerformed(evt);
            }
        });
        jMenuOther.add(jCheckBoxMenuItemViewSQL);

        menuBar.add(jMenuOther);

        jMenuHelp.setText("Help");

        jMenuItemOtherReport.setText("Report a bug/suggestion");
        jMenuItemOtherReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOtherReportActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemOtherReport);

        menuBar.add(jMenuHelp);

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemFileVersionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemFileVersionActionPerformed

        JOptionPane.showMessageDialog(this, "Creation Date: "
                + CREATION_DATE + "\n"
                + "Version: " + VERSION);
    }//GEN-LAST:event_jMenuItemFileVersionActionPerformed

    private void textFieldForSearchMouseClicked(MouseEvent evt) {//GEN-FIRST:event_textFieldForSearchMouseClicked

        textFieldForSearch.setText(null);
    }//GEN-LAST:event_textFieldForSearchMouseClicked

    /**
     * This method is called when the search button is pressed
     * @param evt 
     */
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
       filterBySearch();
    }//GEN-LAST:event_btnSearchActionPerformed
    
    /**
     * This method is called by the searchActionPerformed method
     * and the textForSearchKeyPressed method
     */
    public void filterBySearch() {
        
        String selectedTab = getSelectedTab(); // get the selected tab
        
        int columnIndex; // the column of the table
        
        if (comboBoxSearch.getSelectedItem().toString().equals(SYMBOL_COLUMN_NAME)) {
            columnIndex = 1; // first column is the symbol column
        } else {
            columnIndex = 2; // the second column is the analyst column
        }
        
        String selectedField = textFieldForSearch.getText();  // store string from text box
        
        try{

            // new tableRowFilterSupport instance and takes table to set filter
            tableRowFilterSupport = new TableRowFilterSupport(tabs.get(selectedTab).getTable());
            
            // set actions visible to true
            tableRowFilterSupport.setActionsVisible(true);
            
            // apply changes to tableRowFilterSupport
            // This method still needs refactoring -> legacy code
            tableRowFilterSupport.apply();
            
            // apply changes to filter
            tableRowFilterSupport.getFilter().apply(columnIndex, selectedField);
            
            // this sets the column header green
            GUI.columnFilterStatus(columnIndex, tabs.get(selectedTab).getFilter().getTable());
            
            // set label record information
            labelRecords.setText(tabs.get(selectedTab).getRecordsLabel()); 
            
        }catch(NullPointerException e){
            throwUnknownTableException(selectedTab, e);
        }    
    }
    
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed

        log.info("Connection");

        try{
            String sqlC = "select * from " + ASSIGNMENTS_TABLE_NAME;
            connection(sqlC, assignmentTable);
            log.info("Connection");
            String sqlD = "select * from " + REPORTS_TABLE_NAME;
            connection(sqlD, reportTable);
        } catch (SQLException ex) {
            logwind.sendMessages(ex.getMessage());
        }

    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void btnUploadChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadChangesActionPerformed

        uploadChanges();
    }//GEN-LAST:event_btnUploadChangesActionPerformed

    /**
     * This uploads changes made by editing and saves the changes
     * by uploading them to the database.
     * This method is called by:
     * btnUploadChangesActionPerformed(java.awt.event.ActionEvent evt) 
     * and also a keylistener when editing mode is on and enter is pressed
     */
    public void uploadChanges(){
        
         // upload two tables separately
        
        String selectedTab = getSelectedTab();
        
        updateTable(tabs.get(selectedTab).getTable(), modifiedDataList);

        if (GUI.isIsFiltering()) {
//            loadPrevious(selectedTab);
        } else {         
            loadTables.loadTable(tabs.get(selectedTab).getTable());
        }
        getModifiedDataList().clear();    // reset the arraylist to record future changes
        setLastUpdateTime();    // update time
        makeTableEditable();
    }
    
    private void jMenuItemOtherReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOtherReportActionPerformed
//        new ReportWin();// Create Report
    }//GEN-LAST:event_jMenuItemOtherReportActionPerformed

    private void btnEnterSQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnterSQLActionPerformed
        if (enterButton.isCreateTable(jTextAreaSQL)) {
            try {
                connection(enterButton.getCommand(jTextAreaSQL), assignmentTable);
            } catch (SQLException e) {
                logwind.sendMessages(e.getMessage());  //To change body of catch statement use File | Settings | File Templates.
            }
        } else {
            
            try {
                    Statement state = GUI.con.createStatement();
                    state.executeUpdate(enterButton.getCommand(jTextAreaSQL));
            } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
            } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }//GEN-LAST:event_btnEnterSQLActionPerformed

    private void btnCancelSQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelSQLActionPerformed
        ((AbstractDocument) jTextAreaSQL.getDocument())
                .setDocumentFilter(new CreateDocumentFilter(0));
        jTextAreaSQL.setText("Please input an SQL statement:\n>>");
        ((AbstractDocument) jTextAreaSQL.getDocument())
                .setDocumentFilter(new CreateDocumentFilter(33));
    }//GEN-LAST:event_btnCancelSQLActionPerformed

    private void btnCloseSQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseSQLActionPerformed

        jPanelSQL.setVisible(false);
        jCheckBoxMenuItemViewSQL.setSelected(false);
    }//GEN-LAST:event_btnCloseSQLActionPerformed

    private void btnSwitchEditModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSwitchEditModeActionPerformed

        makeTableEditable();

    }//GEN-LAST:event_btnSwitchEditModeActionPerformed
    //Make the table Editable or Read Only

    public void makeTableEditable() {
        if (jLabelEdit.getText().equals("OFF")) {
            jLabelEdit.setText("ON ");
            btnSwitchEditMode.setVisible(false);
            btnUploadChanges.setVisible(true);
            btnCancelEditMode.setVisible(true);
            btnBatchEdit.setVisible(true);
            isFiltering = false;
            makeEditable(assignmentTable.getModel());
            makeEditable(reportTable.getModel());
            makeEditable(archiveTable.getModel());

        } else {
            jLabelEdit.setText("OFF");
            btnSwitchEditMode.setVisible(true);
            btnUploadChanges.setVisible(false);
            btnCancelEditMode.setVisible(false);
            btnBatchEdit.setVisible(true);
            isFiltering = true;
            ((MyTableModel)assignmentTable.getModel()).setReadOnly(true);
            ((MyTableModel) reportTable.getModel()).setReadOnly(true);
            ((MyTableModel) archiveTable.getModel()).setReadOnly(true);
//            ((MyTableModel) viewerTable.getModel()).setReadOnly(true);
        }
    }

    private void makeEditable(TableModel tableModel) {
        if (tableModel instanceof MyTableModel) {
            ((MyTableModel) tableModel).setReadOnly(false);
             tableModel.removeTableModelListener(assignmentTable); 
             tableModel.removeTableModelListener(reportTable); 
             
        }
    }
    private void btnCancelEditModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelEditModeActionPerformed
        String selectedTab = tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex());
        if (GUI.isIsFiltering()) {
            loadPrevious(selectedTab);
        } else {
            loadTables.loadTable(tabs.get(selectedTab).getTable());
        }
        makeTableEditable();

    }//GEN-LAST:event_btnCancelEditModeActionPerformed
    public void loadPrevious(String selectedTab) {

        // TODO check that the selectedTab is actually needed to be passed in.
        
        try{
            loadTables.loadAssignmentTableWithFilter(tabs.get(selectedTab).getFilter().getColumnIndex(), tabs.get(selectedTab).getFilter().getFilterCriteria());
            setColumnFormat(tabs.get(selectedTab).getColWidthPercent(), tabs.get(selectedTab).getTable());
            GUI.columnFilterStatus(tabs.get(selectedTab).getFilter().getColumnIndex(), tabs.get(selectedTab).getTable());
            // set label record information
            labelRecords.setText(tabs.get(selectedTab).getRecordsLabel()); 
        }catch(NullPointerException e){
            throwUnknownTableException(selectedTab, e);
        }
    }
    
    private void changeTabbedPanelState() {
        
        String selectedTab = getSelectedTab();

        // pointers to information
        boolean isFilterActive = false;
        
        // this enables or disables the menu components for this tab
        jActivateRecord.setEnabled(tabs.get(selectedTab).isActivateRecordMenuItemEnabled()); 
        jArchiveRecord.setEnabled(tabs.get(selectedTab).isArchiveRecordMenuItemEnabled()); 
        
        switch (selectedTab) {
            case ASSIGNMENTS_TABLE_NAME:
                isFilterActive = GUI.filterAssignmentIsActive;
                break;
                
            case REPORTS_TABLE_NAME:
                isFilterActive = GUI.filterReportIstActive;
                break;
                
            case ARCHIVE_TABLE_NAME:
                isFilterActive = GUI.filterArchiveIsActive;
                break;

            default:
                selectedTab = "unknown";
                break;
        }
        
        try{
            if (isFilterActive) {
            tabs.get(selectedTab).setTable(tabs.get(selectedTab).getFilteredTable());
            } else {
                
                // new tableRowFilterSupport instance and takes table to set filter
                tableRowFilterSupport = new TableRowFilterSupport(tabs.get(selectedTab).getTable());

                // set actions visible to true
                tableRowFilterSupport.setActionsVisible(true);

                // apply changes to tableRowFilterSupport
                // This method still needs refactoring -> legacy code
                tableRowFilterSupport.apply();

                // set filter to tabs table
                tabs.get(selectedTab).setFilter(tableRowFilterSupport.getFilter());
                
                // set filtered table in tabs using the filter to filter and return table
                tabs.get(selectedTab).setFilteredTable(tabs.get(selectedTab).getFilter().getTable());
            }
            
            // set label record information
            labelRecords.setText(tabs.get(selectedTab).getRecordsLabel()); 
        }catch(NullPointerException e){
            throwUnknownTableException(selectedTab, e);
        }
    }

    private void btnBatchEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatchEditActionPerformed
        TableEditor tableEditor = new TableEditor(tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()), this);
        tableEditor.setVisible(true);

    }//GEN-LAST:event_btnBatchEditActionPerformed

    private void jMenuEditDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuEditDBActionPerformed
        EditDatabaseList editDBWindow = new EditDatabaseList();
        editDBWindow.setLocationRelativeTo(this);
        editDBWindow.setVisible(true);
    }//GEN-LAST:event_jMenuEditDBActionPerformed

    private void btnAddRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRecordsActionPerformed
        new AddRecords(this, logwind).setVisible(true);
    }//GEN-LAST:event_btnAddRecordsActionPerformed

    /**
     * This method listens if the enter key was pressed in the search text box.
     * This allows the value to be entered without having to click the 
     * search button.
     * @param evt 
     */
    private void textFieldForSearchKeyPressed(KeyEvent evt) {//GEN-FIRST:event_textFieldForSearchKeyPressed
        
        // if the enter key is pressed call the filterBySearch method.
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            filterBySearch();
        }
    }//GEN-LAST:event_textFieldForSearchKeyPressed

    private void jMenuItemLogOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLogOffActionPerformed
        Object[] options = {"Reconnect", "Log Out"};  // the titles of buttons

        int n = JOptionPane.showOptionDialog(this, "Would you like to reconnect?", "Log off",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]); //default button title

        switch (n) {
            case 0: {               // Reconnect
                
                // hide Analyster
                this.setVisible(false);
                
                // show login window
                loginWindow.setVisible(true);

                break;
            }
            case 1:
                System.exit(0); // Quit
        }
    }//GEN-LAST:event_jMenuItemLogOffActionPerformed

    private void jDeleteRecordActionPerformed(java.awt.event.ActionEvent evt) {
        deleteRecord = new DeleteRecord();
        String selectedTab = getSelectedTab();
        String sqlDelete;

       try{
            sqlDelete = deleteRecord.deleteRecordSelected(tabs.get(selectedTab).getTable());
            logwind.sendMessages(sqlDelete);
            
            // set label record information
            tabs.get(selectedTab).subtractFromTotalRowCount(1); // update total row count
            labelRecords.setText(tabs.get(selectedTab).getRecordsLabel()); // update label
            
        }catch(NullPointerException e){
            throwUnknownTableException(selectedTab, e);
        } 
    }


    private void jMenuItemViewAllAssigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemViewAllAssigActionPerformed
        loadData();
    }//GEN-LAST:event_jMenuItemViewAllAssigActionPerformed

    // load only active data from analyst
    private void jMenuItemViewActiveAssigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemViewActiveAssigActionPerformed
        log.info("Connection");
        String sqlC = "select A.* from Assignments A left join t_analysts T\n" + "on A.analyst = T.analyst\n" + "where T.active = 1\n" + "order by A.symbol";
        try {
            connection(sqlC, assignmentTable);
        } catch (SQLException e) {
            logwind.sendMessages(e.getMessage());  
        }

        setColumnFormat(tabs.get(ASSIGNMENTS_TABLE_NAME).getColWidthPercent(), assignmentTable);
        tabs.get(ASSIGNMENTS_TABLE_NAME).getTableState().init(assignmentTable, new String[]{"Symbol", "Analyst"});
        // set label record information
        labelRecords.setText(tabs.get(ASSIGNMENTS_TABLE_NAME).getRecordsLabel()); 
    }//GEN-LAST:event_jMenuItemViewActiveAssigActionPerformed

    private void btnClearAllFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearAllFilterActionPerformed

        String selectedTab = getSelectedTab();
        
        switch (selectedTab) {
            case ASSIGNMENTS_TABLE_NAME:
                GUI.filterAssignmentIsActive = false;
                break;
            case REPORTS_TABLE_NAME:
                GUI.filterReportIstActive = false;
                break;
            case ARCHIVE_TABLE_NAME:
                GUI.filterArchiveIsActive = false;
                break;
        }
        
        loadTables.loadTable(tabs.get(selectedTab).getTable());
        GUI.cleanAllColumnFilterStatus(tabs.get(selectedTab).getTable());
        // set label record information
        labelRecords.setText(tabs.get(selectedTab).getRecordsLabel()); 
                
        modifiedDataList.clear();

    }//GEN-LAST:event_btnClearAllFilterActionPerformed


    private void jMenuItemOthersLoadDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOthersLoadDataActionPerformed
        
        String selectedTab = getSelectedTab();
        
        loadTables.loadTable(tabs.get(selectedTab).getTable());
        // set label record information
        labelRecords.setText(tabs.get(selectedTab).getRecordsLabel()); 
    }//GEN-LAST:event_jMenuItemOthersLoadDataActionPerformed

    private void jArchiveRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jArchiveRecordActionPerformed
        tableService.archiveRecords();

    }//GEN-LAST:event_jArchiveRecordActionPerformed

    private void jActivateRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jActivateRecordActionPerformed
        tableService.activateRecords();

    }//GEN-LAST:event_jActivateRecordActionPerformed

//Filter is generated everytime that table is selected.
    private void tabbedPanelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbedPanelStateChanged

        TableState ts = getTableState();
        changeTabbedPanelState();
        String[] field = ts.getSearchFields();
        if (field == null) {
            comboBoxSearch.setModel(new DefaultComboBoxModel(new String[]{"Symbol", "Analyst"}));
        } else {
            comboBoxSearch.setModel(new DefaultComboBoxModel(field));
        }

        modifiedDataList.clear();    // when selected table changed, clear former edit history
    }//GEN-LAST:event_tabbedPanelStateChanged

    private void jCheckBoxMenuItemViewLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemViewLogActionPerformed

        if(jCheckBoxMenuItemViewLog.isSelected()){
            
            logwind.setLocationRelativeTo(this);
            logwind.setVisible(true); // show log window
            
            // remove check if window is closed from the window
            logwind.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e){
                        jCheckBoxMenuItemViewLog.setSelected(false);
                    }
                });
        }else{
            // hide log window
            logwind.setVisible(false);
        }
    }//GEN-LAST:event_jCheckBoxMenuItemViewLogActionPerformed

    private void jCheckBoxMenuItemViewSQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemViewSQLActionPerformed
 
        /**
         * ************* Strange behavior *************************
         * The jPanelSQL.getHeight() is the height before 
         * the jCheckBoxMenuItemViewSQLActionPerformed method was called.
         * 
         * The jPanelSQL.setVisible() does not change the size 
         * of the sql panel after it is executed.
         * 
         * The jPanel size will only change after 
         * the jCheckBoxMenuItemViewSQLActionPerformed is finished.
         * 
         * That is why the the actual integer is used rather than  getHeight().
         * 
         * Example:
         * jPanelSQL.setVisible(true);
         * jPanelSQL.getHeight(); // this returns 0
         */
        
        if(jCheckBoxMenuItemViewSQL.isSelected()){
            
            // show sql panel
            jPanelSQL.setVisible(true);
            this.setSize(this.getWidth(), 560 + 112); 
            
        }else{
            
            // hide sql panel
            jPanelSQL.setVisible(false);
            this.setSize(this.getWidth(), 560);
        }
    }//GEN-LAST:event_jCheckBoxMenuItemViewSQLActionPerformed

    private void jTableChanged(TableModelEvent e) {

        int row = e.getFirstRow();
        int col = e.getColumn();
        int id;
        Object value;
        
        String selectedTab = getSelectedTab();
        
        id = (Integer) tabs.get(selectedTab).getTable().getModel().getValueAt(row, 0);
        value = tabs.get(selectedTab).getTable().getModel().getValueAt(row, col);

        ModifiedData modifiedData = new ModifiedData();
        modifiedData.setColumnIndex(col);
        modifiedData.setId(id);
        modifiedData.setTableName(selectedTab);
        modifiedData.setValueModified(value);
        modifiedDataList.add(modifiedData);
    }

    public void loadData() {
        loadTables.loadTables(tabs);
    }

    /**
     * This adds mouselisteners and keylisteners to tables.
     * @param table 
     */
    public void setTerminalsFunction(final JTable table) { 
        
        // this adds a mouselistener to the table header
        header = table.getTableHeader();
        if (header != null) {
            header.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        if (isFiltering) {
                            clearFilterDoubleClick(e, table);
                        }
                    } else if (e.getClickCount() == 1) {
                        // why is nothing here?
                        // Shouldnt this order the columns?
                        // or perhaps it is already a built in feature to the JTable?
                    }
                }
            });
        }
        
        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_F2) {
                    makeTableEditable();
                } 
                
                // in editing mode this should ask to upload changes when enter key press
                if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    
                    // make sure in editing mode
                    if(jLabelEdit.getText().equals("ON ")){

                        // if finished display dialog box
                        // Upload Changes? Yes or No?
                        Object[] options = {"Commit", "Revert"};  // the titles of buttons

                        int selectedOption = JOptionPane.showOptionDialog(Analyster.getInstance(), 
                                "Would you like to upload changes?", "Upload Changes",
                                JOptionPane.YES_NO_OPTION, 
                                JOptionPane.QUESTION_MESSAGE,
                                null, //do not use a custom Icon
                                options, //the titles of buttons
                                options[0]); //default button title

                        switch (selectedOption) {
                            case 0:            
                                // if Commit, upload changes and return to editing
                                uploadChanges();
                                break;
                            case 1:
                                // if Revert, revert changes
                                
                                //TODO
                                // there is still no easy way to work with the tables
                                // refactoring first would be ideal for a feasible solution
                                
                                break;
                            default:
                                // do nothing -> cancel
                                break;
                        }   
                        
                        // return to edit mode for next cell
                        
                        // get selected cell
                        int columnIndex = table.getSelectedColumn(); // this returns the column index
                        int rowIndex = table.getSelectedRow() + 1; // this returns the next row index
                        if (rowIndex != -1 && columnIndex != -1) {

                            // this highlights the next row
                            table.changeSelection(rowIndex, columnIndex, false, false);

                            // this starts editing the next cell
                            table.editCellAt(rowIndex, columnIndex);
                            
                            // this selects all the text in the editing cell
                            JTextField selectCom = (JTextField) table.getEditorComponent();
                            if (selectCom != null) {
                                selectCom.requestFocusInWindow();
                                selectCom.selectAll();
                            }
                        }
                    }
                }
            }
        });
        
        table.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        
                        // if left mouse clicks
                        if(SwingUtilities.isLeftMouseButton(e)){
                            if (e.getClickCount() == 2 ) {
                                filterByDoubleClick(table);
                            } else if (e.getClickCount() == 1) {
                                if (jLabelEdit.getText().equals("ON ")) {
                                    selectAllText(e);
                                }
                            }
                        } // end if left mouse clicks
                        
                        // if right mouse clicks
                        else if(SwingUtilities.isRightMouseButton(e)){
                            if (e.getClickCount() == 2 ) {
                                
                                // make table editable
                                makeTableEditable();
                                
                                // get selected cell
                                int columnIndex = table.columnAtPoint(e.getPoint()); // this returns the column index
                                int rowIndex = table.rowAtPoint(e.getPoint()); // this returns the row index
                                if (rowIndex != -1 && columnIndex != -1) {
                                    
                                    // make it the active editing cell
                                    table.changeSelection(rowIndex, columnIndex, false, false);
                                    
                                    selectAllText(e);

                                } // end not null condition
                                
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

    }

    public void filterByDoubleClick(JTable table) {
        
        int columnIndex = table.getSelectedColumn(); // this returns the column index
        int rowIndex = table.getSelectedRow(); // this returns the row index
        if (rowIndex != -1) {
            Object selectedField = table.getValueAt(rowIndex, columnIndex);
            // apply filter
            tabs.get(table.getName()).getFilter().apply(columnIndex, selectedField);
            GUI.columnFilterStatus(columnIndex, tabs.get(table.getName()).getFilter().getTable());
            // set label record information
            labelRecords.setText(tabs.get(table.getName()).getRecordsLabel()); 
        }
    }

    /**
     * This is triggered when the the column header is double clicked
     */
    private void clearFilterDoubleClick(MouseEvent e, JTable table) {
        
        int columnIndex = table.getColumnModel().getColumnIndexAtX(e.getX());
        
        tabs.get(table.getName()).getFilter().apply(columnIndex, tabs.get(table.getName()).getFilter().getDistinctColumnItems(columnIndex));
        GUI.cleanColumnFilterStatus(columnIndex, tabs.get(table.getName()).getFilter().getTable());// clean green background
        // set label record information
        labelRecords.setText(tabs.get(table.getName()).getRecordsLabel()); 
    }

    public String sqlQuery(String tableName) { //Creat Query to select * from DB.
        log.info("Connection");
        String SqlQuery = "SELECT * FROM " + tableName + " ORDER BY symbol ASC";
        return SqlQuery;
    }

    public void tableReload(final JTable table, Vector data, Vector columnNames) {
        MyTableModel model = new MyTableModel(data, columnNames, isFiltering);
        TableRowSorter sorter = new TableRowSorter<>(model);

        model.addTableModelListener(new TableModelListener() {  // add table model listener every time the table model reloaded
            @Override
            public void tableChanged(TableModelEvent e) {
                jTableChanged(e);
            }
        });

        table.setModel(model);
        table.setRowSorter(sorter);
        setColumnFormat(tabs.get(ASSIGNMENTS_TABLE_NAME).getColWidthPercent(), assignmentTable);
        setColumnFormat(tabs.get(REPORTS_TABLE_NAME).getColWidthPercent(), reportTable);
        setColumnFormat(tabs.get(ARCHIVE_TABLE_NAME).getColWidthPercent(), archiveTable);
    }

    void setColumnFormat(float[] width, JTable table) {
        // Center column content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        //LEFT column content
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        //Center column header
        int widthFixedColumns = 0;
        header = table.getTableHeader();
        if (!(header.getDefaultRenderer() instanceof AlignmentTableHeaderCellRenderer)) {
            header.setDefaultRenderer(new AlignmentTableHeaderCellRenderer(header.getDefaultRenderer()));
        }

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        switch (table.getName()) {

            case REPORTS_TABLE_NAME: {
                int i;
                for (i = 0; i < width.length; i++) {
                    int pWidth = Math.round(width[i]);
                    table.getColumnModel().getColumn(i).setPreferredWidth(pWidth);
                    if (i >= width.length - 3) {
                        table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
                    } else {
                        table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    }
                    widthFixedColumns += pWidth;
                }
                Double tw = jPanel5.getSize().getWidth();
                int twi = tw.intValue();
                table.getColumnModel().getColumn(width.length).setPreferredWidth(twi - (widthFixedColumns + 25));
                table.setMinimumSize(new Dimension(908, 300));
                table.setPreferredScrollableViewportSize(new Dimension(908, 300));
                break;
            }
            default:
                for (int i = 0; i < width.length; i++) {
                    int pWidth = Math.round(width[i]);
                    table.getColumnModel().getColumn(i).setPreferredWidth(pWidth);
                    table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    widthFixedColumns += pWidth;
                }
                Double tw = jPanel5.getSize().getWidth();
                int twi = tw.intValue();
                table.getColumnModel().getColumn(width.length).setPreferredWidth(twi - (widthFixedColumns + 25));
                table.setMinimumSize(new Dimension(908, 300));
                table.setPreferredScrollableViewportSize(new Dimension(908, 300));
                break;

        }
    }

    /**
     * This method handles unknown table exceptions
     * It takes the value of the selected tab that caused the exception
     * It also takes the exception to print the stack trace
     * @param selectedTab
     */
    public void throwUnknownTableException(String selectedTab, Exception e) {
        try {
            String errorMessage = "ERROR: unknown table: " + selectedTab;
            throw new NoSuchFieldException(errorMessage);
        } catch (NoSuchFieldException ex) {
            ex.setStackTrace(e.getStackTrace());
            ex.printStackTrace();
            // post to log.txt
            getLogwind().sendMessages(ex.getMessage());
        }
    }
    

    // Keep the float in Table Editor by separating editing part out here
    public void batchEdit(TableEditor editor) {
        
        int row[], id, col = 1, i, j, num;
        JTable table = getSelectedTable();   // current Table
        String newString, columnName;
        table.setAutoCreateRowSorter(false);
        List<ModifiedData> modifiedDataBatchEdit = new ArrayList<>();
        newString = editor.newString;
        row = table.getSelectedRows();
        num = table.getSelectedRowCount();
        columnName = editor.category;
        for (i = 0; i < table.getColumnCount(); i++) {
            if (columnName.equals(table.getColumnName(i))) {
                col = i;
                break;
            }
        }
            for (i = 0; i <= num - 1; i++) {
                int row2 = table.convertRowIndexToModel(row[i]);
                id = (Integer)table.getModel().getValueAt(row2,0);
                ModifiedData modifiedData = new ModifiedData();
                modifiedData.setColumnIndex(col);
                modifiedData.setTableName(table.getName());
                modifiedData.setId(id);
                modifiedData.setValueModified(newString);
                modifiedDataBatchEdit.add(modifiedData);
            }
            updateTable(table, modifiedDataBatchEdit);

    }

    private void updateTable(JTable table, List<ModifiedData> modifiedDataList) {
        table.getModel().addTableModelListener(table);
        try {
            String uploadQuery = uploadRecordService.uploadRecord(table, modifiedDataList);
            loadPrevious(table.getName());

            JOptionPane.showMessageDialog(this, "Edits uploaded!");
            logwind.sendMessages(uploadQuery);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Upload failed!");
            logwind.sendMessages(e.getMessage());
            logwind.sendMessages(e.getSQLState() + "\n");
        }
    }

    public JTable getSelectedTable() {  //get JTable by  selected Tab
        String selectedTab = getSelectedTab();
        return tabs.get(selectedTab).getTable();
    }

    public TableState getTableState() {                 // get TableState by selected Tab
        String selectedTab = getSelectedTab();
        return tabs.get(selectedTab).getTableState();
    }

    public TableState getTableState(JTable table) {     // get TableState by jTable object
        return tabs.get(table.getName()).getTableState();
    }

    public void setLastUpdateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(new Date());
        labelTimeLastUpdate.setText("Last updated: " + time);
    }
    
    private void setKeyboardFocusManager() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {// Allow to TAB-

            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    if (jLabelEdit.getText().equals("ON ")) {
                        if (e.getComponent() instanceof JTable) {
                            JTable table = (JTable) e.getComponent();
                            int row = table.getSelectedRow();
                            int column = table.getSelectedColumn();
                            if (column == table.getRowCount() || column == 0) {
                                return false;
                            } else {
                                table.getComponentAt(row, column).requestFocus();
                                table.editCellAt(row, column);
                                JTextField selectCom = (JTextField) table.getEditorComponent();
                                selectCom.requestFocusInWindow();
                                selectCom.selectAll();
                            }
                        }
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {
                    if (jLabelEdit.getText().equals("ON ")) {                       // Default Date input with today's date
                        JTable table = (JTable) e.getComponent().getParent();
                        int column = table.getSelectedColumn();
                        if (table.getColumnName(column).toLowerCase().contains("date")) {
                            if (e.getID() != 401) {
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
                }
                return false;
            }
        }
        );
    }
    
    public UploadRecord getUploadRecordService() {
        return uploadRecordService;
    }

    public void setUploadRecordService(UploadRecord uploadRecordService) {
        this.uploadRecordService = uploadRecordService;
    }

    public JTableFilter getFilterTempArchive() {
        return tabs.get(ARCHIVE_TABLE_NAME).getFilter();
    }

    public void setFilterTempArchive(JTableFilter filterTempArchive) {
        tabs.get(ARCHIVE_TABLE_NAME).setFilter(filterTempArchive);
    }

    public JTableFilter getFilterTempReport() {
        return tabs.get(REPORTS_TABLE_NAME).getFilter();
    }

    public void setFilterTempReport(JTableFilter filterTempReport) {
        tabs.get(REPORTS_TABLE_NAME).setFilter(filterTempReport);
    }

    public void setFilterTempAssignment(JTableFilter filterTempAssignment) {
        tabs.get(ASSIGNMENTS_TABLE_NAME).setFilter(filterTempAssignment);
    }

    public void setNumberArchiveAssignInit(int numberArchiveAssignInit) {
        tabs.get(ARCHIVE_TABLE_NAME).setTotalRecords(numberArchiveAssignInit);
    }

    public void setNumberAssignmentInit(int numberAssignmentInit) {
        tabs.get(ASSIGNMENTS_TABLE_NAME).setTotalRecords(numberAssignmentInit);
    }

    public void setNumberReportsInit(int numberReportsInit) {
        tabs.get(REPORTS_TABLE_NAME).setTotalRecords(numberReportsInit);
    }

    public static String getAssignmentsTableName() {
        return ASSIGNMENTS_TABLE_NAME;
    }

    public static String getReportsTableName() {
        return REPORTS_TABLE_NAME;
    }

    public static String getArchiveTableName() {
        return ARCHIVE_TABLE_NAME;
    }

    public static Analyster getInstance() {
        if (instance == null) {
            instance = new Analyster();
        }
        return instance;
    }

    public JTable getassignmentTable() {
        return assignmentTable;
    }

    public JTable getReportTable() {
        return reportTable;
    }

    public JTable getArchiveAssignTable() {
        return archiveTable;
    }

    public JTabbedPane getjTabbedPanel1() {
        return tabbedPanel;
    }

    public JMenuItem getjActivateRecord() {
        return jActivateRecord;
    }

    public JMenuItem getjArchiveRecord() {
        return jArchiveRecord;
    }

    public JLabel getRecordsLabel() {
        return labelRecords;
    }

    public LogWindow getLogwind() {
        return logwind;
    }

    public List<ModifiedData> getModifiedDataList() {
        return modifiedDataList;
    }

    public TableState getAssignments() {
        return tabs.get(ASSIGNMENTS_TABLE_NAME).getTableState();
    }

    public TableState getReports() {
        return tabs.get(REPORTS_TABLE_NAME).getTableState();
    }

    public TableState getArchiveAssign() {
        return tabs.get(ARCHIVE_TABLE_NAME).getTableState();
    }

    public JTableFilter getFilterTempAssignment() {
        return tabs.get(ASSIGNMENTS_TABLE_NAME).getFilter();
    }
    
        public Map<String, Tab> getTabs() {
        return tabs;
    }

    public void setTabs(Map<String, Tab> tabs) {
        this.tabs = tabs;
    }
    
    public String getSelectedTab() {
        return tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex());
    }

    public LoadTables getLoadTables() {
        return loadTables;
    }
    
    
    
    // @formatter:off
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel addPanel_control;
    private javax.swing.JTable archiveTable;
    private javax.swing.JTable assignmentTable;
    private javax.swing.JButton btnAddRecords;
    private javax.swing.JButton btnBatchEdit;
    private javax.swing.JButton btnCancelEditMode;
    private javax.swing.JButton btnCancelSQL;
    private javax.swing.JButton btnClearAllFilter;
    private javax.swing.JButton btnCloseSQL;
    private javax.swing.JButton btnEnterSQL;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSwitchEditMode;
    private javax.swing.JButton btnUploadChanges;
    private javax.swing.JComboBox comboBoxSearch;
    private javax.swing.JMenuItem jActivateRecord;
    private javax.swing.JMenuItem jArchiveRecord;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemViewLog;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemViewSQL;
    private javax.swing.JMenuItem jDeleteRecord;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelEdit;
    private javax.swing.JMenu jMenuEdit;
    private javax.swing.JMenuItem jMenuEditDB;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenu jMenuFind;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItemFileVersion;
    private javax.swing.JMenuItem jMenuItemLogOff;
    private javax.swing.JMenuItem jMenuItemOtherReport;
    private javax.swing.JMenuItem jMenuItemOthersLoadData;
    private javax.swing.JMenuItem jMenuItemPrintDisplay;
    private javax.swing.JMenuItem jMenuItemPrintGUI;
    private javax.swing.JMenuItem jMenuItemSaveFile;
    private javax.swing.JMenuItem jMenuItemViewActiveAssig;
    private javax.swing.JMenuItem jMenuItemViewAllAssig;
    private javax.swing.JMenuItem jMenuItemViewAssig;
    private javax.swing.JMenuItem jMenuItemViewReports;
    private javax.swing.JMenu jMenuOther;
    private javax.swing.JMenu jMenuPrint;
    private javax.swing.JMenu jMenuReport;
    private javax.swing.JMenu jMenuSelectConn;
    private javax.swing.JMenu jMenuView;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelEdit;
    private javax.swing.JPanel jPanelSQL;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextAreaSQL;
    private javax.swing.JLabel labelRecords;
    private javax.swing.JLabel labelTimeLastUpdate;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JTable reportTable;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTabbedPane tabbedPanel;
    private javax.swing.JTextField textFieldForSearch;
    // End of variables declaration//GEN-END:variables
    // @formatter:on

 
    /**
     *  CLASS 
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
