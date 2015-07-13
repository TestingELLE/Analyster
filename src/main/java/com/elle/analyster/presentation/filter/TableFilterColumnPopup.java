
package com.elle.analyster.presentation.filter;

/**
 *
 * @author danielabecker
 */


import com.elle.analyster.CommandAction;
import com.elle.analyster.GUI;
import com.elle.analyster.PopupWindow;

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

class TableFilterColumnPopup extends PopupWindow implements MouseListener {

    // this calls the build method
    private final CheckList<DistinctColumnItem> filterList = new CheckList.Builder().build();
    
    // column Attributes?
    private final Map<Integer, ColumnAttrs> colAttrs = new HashMap<Integer, ColumnAttrs>();
    
    
    private boolean enabled = false;
    private int mColumnIndex = -1;
    private JTableFilter filter;
    private boolean actionsVisible = true;
    private boolean useTableRenderers = false;
    private TableModel myTableModelInitial;
    private GUI gui = new GUI();

    @SuppressWarnings("static-access")
    public TableFilterColumnPopup(JTableFilter filter) {

        super(true); // PopupWindow( resizable = true )

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

    public void setActionsVisible(boolean actionsVisible) {
        this.actionsVisible = actionsVisible;
    }

    public void setUseTableRenderers(boolean reuseRenderers) {
        this.useTableRenderers = reuseRenderers;
    }

    private void setupTableHeader() {
        JTableHeader header = filter.getTable().getTableHeader();
        if (header != null) {
            header.addMouseListener(this);
        }
    }

    @SuppressWarnings("serial")
    @Override
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
        CommandAction applyBtnCommandAction = new CommandAction("Apply"){

            @Override
            protected boolean perform() {
                return applyColumnFilter();
            }
        };
        
        applyBtnCommandAction.setMenu(this.getMenu());

        JButton apply = new JButton(applyBtnCommandAction);
        
        commands.add(apply);
        
        // add commandAction for the cancel button and set the menu
        CommandAction cancelBtnCommandAction = new CommandAction("Cancel");
        cancelBtnCommandAction.setMenu(this.getMenu());

        commands.add(Box.createHorizontalStrut(5));
        commands.add(new JButton(cancelBtnCommandAction));
        commands.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
        commands.setBackground(UIManager.getColor("Panel.background"));
        commands.setOpaque(true);
        owner.add(new JScrollPane(filterList.getList()), BorderLayout.CENTER);
        owner.add(commands, BorderLayout.SOUTH);

        return owner;

    }

    public boolean applyColumnFilter() {
        Collection<DistinctColumnItem> checked = filterList.getCheckedItems();
        ICheckListModel<DistinctColumnItem> model = filterList.getModel();
        myTableModelInitial = filter.getTable().getModel();
        model.filter("", CheckListFilterType.CONTAINS); // clear filter to get true results
        filter.apply(mColumnIndex, checked);
        filter.saveFilterCriteria(checked);
        filter.setColumnIndex(mColumnIndex);
        gui.columnFilterStatus(mColumnIndex, filter.getTable());
        return true;
    }

    TableModel getMyTableModelInitial() {
        return myTableModelInitial;
    }

    public JTableFilter getFilter() {
        return filter;
    }

    public JTable getTable() { 
        return filter.getTable();
    }

    public boolean isEnabled() {
        return enabled;
    }

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
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (enabled && e.isPopupTrigger()) {
            showFilterPopup(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (enabled && e.isPopupTrigger()) {
            showFilterPopup(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void showFilterPopup(MouseEvent e) {
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

        Collection<DistinctColumnItem> distinctItems = filter.getDistinctColumnItems(mColumnIndex);

        DefaultCheckListModel<DistinctColumnItem> model = new DefaultCheckListModel<>(distinctItems);
        filterList.setModel(actionsVisible ? new ActionCheckListModel<>(model) : model);
        Collection<DistinctColumnItem> checked = filter.getFilterState(mColumnIndex);

        // replace empty checked items with full selection
        filterList.setCheckedItems(CollectionUtils.isEmpty(checked) ? distinctItems : checked);

        if (useTableRenderers) {
            filterList.getList().setCellRenderer(new TableAwareCheckListRenderer(filter.getTable(), vColumnIndex));
        }

        // show pop-up
        show(header, headerRect.x, header.getHeight());
    }

    private ColumnAttrs getColumnAttrs(int column) {
        ColumnAttrs attrs = colAttrs.get(column);
        if (attrs == null) {
            attrs = new ColumnAttrs();
            colAttrs.put(column, attrs);
        }

        return attrs;
    }

    @Override
    public void beforeHide() {
        // save pop-up's dimensions before pop-up becomes hidden
        getColumnAttrs(mColumnIndex).preferredSize = getPreferredSize();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    static class ColumnAttrs {

        public Dimension preferredSize;
    }

}
