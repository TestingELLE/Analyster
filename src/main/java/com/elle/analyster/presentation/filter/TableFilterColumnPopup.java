
package com.elle.analyster.presentation.filter;

/**
 *
 * @author danielabecker
 */


import com.elle.analyster.Analyster;
import com.elle.analyster.CommandAction;
import com.elle.analyster.GUI;
import com.elle.analyster.ResizablePopupMenu;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.PopupMenuEvent;

class TableFilterColumnPopup implements MouseListener {

    // class attributes
    private final CheckList<DistinctColumnItem> filterList = new CheckList.Builder().build(); // this calls the build method
    private final Map<Integer, ColumnAttrs> colAttrs = new HashMap<Integer, ColumnAttrs>(); // column Attributes?
    private Dimension defaultSize = new Dimension(100,100);
    private boolean enabled = false;
    private int mColumnIndex = -1;
    private boolean actionsVisible = true;
    private boolean useTableRenderers = false;
    
    // components
    private JTableFilter filter; // JTable filter
    private TableModel myTableModelInitial; // initial table model
    private GUI gui = new GUI(); // this is usually static and an instance is usually not used
    private CommandAction commandAction; // this is the actions for the buttons (Apply & Cancel)
    private ResizablePopupMenu menu; // JPopupMenu
    private DefaultCheckListModel<DistinctColumnItem> model;
    private ActionCheckListModel actionCheckListModel;

    /**
     * CONSTRUCTOR
     * TableFilterColumnPopup
     * @param filter 
     */
    public TableFilterColumnPopup(JTableFilter filter) {

        // ResizablePopupMenu is a JPopupMenu
        menu = new ResizablePopupMenu( true ) {

            private static final long serialVersionUID = 1L;

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                if ( menu.getComponentCount() == 0 ) {
                    JComponent content = buildContent(); // this builds the window JPanel
                    defaultSize = content.getPreferredSize();
                    
                    menu.add( content ); //add JPanel with content to the JPopupMenu

                }
                beforeShow();
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                beforeHide();
            }

        };

        this.filter = filter;
        
        // CheckList<DistinctColumnItem>
        // returns a JList and sets its visible row count to 6
        filterList.getList().setVisibleRowCount(6); 

        // set up the table header of the table from the filter
        // and add a mouselistener
        setupTableHeader();
        
