
package com.elle.analyster.presentation.filter;

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
               int index = checkBoxList.locationToIndex(e.getPoint());

               if (index != -1) {
                  JCheckBox checkbox = (JCheckBox)
                              checkBoxList.getModel().getElementAt(index);
                  checkbox.setSelected(
                                     !checkbox.isSelected());
                  repaint();
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
        
        int cap = 10; // cap the String length of list options
        Object value; // value of the cell
        
        // for every column
        for(int col = 0; col < filter.getTable().getColumnCount(); col++){
            
            // clear the array
            //distinctItems.get(col).clear();
            
            ArrayList<String> temp = new ArrayList<>();
            
            // for every row
            for (int row = 0; row < filter.getTable().getRowCount(); row++){
                
                // get value of cell
                value = filter.getTable().getValueAt(row, col);
                
                // handle null values
                if(value == null)
                    value = "";
                
                // cap the String length of list options
                if(value.toString().length() > cap){
                    value = value.toString().substring(0, cap);
                }
                
                // add the first item to the array for comparison
                if(temp.isEmpty()){
                    temp.add(value.toString());
                }
                else{
                    
                    // compare the values
                    if(!temp.contains(value.toString())){
                        temp.add(value.toString());
                    }
                }
            }
            
            // sort items
            temp.sort(null);
            
            // the first item is (All) for select all and uncheck all
            distinctItems.get(col).add(new JCheckBox("(All)"));
            
            // add distinct items
            for(String item: temp){
                distinctItems.get(col).add(new JCheckBox(item));
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
        for(JCheckBox item: dItems){
            if(item.isSelected()){
                filter.addFilterItem(columnIndex, item.getText());
            }
        }      
        filter.applyFilter();
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
