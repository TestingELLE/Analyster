
package com.elle.analyster.presentation.filter;

import com.elle.analyster.Analyster;
import com.elle.analyster.Tab;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author cigreja
 */
public class ColumnPopupMenu extends JPopupMenu{
    
    // attributes
    private CheckBoxList checkBoxList;
    private TableFilter filter;
    private ArrayList<JCheckBox> checkBoxItems; // items in checklist
    private Map<Integer,ArrayList<JCheckBox>> distinctItems; // distinct items for options
    private int columnIndex; // selected colunm
    
    // for updating the records label when a filter is applied
    Analyster analyster;
    Map<String,Tab> tabs;
    
    /**
     * CONSTRUCTOR
     * ColumnPopupMenu
     * creates a ColumnPopupMenu
     */
    public ColumnPopupMenu(TableFilter filter){
        initComponents();
        this.filter = filter;
        
        // initialize distinctItems
        distinctItems = new HashMap<>(); 
        for(int i = 0; i < filter.getTable().getColumnCount(); i++){
            distinctItems.put(i, new ArrayList<>());
        }
        
        // load all distinct items
        loadAllDistinctItems();
        
        // initialize analyster and tabs 
        // for updating the records label when filter is applied
        analyster = Analyster.getInstance();
        tabs = analyster.getTabs();
    }
    