        // add property change listener to setup the table header
        filter.getTable().addPropertyChangeListener("tableHeader", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                setupTableHeader();
            }
        });
        
        // add property change listener to clear the colAttrs hash map
        filter.getTable().addPropertyChangeListener("model", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                colAttrs.clear(); // clear hash map
            }
        });

    }

    /**
     * setActionsVisible
     * @param actionsVisible 
     */
    public void setActionsVisible(boolean actionsVisible) {
        this.actionsVisible = actionsVisible;
    }

    /**
     * setUseTableRenderers
     * @param reuseRenderers 
     */
    public void setUseTableRenderers(boolean reuseRenderers) {
        this.useTableRenderers = reuseRenderers;
    }

    /**
     * setupTableHeader
     */
    private void setupTableHeader() {
        JTableHeader header = filter.getTable().getTableHeader();
        if (header != null) {
            header.addMouseListener(this);
        }
    }

    /**
     * buildContent
     * @return 
     * 
     * This builds the JPanel that has a list for the checkbox items
     * and a command box for the command buttons apply & cancel
     */
    protected JComponent buildContent() {
        JPanel owner = new JPanel(new BorderLayout(3, 3));
        owner.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        owner.setPreferredSize(new Dimension(250, 300)); // default popup size

        Box commands = new Box(BoxLayout.LINE_AXIS);

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setOpaque(false);
        commands.add(toolbar);

        commands.add(Box.createHorizontalGlue());
        
        // create apply action performed and set menu
        commandAction = new CommandAction("Apply"){

            @Override
            protected boolean perform() {
                return applyColumnFilter();
            }
        };
        
        commandAction.setMenu(this.getMenu());

        JButton apply = new JButton(commandAction);
        
        commands.add(apply);
        
        // add commandAction for the cancel button and set the menu
        commandAction = new CommandAction("Cancel");
        commandAction.setMenu(this.getMenu()); // pass this menu reference

        commands.add(Box.createHorizontalStrut(5));
        commands.add(new JButton(commandAction));
        commands.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
        commands.setBackground(UIManager.getColor("Panel.background"));
        commands.setOpaque(true);
        // owner is the JPanel
        owner.add(new JScrollPane(filterList.getList()), BorderLayout.CENTER); // add list to center
        owner.add(commands, BorderLayout.SOUTH); // add command buttons to south of panel

        return owner; // return JPanel

    }

    /**
     * applyColumnFilter
     * @return 
     * 
     * This is the action performed for apply button
     */
    public boolean applyColumnFilter() {
        
        // apply filter
        Collection<DistinctColumnItem> checked = filterList.getCheckedItems();
        ICheckListModel<DistinctColumnItem> model = filterList.getModel();
        myTableModelInitial = filter.getTable().getModel();
        model.filter("", CheckListFilterType.CONTAINS); // clear filter to get true results
        filter.apply(mColumnIndex, checked);
        filter.saveFilterCriteria(checked);
        filter.setColumnIndex(mColumnIndex);
        gui.columnFilterStatus(mColumnIndex, filter.getTable());
        
        // update records
        Analyster analyster = Analyster.getInstance();
        String selectedTab = analyster.getSelectedTab();
        String labelMsg = analyster.getTabs().get(selectedTab).getRecordsLabel();
        analyster.getRecordsLabel().setText(labelMsg);
        
        return true;
    }

    /**
     * getMyTableModelInitial
     * @return 
     */
    TableModel getMyTableModelInitial() {
        return myTableModelInitial;
    }

    /**
     * getFilter
     * @return 
     */
    public JTableFilter getFilter() {
        return filter;
    }

    /**
     * getTable
     * @return 
     */
    public JTable getTable() { 
        return filter.getTable();
    }

    /**
     * isEnabled
     * @return 
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * setEnabled
     * @param enabled 
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    
    // HERE excecute popup windows
    // Popup menus are triggered differently on different platforms
    // Therefore, isPopupTrigger should be checked in both mousePressed and mouseReleased
    // events for for proper cross-platform functionality
    //
    // This is the mouselistener for the check list window
    // this window is right-click on table header or ctrl-click depending on platform
    
    /**
     * mousePressed
     * @param e 
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (enabled && e.isPopupTrigger()) {
            showFilterPopup(e);
        }
    }

    /**
     * mouseReleased
     * @param e 
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (enabled && e.isPopupTrigger()) {
            showFilterPopup(e);
        }
    }


    /**
     * showFilterPopup
     * @param e 
     */
    private void showFilterPopup(MouseEvent e) {
        
        // these are Java API Components
        JTableHeader header = (JTableHeader) (e.getSource());
        TableColumnModel colModel = filter.getTable().getColumnModel();

        // The index of the column whose header was clicked
        int vColumnIndex = colModel.getColumnIndexAtX(e.getX());
        if (vColumnIndex < 0) {
            return;
        }

        // Determine if mouse was clicked between column heads
        Rectangle headerRect = filter.getTable().getTableHeader().getHeaderRect(vColumnIndex);
        if (vColumnIndex == 0) {
            headerRect.width -= 2;
        } else {
            headerRect.grow(-2, 0);
        }

        // Mouse was clicked between column heads
        if (!headerRect.contains(e.getX(), e.getY())) {
            return;
        }

        // restore popup's size for the column
        mColumnIndex = filter.getTable().convertColumnIndexToModel(vColumnIndex);
        setPreferredSize(getColumnAttrs(vColumnIndex).preferredSize);

        // get Collection<DistinctColumnItem>  for column index
        Collection<DistinctColumnItem> distinctItems = filter.getDistinctColumnItems(mColumnIndex);

        // new DefaultCheckListModel passed Collection<DistinctColumnItem> 
        model = new DefaultCheckListModel<>(distinctItems);
        
        // pass that model to ActionCheckListModel
        actionCheckListModel = new ActionCheckListModel<>(model);
        
        // filterList = Collection<DistinctColumnItem> 
        filterList.setModel(actionsVisible ? actionCheckListModel : model);
        
        Collection<DistinctColumnItem> checked = filter.getFilterState(mColumnIndex);

        // replace empty checked items with full selection
        filterList.setCheckedItems(CollectionUtils.isEmpty(checked) ? distinctItems : checked);

        if (useTableRenderers) {
            filterList.getList().setCellRenderer(new TableAwareCheckListRenderer(filter.getTable(), vColumnIndex));
        }

        // show pop-up
        show(header, headerRect.x, header.getHeight());
    }

    /**
     * getColumnAttrs
     * @param column
     * @return 
     */
    private ColumnAttrs getColumnAttrs(int column) {
        ColumnAttrs attrs = colAttrs.get(column);
        if (attrs == null) {
            attrs = new ColumnAttrs();
            colAttrs.put(column, attrs);
        }

        return attrs;
    }

    /**
     * beforeHide
     */
    public void beforeHide() {
        // save pop-up's dimensions before pop-up becomes hidden
        getColumnAttrs(mColumnIndex).preferredSize = getPreferredSize();
    }

    /**
     * mouseClicked
     * @param e 
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * mouseEntered
     * @param e 
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * mouseExited
     * @param e 
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * CLASS
     * ColumnAttrs
     */
    static class ColumnAttrs {

        public Dimension preferredSize; // No methods?
    }

    
    /**************************************************************************
     * ******************** PopupWindow Methods *******************************
     **************************************************************************/
    
    /**
     * getDefaultSize
     * @return 
     */
    public final Dimension getDefaultSize() {
        return defaultSize;
    }

    /**
     * getPreferredSize
     * @return 
     */
    public final Dimension getPreferredSize() {
        return menu.getPreferredSize();
    }

    /**
     * setPreferredSize
     * @param preferredSize 
     */
    public final void setPreferredSize( Dimension preferredSize ) {
        menu.setPreferredSize(preferredSize);
    }


    /**
     * Shows Popup in predefined location
     * @param invoker
     * @param x
     * @param y
     */
    public void show( Component invoker, int x, int y ) {
        menu.show( invoker, x, y );
    }

    /**
     * Shows popup in predefined location
     * @param invoker
     * @param location
     */
    public void show( Component invoker, Point location ) {
        show( invoker, location.x, location.y );
    }

    /**
     * beforeShow
     */
    protected void beforeShow() {}

    /**
     * getMenu
     * @return 
     */
    public JPopupMenu getMenu() {
        return menu;
    }
}
