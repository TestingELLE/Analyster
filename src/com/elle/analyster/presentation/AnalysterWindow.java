package com.elle.analyster.presentation;

import com.elle.analyster.database.DBConnection;
import com.elle.analyster.logic.CreateDocumentFilter;
import com.elle.analyster.logic.ITableConstants;
import com.elle.analyster.entities.Assignment;
import com.elle.analyster.entities.AssignmentArchived;
import com.elle.analyster.admissions.Authorization;
import com.elle.analyster.controller.DataManager;
import com.elle.analyster.dao.AccessLevelDAO;
import com.elle.analyster.logic.AssignmentConverter;
import com.elle.analyster.logic.ArchiveConverter;
import static com.elle.analyster.logic.AssignmentArchiveConverter.archiveToAssignment;
import static com.elle.analyster.logic.AssignmentArchiveConverter.assignmentToArchive;
import com.elle.analyster.logic.TableFilter;
import com.elle.analyster.logic.LoggingAspect;
import com.elle.analyster.logic.ShortCutSetting;
import com.elle.analyster.logic.ArchivesTab;
import com.elle.analyster.logic.AssignmentsTab;
import com.elle.analyster.logic.BaseTab;
import com.elle.analyster.logic.ReportsTab;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ListCellRenderer;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

/**
 * AnalysterWindow
 *
 * @author Carlos Igreja
 * @since June 10, 2015
 * @version 0.6.3
 */
public class AnalysterWindow extends JFrame implements ITableConstants {

    public static String versionDate;  // set automatically from manifest
    public static String version;       // set automatically from manifest
    
    //data manager
    DataManager dataManager; 

    // attributes
    private Map<Integer, BaseTab> tabs; // stores individual tab objects 
    private static Statement statement;
    private String database;
    private String server;
    
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

    // create a jlabel to show the database and server used
    private JLabel databaseLabel;
    private String editingTabName; // stores the name of the tab that is editing
    private String searchValue = "";
    public final static String SEPARATOR = "SEPARATOR";

    
    private boolean comboBoxStartToSearch;
    List<Object> inactiveAnalysts = new ArrayList<Object>();
    
   
    /**
     * CONSTRUCTOR
     */
    public AnalysterWindow() throws Exception {
        
        dataManager = DataManager.getInstance();         
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

        initComponents(); // generated code
        
        //initialize tabs
       
        tabs = new HashMap();
        tabs.put(0, new AssignmentsTab(assignmentTable));
        tabs.put(1, new ReportsTab(reportTable));
        tabs.put(2, new ArchivesTab(archiveTable));
        
        
        // initialize the colors for the edit mode text
        editModeActiveTextColor = new Color(44, 122, 22); //dark green
        editModeDefaultTextColor = labelEditMode.getForeground();

        // this sets the KeyboardFocusManger
        setKeyboardFocusManager();
        
        //set combobox renderer
        comboBoxValue.setRenderer(new ComboBoxRenderer());
        comboBoxValue.addActionListener(new BlockComboListener(comboBoxValue));

        // hide sql panel
        //jTextAreaSQL.setVisible(true);
        jPanelSQL.setVisible(false);
       

        //initialize tab related components
        //including button state, recordsLabel, comboBoxSearchField, addIssue button text ,etc
        BaseTab currentTab = tabs.get(tabbedPanel.getSelectedIndex()); 
        changeTabbedPanelState(currentTab);

      
        // add copy+paste short cut into table and text Area
        InputMap ip = (InputMap) UIManager.get("TextField.focusInputMap");
        InputMap ip2 = this.jTextAreaSQL.getInputMap();
        ShortCut.copyAndPasteShortCut(ip);
        ShortCut.copyAndPasteShortCut(ip2);

        informationLabel.setText("");
        
        
        this.comboBoxValue.setSelectedItem("");
        
        
        // set title of window to Analyster
        this.setTitle("Analyster");
        this.setSize(this.getWidth(), 560);

        setAccessForDeveloper();
        setPanelListeners();
        
        //set tooltips
        btnClearAllFilter.setToolTipText("You can reset the filter here!");

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
        java.awt.GridBagConstraints gridBagConstraints;

        addPanel_control = new javax.swing.JPanel();
        labelTimeLastUpdate = new javax.swing.JLabel();
        searchPanel = new javax.swing.JPanel();
        btnSearch = new javax.swing.JButton();
        comboBoxSearch = new javax.swing.JComboBox();
        btnClearAllFilter = new javax.swing.JButton();
        searchInformationLabel = new javax.swing.JLabel();
        comboBoxValue = new javax.swing.JComboBox();
        labelTotalRecords = new javax.swing.JLabel();
        labelRecordsShown = new javax.swing.JLabel();
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
        About = new javax.swing.JMenuItem();
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
        menuTools = new javax.swing.JMenu();
        menuItemReloadData = new javax.swing.JMenuItem();
        menuItemTurnEditModeOff = new javax.swing.JMenuItem();
        menuItemBackup = new javax.swing.JMenuItem();
        menuItemAddslash = new javax.swing.JMenuItem();
        menuItemStripslash = new javax.swing.JMenuItem();
        menuHelp = new javax.swing.JMenu();
        menuItemRepBugSugg = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(900, 574));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        addPanel_control.setLayout(new java.awt.GridBagLayout());