    /**
     * initComponents
     * initialize the components of the ColumnPopupMenu
     */
    private void initComponents() {
        
        // create a new JPanel
        JPanel panel = new JPanel(new BorderLayout(3, 3));
        panel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panel.setPreferredSize(new Dimension(250, 300)); // default popup size
        
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
                  if(checkbox.getText().equals("(All)")){
                      if(checkbox.isSelected()){
                          removeAllChecks(getColumnIndex());
                      }
                      else{
                          checkAll(getColumnIndex());
                      }
                  }
                  else{
                      // toogle the check for the checkbox item
                      checkbox.setSelected(!checkbox.isSelected());
                  }
                  repaint(); // redraw graphics
               }
            }
        });
        
        // add the check box list to the panel
        panel.add(new JScrollPane(checkBoxList), BorderLayout.CENTER); // add list to center

        // create a new Box for the buttons
        Box boxButtons = new Box(BoxLayout.LINE_AXIS);

        // add horizontal glue to the box
        boxButtons.add(Box.createHorizontalGlue());
        
        // create Apply button
        JButton btnApply = new JButton("Apply");
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyColumnFilter(getColumnIndex());
                setVisible(false);
            }
        });
        
        // create Cancel button
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setVisible(false);
            }
        });
        
        // add buttons and look and feel to box of commands
        boxButtons.add(btnApply);
        boxButtons.add(Box.createHorizontalStrut(5));
        boxButtons.add(btnCancel);
        boxButtons.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
        boxButtons.setBackground(UIManager.getColor("Panel.background"));
        boxButtons.setOpaque(true);
        
        // add the box of buttons to the panel
        panel.add(boxButtons, BorderLayout.SOUTH); // add command buttons to south of panel
        
        // add the panel to the ColumnPopupMenu
        add(panel);
    }
    
    /**
     * showPopupMenu
     * This method determines what column was clicked 
     * and displays the popup under it according to the location of the column.
     * @param e 
     */
    public void showPopupMenu(MouseEvent e) {
        
        // Determine the header and column model that was clicked
        JTableHeader header = (JTableHeader) (e.getSource());
        TableColumnModel colModel = header.getTable().getColumnModel();

        // The index of the column whose header was clicked
        int vColumnIndex = colModel.getColumnIndexAtX(e.getX());
        if (vColumnIndex < 0) {
            return;
        }

        // Determine if mouse was clicked between column heads
        Rectangle headerRect = header.getTable().getTableHeader().getHeaderRect(vColumnIndex);
        if (vColumnIndex == 0) {
            headerRect.width -= 2;
        } else {
            headerRect.grow(-2, 0);
        }

        // Mouse was clicked between column heads
        if (!headerRect.contains(e.getX(), e.getY())) {
            return;
        }

        setColumnIndex(vColumnIndex);
        loadList(vColumnIndex);
        
        // show pop-up
        this.show(header, headerRect.x, header.getHeight());
    }
    
    /**
     * loadList
     * @param col
     */
    public void loadList(int col){
        
        // apply checks to filtered items
        applyChecksToFilteredItems(col);
        
        // load JList with checkbox items
        checkBoxList.setListData(distinctItems.get(col).toArray());
    }
    
    /**
     * applyChecksToFilteredItems
     */
    public void applyChecksToFilteredItems(int col){
        
        // get filtered items
        ArrayList<Object> fItems = filter.getFilterItems().get(col);
        ArrayList<JCheckBox> dItems = distinctItems.get(col);
        
        // reset all checks to false
        removeAllChecks(col);
        
        // apply checks to filtered items
        for(JCheckBox dItem: dItems){
            for(Object fItem: fItems){
                if(dItem.getText().equals(fItem.toString())){
                    dItem.setSelected(true);
                }
            }
        }
        
    }
    
    /**
     * loadDistinctItems
     * This is to load all of the distinct options to initialize or when
     * needed to refresh the list. Database change or refresh for example.
     */
    public void loadAllDistinctItems(){
        
        // for every column
        for(int col = 0; col < filter.getTable().getColumnCount(); col++){
            
            ArrayList<Object> temp = filter.getFilterItems().get(col);
            
            // add distinct items
            for(Object item: temp){
                distinctItems.get(col).add(new JCheckBox(item.toString()));
            }
            
        }
    }

    /**
     * getCheckBoxList
     * @return 
     */
    public CheckBoxList getCheckBoxList() {
        return checkBoxList;
    }

    /**
     * setCheckBoxList
     * @param checkBoxList 
     */
    public void setCheckBoxList(CheckBoxList checkBoxList) {
        this.checkBoxList = checkBoxList;
    }

    /**
     * getFilter
     * @return 
     */
    public TableFilter getFilter() {
        return filter;
    }

    /**
     * setFilter
     * @param filter 
     */
    public void setFilter(TableFilter filter) {
        this.filter = filter;
    }

    /**
     * getCheckBoxItems
     * @return 
     */
    public ArrayList<JCheckBox> getCheckBoxItems() {
        return checkBoxItems;
    }

    /**
     * setCheckBoxItems
     * @param checkBoxItems 
     */
    public void setCheckBoxItems(ArrayList<JCheckBox> checkBoxItems) {
        this.checkBoxItems = checkBoxItems;
    }

    /**
     * getDistinctItems
     * @return 
     */
    public Map<Integer, ArrayList<JCheckBox>> getDistinctItems() {
        return distinctItems;
    }

    /**
     * setDistinctItems
     * @param distinctItems 
     */
    public void setDistinctItems(Map<Integer, ArrayList<JCheckBox>> distinctItems) {
        this.distinctItems = distinctItems;
    }

    /**
     * getColumnIndex
     * @return 
     */
    public int getColumnIndex() {
        return columnIndex;
    }

    /**
     * setColumnIndex
     * @param columnIndex 
     */
    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }
    
    /**
     * applyColumnFilter
     * @param columnIndex 
     */
    public void applyColumnFilter(int columnIndex) {
        
        filter.removeFilterItems(columnIndex);
        ArrayList<JCheckBox> dItems = distinctItems.get(columnIndex);
        ArrayList<Object> filterItems = new ArrayList<>();
        for(JCheckBox item: dItems){
            if(item.isSelected()){
                filterItems.add(item.getText());
            }
        }      
        filter.addFilterItems(columnIndex, filterItems);
        filter.applyFilter();
        
        // update record label
        String selectedTab = analyster.getSelectedTab();
        String records = tabs.get(selectedTab).getRecordsLabel();
        analyster.getRecordsLabel().setText(records);
    }  
    
    /**
     * removeAllChecks
     * @param columnIndex 
     */
    public void removeAllChecks(int columnIndex){
        ArrayList<JCheckBox> dItems = distinctItems.get(columnIndex);
        for(JCheckBox item: dItems)
            item.setSelected(false);
    }
    
    /**
     * checkAll
     * @param columnIndex 
     */
    public void checkAll(int columnIndex){
        ArrayList<JCheckBox> dItems = distinctItems.get(columnIndex);
        for(JCheckBox item: dItems)
            item.setSelected(true);
    }
}