        labelTimeLastUpdate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelTimeLastUpdate.setText("Last updated: ");
        labelTimeLastUpdate.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        labelTimeLastUpdate.setIconTextGap(0);
        labelTimeLastUpdate.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(-1, 0, 0, 3);
        addPanel_control.add(labelTimeLastUpdate, gridBagConstraints);

        searchPanel.setLayout(new java.awt.GridBagLayout());

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        searchPanel.add(btnSearch, gridBagConstraints);

        comboBoxSearch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "symbol", "analyst" }));
        comboBoxSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        searchPanel.add(comboBoxSearch, gridBagConstraints);

        btnClearAllFilter.setText("Clear All Filters");
        btnClearAllFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearAllFilterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        searchPanel.add(btnClearAllFilter, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 224;
        gridBagConstraints.ipady = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 11, 10);
        searchPanel.add(searchInformationLabel, gridBagConstraints);

        comboBoxValue.setEditable(true);
        comboBoxValue.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxValue.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        comboBoxValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 55;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        searchPanel.add(comboBoxValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        addPanel_control.add(searchPanel, gridBagConstraints);

        labelTotalRecords.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelTotalRecords.setText("labelTotalRecords");
        labelTotalRecords.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        labelTotalRecords.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        labelTotalRecords.setIconTextGap(0);
        labelTotalRecords.setInheritsPopupMenu(false);
        labelTotalRecords.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(-2, 0, 0, 2);
        addPanel_control.add(labelTotalRecords, gridBagConstraints);

        labelRecordsShown.setText("labelRecordsShown");
        labelRecordsShown.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        labelRecordsShown.setIconTextGap(0);
        labelRecordsShown.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 2);
        addPanel_control.add(labelRecordsShown, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 185;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(addPanel_control, gridBagConstraints);

        jPanel5.setMinimumSize(new java.awt.Dimension(900, 400));
        jPanel5.setPreferredSize(new java.awt.Dimension(900, 400));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        tabbedPanel.setMinimumSize(new java.awt.Dimension(850, 120));
        tabbedPanel.setPreferredSize(new java.awt.Dimension(875, 300));
        tabbedPanel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbedPanelStateChanged(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setMinimumSize(new java.awt.Dimension(1072, 150));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(1072, 300));

        assignmentTable.setAutoCreateRowSorter(true);
        assignmentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

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
        assignmentTable.setName("Assignments"); // NOI18N
        assignmentTable.setRequestFocusEnabled(false);
        jScrollPane1.setViewportView(assignmentTable);

        tabbedPanel.addTab("Assignments", jScrollPane1);

        jScrollPane4.setMinimumSize(new java.awt.Dimension(900, 300));
        jScrollPane4.setPreferredSize(new java.awt.Dimension(1072, 400));

        reportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "symbol", "analyst", "analysisDate", "path", "document", "decision", "notes"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        reportTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        reportTable.setMinimumSize(new java.awt.Dimension(1500, 400));
        reportTable.setName("Reports"); // NOI18N
        jScrollPane4.setViewportView(reportTable);

        tabbedPanel.addTab("Reports", jScrollPane4);

        jScrollPane3.setMinimumSize(new java.awt.Dimension(900, 300));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(1072, 400));

        archiveTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "dateArchived", "aID", "symbol", "analyst", "priority", "dateAssigned", "dateDone", "notes"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, true, true, true, true, true, true
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
        archiveTable.setName("Assignments_Archived"); // NOI18N
        jScrollPane3.setViewportView(archiveTable);

        tabbedPanel.addTab("Assignments_Archived", jScrollPane3);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 274;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel5.add(tabbedPanel, gridBagConstraints);
        tabbedPanel.getAccessibleContext().setAccessibleName("Reports");
        tabbedPanel.getAccessibleContext().setAccessibleParent(tabbedPanel);

        jPanelEdit.setPreferredSize(new java.awt.Dimension(636, 180));
        jPanelEdit.setLayout(new java.awt.GridBagLayout());

        btnBatchEdit.setText("Batch Edit");
        btnBatchEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatchEditActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        jPanelEdit.add(btnBatchEdit, gridBagConstraints);

        btnAddRecords.setText("Add Record(s)");
        btnAddRecords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRecordsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanelEdit.add(btnAddRecords, gridBagConstraints);

        btnUploadChanges.setText("Upload Changes");
        btnUploadChanges.setMaximumSize(new java.awt.Dimension(95, 30));
        btnUploadChanges.setMinimumSize(new java.awt.Dimension(95, 30));
        btnUploadChanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadChangesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, -15, 0, 0);
        jPanelEdit.add(btnUploadChanges, gridBagConstraints);

        labelEditModeState.setText("OFF");
        labelEditModeState.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelEditModeStateMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        jPanelEdit.add(labelEditModeState, gridBagConstraints);

        labelEditMode.setText("Edit Mode:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanelEdit.add(labelEditMode, gridBagConstraints);

        btnRevertChanges.setText("Revert Changes");
        btnRevertChanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRevertChangesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanelEdit.add(btnRevertChanges, gridBagConstraints);

        informationLabel.setText("Information Label");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 3, 2, 0);
        jPanelEdit.add(informationLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 195;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel5.add(jPanelEdit, gridBagConstraints);

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
                .addGap(0, 0, 0)
                .addGroup(jPanelSQLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCancelSQL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEnterSQL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCloseSQL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1935, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        jPanelSQLLayout.setVerticalGroup(
            jPanelSQLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSQLLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanelSQLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                    .addGroup(jPanelSQLLayout.createSequentialGroup()
                        .addComponent(btnEnterSQL)
                        .addGap(0, 0, 0)
                        .addComponent(btnCancelSQL)
                        .addGap(0, 0, 0)
                        .addComponent(btnCloseSQL)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(2, 2, 2))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 811;
        gridBagConstraints.ipady = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel5.add(jPanelSQL, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jPanel5, gridBagConstraints);

        menuFile.setText("File");

        About.setText("About");
        About.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AboutActionPerformed(evt);
            }
        });
        menuFile.add(About);

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
        menuItemRepBugSugg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemRepBugSuggActionPerformed(evt);
            }
        });
        menuHelp.add(menuItemRepBugSugg);

        menuBar.add(menuHelp);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
    
    // Corinne
    private void menuItemRepBugSuggActionPerformed(java.awt.event.ActionEvent evt){}
    
    //search and filter current tab
    // modified by Yi
    // 08-02-2016
    public void filterBySearch() {
    
        BaseTab tab = tabs.get(tabbedPanel.getSelectedIndex());
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
                
                filter.addFilterItem(col, searchBoxValue);
                filter.applyFilter();

            }

        }

    }

   
    // not sure what this is
    //commented out all code by Yi
    private void menuItemAWSAssignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAWSAssignActionPerformed

//        try {
//            loadTable(assignmentTable);
//        } catch (Exception ex) {
//            Logger.getLogger(AnalysterWindow.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        try {
//            loadTable(reportTable);
//        } catch (Exception ex) {
//            Logger.getLogger(AnalysterWindow.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }//GEN-LAST:event_menuItemAWSAssignActionPerformed

    private void btnUploadChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadChangesActionPerformed

        try {
            BaseTab currentTab = tabs.get(tabbedPanel.getSelectedIndex());
            currentTab.uploadChanges();
        } catch (Exception ex) {
            Logger.getLogger(AnalysterWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnUploadChangesActionPerformed


    private void btnEnterSQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnterSQLActionPerformed

        int commandStart = jTextAreaSQL.getText().lastIndexOf(">>") + 2;
        String command = jTextAreaSQL.getText().substring(commandStart);
        if(sqlOutputWindow == null){
            try { 
                sqlOutputWindow = new SqlOutputWindow(command,this);
            } catch (Exception ex) {
                Logger.getLogger(AnalysterWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            sqlOutputWindow.setLocationRelativeTo(this);
            sqlOutputWindow.toFront();
            try {
                sqlOutputWindow.setTableModel(command);
            } catch (Exception ex) {
                Logger.getLogger(AnalysterWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
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
     * @author Yi 7/28/2016
     * changeTabbedPanelState
     * getting states from panel, and set up the pwWindow components
     */

    public void changeTabbedPanelState(BaseTab tab) {
        
        //comboBoxField drop down reset
        String[] searchFields = tab.getSearchFields();
        /*if (searchFields != null) {
            comboBoxSearch.setModel(new DefaultComboBoxModel(searchFields));
        }*/
        //populate comboxValue drop down
        String searchContent = comboBoxSearch.getSelectedItem().toString();
        this.updateComboList(searchContent, tab);
        this.comboBoxValue.setSelectedItem("");
        
        
            
        //labelRecords reset for # of records andn # of records shown
        labelTotalRecords.setText(tab.getTotalRecordsLabel());
        labelRecordsShown.setText(tab.getRecordsShownLabel());
        
        //set buttons state
        setButtonsState(tab);
    

        //Authorization.authorize(this);
    
    }
    
    
     /**
     * set button state from tab
     * @author Yi
     * @since 07/28/2016
     *
     * @param baseTab
     */
    private void setButtonsState(BaseTab tab) {
        
        //buttons
        btnAddRecords.setVisible(tab.getState().isAddBtnVisible());
        btnBatchEdit.setVisible(tab.getState().isBatchEditBtnVisible());
        btnUploadChanges.setVisible(tab.getState().isUploadChangesBtnVisible());
        btnRevertChanges.setVisible(tab.getState().isRevertChangesBtnVisible());
        if(tab.getState().isEditMode()) {
            labelEditModeState.setText("ON");
            
        }
        else labelEditModeState.setText("OFF");
        
        editModeTextColor(tab.getState().isEditMode());
        
        //menuItems
        menuItemActivateRecord.setEnabled(tab.getState().isActivateRecordEnabled());
        menuItemArchiveRecord.setEnabled(tab.getState().isArchiveRecordEnabled());
        menuItemOpenDocument.setEnabled(tab.getState().isOpenDocumentEnabled());
        menuItemStripslash.setEnabled(tab.getState().isStripSlashEnabled());
        menuItemAddslash.setEnabled(tab.getState().isAddSlashEnabled());
        
                
    }

    
    
    
    private void btnBatchEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatchEditActionPerformed

        
        // get selected tab
        
        BaseTab tab = tabs.get(tabbedPanel.getSelectedIndex());
        JTable table = tab.getTable();
        int[] rows = table.getSelectedRows();

        // set the tab state to editing
        //hide add button, show changes btns, set editmode
        tab.getState().enableEdit(true);
        changeTabbedPanelState(tab);
        
        //open batch edit window
        // open a batch edit window and make visible only to this tab
        batchEditWindow = new BatchEditWindow(tab);
        this.setEnabled(false);
        batchEditWindow.setVisible(true);

    }//GEN-LAST:event_btnBatchEditActionPerformed

    private void menuItemManageDBsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemManageDBsActionPerformed
        try {
            editDatabaseWindow = new EditDatabaseWindow();
        } catch (Exception ex) {
            Logger.getLogger(AnalysterWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        editDatabaseWindow.setLocationRelativeTo(this);
        editDatabaseWindow.setVisible(true);
    }//GEN-LAST:event_menuItemManageDBsActionPerformed

    /**
     * btnAddRecordsActionPerformed
     *
     * @param evt
     */
    private void btnAddRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRecordsActionPerformed
        BaseTab currentTab = tabs.get(tabbedPanel.getSelectedIndex());
        // if no add records window is open
        if (addRecordsWindow == null || !addRecordsWindow.isDisplayable()) {
            addRecordsWindow = new AddRecordsWindow(currentTab);
            addRecordsWindow.setVisible(true);
        } // if window is already open then set the focus
        else {
            addRecordsWindow.toFront();
        }

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
            case 0: {               try {
                // Reconnect
                
                // create a new Login Window
                loginWindow = new LoginWindow();
            } catch (Exception ex) {
                Logger.getLogger(AnalysterWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
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

    /**?
     * menuItemDeleteRecordActionPerformed Delete records menu item action
     * performed
     *
     * @param evt
     */
    private void menuItemDeleteRecordActionPerformed(java.awt.event.ActionEvent evt) {
        
        try {
            
            BaseTab tab = tabs.get(tabbedPanel.getSelectedIndex());
            JTable table = tab.getTable();
            int[] selectedRows = table.getSelectedRows(); 
            
            //get ids for selected records
            int[] ids = new int[selectedRows.length];
            
            for(int i = 0; i< selectedRows.length; i++) {
                ids[i] = (int) table.getValueAt(selectedRows[i], 0);
                
            }
            //remove records from datamanager
            switch(table.getName()) {
                case "Assignments" : {
                    dataManager.deleteAssignments(ids);
                    break;
                }
                
                case "Reports" : {
                    dataManager.deleteReports(ids);
                    break;
                }
                
                case "Assignments_Archived" :{
                    dataManager.deleteArchives(ids);
                    break;
                }
                default : break;
                    
            }
            
            //remove rows from table
            for(int index = selectedRows.length -1; index >= 0; index --) {
                tab.deleteRow(selectedRows[index]);
                
            }
            
            
           
        } catch (Exception ex) {
            Logger.getLogger(AnalysterWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    
    /*end of disabled functions */
    
    /**
     * btnClearAllFilterActionPerformed clear all filters
     *
     * @param evt
     */
    private void btnClearAllFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearAllFilterActionPerformed

        String totalRecordsLabel = "";
        String recordsShownLabel = "";
        // clear all filters
        //      String tabName = getSelectedTabName();
        for (Map.Entry<Integer, BaseTab> entry : tabs.entrySet()) {
            BaseTab tab = tabs.get(entry.getKey());
            TableFilter filter = tab.getFilter();
            filter.clearAllFilters();
            filter.applyFilter();
            filter.applyColorHeaders();
            /*Author:Swapna
            Date;18th October 2017
            Comments: Blank string is required
            */
            totalRecordsLabel = ""+ tab.getTotalRecordsLabel();
            recordsShownLabel = tab.getRecordsShownLabel();

        }
        labelTotalRecords.setText(totalRecordsLabel);
        labelRecordsShown.setText(recordsShownLabel);
        System.out.println(totalRecordsLabel+ "/n" + recordsShownLabel);
    }//GEN-LAST:event_btnClearAllFilterActionPerformed

    /**
     * jMenuItemOthersLoadDataActionPerformed
     *
     * @param evt
     */
    private void menuItemReloadDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemReloadDataActionPerformed

        try {
            reloadDataAction();
        } catch (Exception ex) {
            Logger.getLogger(AnalysterWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        String text = "Data reloaded!";
        setInformationLabel(text, 5);
        logWindow.addMessageWithDate(text);
        System.out.println(text);
    }//GEN-LAST:event_menuItemReloadDataActionPerformed

    // reload the data
    private void reloadDataAction() throws Exception {
        
        BaseTab tab = tabs.get(tabbedPanel.getSelectedIndex());

        // reload table from dataManager, this is a soft reload
        
        tab.reloadTable();
        
    }

    /**
     * jArchiveRecordActionPerformed
     *
     * @param evt
     * modified by Yi
     * since 08/03/2016
     */
    private void menuItemArchiveRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemArchiveRecordActionPerformed

        //converters required 
        AssignmentConverter converter = new AssignmentConverter();
        //tab 0 is assignmentTab
        //tab 2 is archiveTab
        BaseTab assignmentTab = tabs.get(0);
        BaseTab archiveTab = tabs.get(2);
        
        //array store archives
        ArrayList<AssignmentArchived> archives = new ArrayList();
        
        int[] selectedRows = assignmentTab.getTable().getSelectedRows();
        if (selectedRows.length > 0) {
            for(int rowIndex : selectedRows) {
                //get row data
                Object[] data = assignmentTab.getRowData(rowIndex);
                //convert row data to assignment
                Assignment assignment = converter.convertFromRow(data);
                //convert assignment to archive
                AssignmentArchived archive = assignmentToArchive(assignment);
                archives.add(archive);
            }
            
            //update data
            dataManager.insertArchives(archives);
            
            //refresh archiveTab
            archiveTab.reloadTable();
        }
        
       
        



    }//GEN-LAST:event_menuItemArchiveRecordActionPerformed

    /**
     * tabbedPanelStateChanged
     *
     * @param evt
     */
    private void tabbedPanelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbedPanelStateChanged

        if (tabs != null) {
            BaseTab tab = tabs.get(tabbedPanel.getSelectedIndex());
            
            //set up related JComponents 
            changeTabbedPanelState(tab);
        }
        
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
        BaseTab tab = tabs.get(tabbedPanel.getSelectedIndex());
        try {
            tab.revertChanges();
        } catch (Exception ex) {
            Logger.getLogger(AnalysterWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            if (DBConnection.open()) {  // open a new connection
                BackupDBTablesDialog backupDBTables = new BackupDBTablesDialog(this);
            } else {
                JOptionPane.showMessageDialog(this, "Could not connect to Database");
            }
        } catch (Exception ex) {
            Logger.getLogger(AnalysterWindow.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_menuItemBackupActionPerformed

    
    private void menuItemTurnEditModeOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemTurnEditModeOffActionPerformed
        turnOffEditMode();
        
    }//GEN-LAST:event_menuItemTurnEditModeOffActionPerformed

    private void turnOffEditMode() {
        BaseTab tab = tabs.get(tabbedPanel.getSelectedIndex());
        //change tab state
        tab.getState().enableEdit(false);
        tab.makeTableEditable(false);
        changeTabbedPanelState(tab);
        
        String text = "Edit mode turned off!";
        setInformationLabel(text, 5);
        logWindow.addMessageWithDate(text);
        System.out.println(text);
        
    }
    
    /**
     * jActivateRecordActionPerformed
     *
     * @param evt
     * modified by Yi
     * since 08/03/2016
     */
    private void menuItemActivateRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemActivateRecordActionPerformed

        //converters required 
        ArchiveConverter converter = new ArchiveConverter();
        //tab 0 is assignmentTab
        //tab 2 is archiveTab
        BaseTab assignmentTab = tabs.get(0);
        BaseTab archiveTab = tabs.get(2);
        
        //array store archives
        ArrayList<Assignment> assignments = new ArrayList();
        
        int[] selectedRows = archiveTab.getTable().getSelectedRows();
        if (selectedRows.length > 0) {
            for(int rowIndex : selectedRows) {
                //get row data
                Object[] data = archiveTab.getRowData(rowIndex);
                //convert row data to assignment
                AssignmentArchived archive = converter.convertFromRow(data);
                //convert assignment to archive
                Assignment assignment = archiveToAssignment(archive);
                assignments.add(assignment);
            }
            
            //update data
            dataManager.insertAssignments(assignments);
            
            //refresh archiveTab
            assignmentTab.reloadTable();
        }
       
    }//GEN-LAST:event_menuItemActivateRecordActionPerformed

    
    private void comboBoxSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxSearchActionPerformed
        comboBoxStartToSearch = false;
        //get search string
        String searchColName = comboBoxSearch.getSelectedItem().toString();
        searchValue = comboBoxValue.getSelectedItem().toString();
        
        //get current tab
        BaseTab tab = tabs.get(tabbedPanel.getSelectedIndex());

        updateComboList(searchColName, tab);

        comboBoxValue.setSelectedItem(searchValue);


    }//GEN-LAST:event_comboBoxSearchActionPerformed

    public void comboBoxForSearchMouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            comboBoxValue.getEditor().selectAll();
        } else if (e.isControlDown()) {
            comboBoxValue.showPopup();

        }
    }
    
   /* commented by Yi
   * add and strip slash functions involve bucky changes
   * did not change the db logics
   * only changed the reloading process using dataManager and baseTab
   */
    private void menuItemStripslashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemStripslashActionPerformed
        // Pick out the selected tab ,only reportsTab is selected
        BaseTab tab = tabs.get(tabbedPanel.getSelectedIndex());

        int sqlChangeNum = 0;
        //      String sqlChange = "";
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";

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
                //this.makeTableEditable(true);
            }
        }
        if (sqlChangeNum > 0) {
            sql3 = sql3.substring(0, sql3.length() - 1);
            String sql = sql1 + sql2 + " END \n" + "WHERE ID IN (" + sql3 + ");";
            System.out.println(sql);
            System.out.println(sqlChangeNum);
            DBConnection.close();
            try {
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
            } catch (Exception ex) {
                Logger.getLogger(AnalysterWindow.class.getName()).log(Level.SEVERE, null, ex);
            }

            DBConnection.close();
        }
        //hard reload dataManager report Table
        dataManager.getControllers().get("report").getAll();

        //reload table
        tab.reloadTable();

    }//GEN-LAST:event_menuItemStripslashActionPerformed

    private void menuItemAddslashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAddslashActionPerformed

        // Pick out the selected tab
        BaseTab tab = tabs.get(tabbedPanel.getSelectedIndex());
        int sqlChangeNum = 0;
        //      String sqlChange = "";
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";

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
                // this.makeTableEditable(true);
            }

        }
        if (sqlChangeNum > 0) {
            sql3 = sql3.substring(0, sql3.length() - 1);
            String sql = sql1 + sql2 + " END \n" + "WHERE ID IN (" + sql3 + ");";
            System.out.println(sql);
            DBConnection.close();
            try {
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
            } catch (Exception ex) {
                Logger.getLogger(AnalysterWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
            DBConnection.close();
        }
        //hard reload dataManager report Table
        dataManager.getControllers().get("report").getAll();

        //reload table
        tab.reloadTable();



    }//GEN-LAST:event_menuItemAddslashActionPerformed

    private void menuItemOpenDocumentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemOpenDocumentActionPerformed
        //tabs 1 is reportsTab
        ReportsTab reportsTab = (ReportsTab)tabs.get(1);
        reportsTab.openDocumentTool();
        
    }//GEN-LAST:event_menuItemOpenDocumentActionPerformed

    private void comboBoxValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxValueActionPerformed
        if (comboBoxValue.getSelectedItem() != null){
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

    //this is turning off edit mode
    private void labelEditModeStateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelEditModeStateMouseClicked
        if (labelEditModeState.getText().equals("ON ")) {
            turnOffEditMode();
        }
    }//GEN-LAST:event_labelEditModeStateMouseClicked

   
    /* New About Menu option found in the File menu.  
     This will display the Splash Screen, Version Date and Version Number in a 
     showMessageDialog box.
     By Tom Tran
    Date 2018-05-17
    */
    
    private void AboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AboutActionPerformed
   
        // new image Icon object for the splash screen
        //final ImageIcon bg = new ImageIcon ("src/com/elle/analyster/splashImage.png");  
        
        // Creates showMessageDialog box and shows the Splash Screen, Version Date and Version Number.
        
        JOptionPane.showMessageDialog(null,"Analyster" + "\n" + "Version Date: "
                + versionDate + "\n"
                + "Version: " + version, "About", 
                JOptionPane.PLAIN_MESSAGE, 
                new ImageIcon(AnalysterWindow.class.getResource("splashImage.png")));       // Fixed this line to have the image show up in the jar 2018-05-29.  Ensure that image is in the classes folder for this code to work.
    }//GEN-LAST:event_AboutActionPerformed

    
    //set the timer for information Label show
    public static void startCountDownFromNow(int waitSeconds) {
        Timer timer = new Timer(waitSeconds * 1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                informationLabel.setText("v."+version);
                searchInformationLabel.setText("");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * loadData
     */
    public void loadData() throws Exception {
//        loadTables(tabs);
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
     * setKeyboardFocusManager sets the keyboard focus manager
     */
    private void setKeyboardFocusManager() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                BaseTab tab = tabs.get(tabbedPanel.getSelectedIndex());
                if (tab.getState().isEditMode()) {
                    
                    JTable table = tab.getTable();
                    int row = table.getSelectedRow();
                    int column = table.getSelectedColumn();
                    int columnCount = table.getColumnCount();
                    int rowCount = table.getRowCount();
                    if (e.getKeyCode() == KeyEvent.VK_TAB) {
                        if (e.getComponent() instanceof JTable) {
                            
                            tableCellSelection(e, table, row, column);
                           
                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        if (e.getID() == KeyEvent.KEY_RELEASED) {
                            if (e.getComponent() instanceof JTable) {

                               
                                if (row == table.getRowCount()) {
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
                                    table.changeSelection(row, column, false, false);
                                    tableCellSelection(e, table, row, column);
                                }

                            } else {
                                
                                if (column != 0) {
                                    column = column - 1;
                                }
//                                System.out.println("left now at: " + row + " " + column );
                                table.changeSelection(row, column, false, false);
                                tableCellSelection(e, table, row, column);
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
                               
                            }
                        }
                    }
                    if (e.getKeyCode() == KeyEvent.VK_Z && e.isMetaDown()) {

                    }
                }
                
                    if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {
                        if (tab.getState().isEditMode()) { 
                            
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

    public JLabel getLabelTotalRecords() {
        return labelTotalRecords;
    }
    
    public JLabel getLabelRecordsShown(){
        return labelRecordsShown;
    }

    public LogWindow getLogWindow() {
        return logWindow;
    }

    public Map<Integer, BaseTab> getTabs() {
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

   

    public ArrayList getInactiveAnalysts() throws Exception{
        String sql = "SELECT * FROM analysts WHERE status = 'INACTIVE'";
        ResultSet rs = null;
        ArrayList <Object> inactiveAnalysts = new ArrayList<Object>();
        try {

            DBConnection.close();
            DBConnection.open();
            rs = DBConnection.getStatement().executeQuery(sql);
            
            while(rs.next()){
                inactiveAnalysts.add(rs.getObject("name"));
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
    

    /* 
    * @modified by Yi
    * @date 08-03-2016
    */
    private void updateComboList(String colName, BaseTab tab) {
        //create a combo box model
        DefaultComboBoxModel comboBoxSearchModel = new DefaultComboBoxModel();
        comboBoxValue.setModel(comboBoxSearchModel);
        
        JTable table = tab.getTable();
        
        
        //loading values for column
        Map<Integer, ArrayList<Object>> comboBoxForSearchValue  = tab.loadingDropdownList();
        ArrayList<Object> values =  null;
        for (int col = 0; col < table.getColumnCount(); col++) {
            if (table.getColumnName(col).equalsIgnoreCase(colName)) {
                values = (ArrayList<Object>) comboBoxForSearchValue.get(col);
            }
        }
        
        //if analyst column, need to add seperator
        if (colName.equalsIgnoreCase("analyst")) {
            //sort the values into active vs nonactive
            Collections.sort(values, new AnalystComparator());
            for (int i = 0; i < values.size(); i++) {
                //find the start index of nonactive, add separator
                String currentAnalyst = values.get(i).toString();
                if (inactiveAnalysts.contains(currentAnalyst)) {
                    values.add(i, SEPARATOR);
                    break;
                }
                
            }
        }
        
        
        else {
            //process path column values
            if (colName.equalsIgnoreCase("path")) {
                for(Object item : values) {
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
                }      
            }
            
            //general sorting for column values
            Collections.sort(values, new Comparator<Object>() {
                public int compare(Object o1, Object o2) {
                    return o1.toString().compareTo(o2.toString());
                }
            });

       }
       
        //add values to combobox searchmodel
        for(Object value: values) {
            comboBoxSearchModel.addElement(value);
        }
        
        
       
    }
    

   //custom renderer used to render the list of analysts in comboBoxValue with a seperator line
    //by Corinne Martus
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
    
     //custom combo box listener applied to the list of analysts in comboBoxValue 
    // prevents the seperator line from being selected
    //by Corinne Martus
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
    public void setServer(String server){
        this.server = server;
    }

     public void showDatabase() {
        databaseLabel = new JLabel(
            server + "." + database);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(databaseLabel);
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
        return labelTotalRecords;
    }

    public void setLabelRecords(JLabel labelRecords) {
        this.labelTotalRecords = labelRecords;
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

    public DataManager getDataManager() {
        return dataManager;
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    
    

    // @formatter:off
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem About;
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
    private javax.swing.JLabel labelRecordsShown;
    private javax.swing.JLabel labelTimeLastUpdate;
    private javax.swing.JLabel labelTotalRecords;
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
        
        if (database.equalsIgnoreCase("pupone_Analyster")) {
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

}
