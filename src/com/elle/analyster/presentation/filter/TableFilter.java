
package com.elle.analyster.presentation.filter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * TableFilter
 * This class takes a JTable and filters the view for searching data.
 * It also applies colors to the column headers when filtering.
 * @author cigreja
 */
public class TableFilter extends RowFilter<TableModel, Integer> {
    
    // attributes
    private JTable table;                                // table to be filtered 
    private TableRowSorter<TableModel> sorter;           // the table sorter
    private Map<Integer,ArrayList<Object>> filterItems;  // distinct items to filter
    private Color color;                                 // color to paint header
    private boolean isFiltering;                         // is filtering items
    

    /**
     * CONSTRUCTOR
     * TableFilter
     * @param table  // the table to apply the filter
     */
    public TableFilter(JTable table){
        
        this.table = table;
        
        // set table sorter
        sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);
        
        // initialize filterItems
        filterItems = new HashMap<>(); 
        for(int i = 0; i < table.getColumnCount(); i++){
            filterItems.put(i, new ArrayList<>());
        }
        
        // initialize the color for the table header when it is filtering
        color = Color.GREEN; // default color is green
        
        // initialize the tables
        loadAllRows(); // load all rows 
        applyFilter(); // apply filter
        
        isFiltering = false;
    }
    
    /**
     * addDistinctItem
     * @param col
     * @param selectedField 
     */
    public void addFilterItem(int col, Object selectedField){
        
        // if not filtering then all filters are cleared (full table)
        // this is to not clear other filtered columns
        if(isFiltering == false){
            removeAllFilterItems();                // this empties all column filters
            isFiltering = true;
        }
        else{
            filterItems.get(col).clear();              // remove all items from this column
        }
        
        if(selectedField == null)                  // check for null just in case
            selectedField = "";                    // no reason not to
        
        filterItems.get(col).add(selectedField);   // add passed item

        addColorHeader(col);                       // highlight header
    }
    
    /**
     * addDistinctItems
     * @param col
     * @param items 
     */
    public void addFilterItems(int col, ArrayList<Object> items){
        
        if(!items.isEmpty()){
            // if not filtering then all filters are cleared (full table)
            // this is to not clear other filtered columns
            if(isFiltering == false){
                removeAllFilterItems();                // this empties all column filters
                isFiltering = true;
            }
            else{
                filterItems.get(col).clear();              // remove all items from this column
            }

            for(Object item: items){                  

                if(item == null)                      // check for null just in case
                    item = "";                        // no reason not to

                filterItems.get(col).add(item);       // add item to list
            }

            addColorHeader(col);                      // highlight header
        }
    }
    
    /**
     * removeDistinctItem
     * @param col
     * @param selectedField 
     */
    public void removeFilterItem(int col, Object selectedField){
        
        if(selectedField == null) 
            selectedField = "";
        
        filterItems.get(col).remove(selectedField);
    }
    
    /**
     * removeDistinctItems
     * @param col
     * @param items 
     */
    public void removeFilterItems(int col, ArrayList<Object> items){
        
        for(Object item: items){
            
            if(item == null)
                item = "";
            
            filterItems.get(col).remove(item);
        }
        
        removeColorHeader(col);
    }
    
    /**
     * removeDistinctItems
     * @param col
     */
    public void removeFilterItems(int col){

        filterItems.get(col).clear();
        removeColorHeader(col);
    }
    
    /**
     * removeAllDistinctItems
     * @param col
     * @param items 
     */
    public void removeAllFilterItems(){
        
        for(int i = 0; i < filterItems.size(); i++)
            filterItems.get(i).clear();
    }
    
    /**
     * applyFilter
     */
    public void applyFilter(){
        
        sorter.setRowFilter(this);
    }
    
    /**
     * addColorHeader
     * @param columnIndex
     * @param color 
     */
    public void addColorHeader(int columnIndex){
        
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setBackground(color);
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(columnIndex)
                .setHeaderRenderer(cellRenderer);
        table.getTableHeader().repaint();
    }
    
    /**
     * applyColorHeaders
     * @param color 
     */
    public void applyColorHeaders(){
        
        for(int i = 0; i < filterItems.size(); i++){
            if(filterItems.get(i).isEmpty()){
                removeColorHeader(i);
            }
            else{
                addColorHeader(i);
            }
        }
        table.getTableHeader().repaint();
    }
    
    /**
     * removeColorHeader
     * @param columnIndex
     * @param color 
     */
    public void removeColorHeader(int columnIndex){
        
        table.getColumnModel().getColumn(columnIndex)
                .setHeaderRenderer(table.getTableHeader().getDefaultRenderer());
        table.getTableHeader().repaint();
    }
    
    /**
     * removeAllColorHeaders
     * @param color 
     */
    public void removeAllColorHeaders(){
        
        for(int i = 0; i < filterItems.size(); i++){
            removeColorHeader(i);
        }
        table.getTableHeader().repaint();
    }

    /**
     * getColor
     * @return 
     */
    public Color getColor() {
        return color;
    }

    /**
     * setColor
     * @param color 
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * getTable
     * @return 
     */
    public JTable getTable() {
        return table;
    }

    /**
     * setTable
     * @param table 
     */
    public void setTable(JTable table) {
        this.table = table;
    }

    /**
     * getSorter
     * @return 
     */
    public TableRowSorter<TableModel> getSorter() {
        return sorter;
    }

    /**
     * setSorter
     * @param sorter 
     */
    public void setSorter(TableRowSorter<TableModel> sorter) {
        this.sorter = sorter;
    }

    /**
     * getDistinctColumnItems
     * @return 
     */
    public Map<Integer, ArrayList<Object>> getFilterItems() {
        return filterItems;
    }

    /**
     * setDistinctColumnItems
     * @param distinctColumnItems 
     */
    public void setFilterItems(Map<Integer, ArrayList<Object>> distinctColumnItems) {
        this.filterItems = distinctColumnItems;
    }
    
    /**
     * loadAllRows
     * gets every row and adds it to the filterItems for the specified column
     * @param columnIndex 
     */
    public void loadAllRows(int columnIndex){
        
        Object value; // value of the cell
            
        // clear the array
        filterItems.get(columnIndex).clear();

        ArrayList<String> temp = new ArrayList<>();

        // for every row
        for (int row = 0; row < table.getModel().getRowCount(); row++){

            // get value of cell
            value = table.getModel().getValueAt(row, columnIndex);

            // handle null values
            if(value == null)
                value = "";

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
        filterItems.get(columnIndex).add("(All)");

        // add distinct items
        for(String item: temp){
            filterItems.get(columnIndex).add(item);
        }
        
    }
    
    /**
     * loadAllRows
     * gets every row and adds it to the filterItems for every column
     */
    public void loadAllRows(){
        
        // for every column
        for(int col = 0; col < table.getColumnCount(); col++){
            loadAllRows(col);
        }
    }
    
    /**
     * clearAllFilters
     * clears filters for the specified column
     * @param columnIndex
     * @return 
     */
    public void clearAllFilters(int columnIndex){
        loadAllRows(columnIndex);         // load all rows
        removeColorHeader(columnIndex);   // remove all header highlighted Colors
        isFiltering = false;              // no filters are applied 
    }
    
    /**
     * clearAllFilters
     * Loads all rows, applys the filter, and removes the color highlights
     */
    public void clearAllFilters(){
        
        loadAllRows();               // load all rows
        removeAllColorHeaders();     // remove all header highlighted Colors
        isFiltering = false;         // no filters are applied 
    }

    /**
     * include
     * @param entry
     * @return 
     */
    @Override
    public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
        
        TableModel model = entry.getModel();
        int row = entry.getIdentifier();
        int numColsfiltered = filterItems.size();     // number of cols filtered
        int itemsFound = 0;                           // items found must match the total columns filtered
        
        // check every column
        for( int col = 0; col < model.getColumnCount(); col++ ) {

            if ( filterItems.get(col).isEmpty() ){
                numColsfiltered--;
                continue;                          // the column is empty
            }
                // get filter values
                ArrayList<Object> distinctItems = filterItems.get(col);

                // get value
                Object cellValue = model.getValueAt(row, col);

                // handle null exception
                if(cellValue == null) 
                    cellValue = "";

                // if contains any of the filter items then include
                if(distinctItems.contains(cellValue.toString())){
                    itemsFound++;
                }
                else{
                    // search for a match and ignore case
                    for(Object distinctItem : distinctItems){
                        if(cellValue.toString().equalsIgnoreCase(distinctItem.toString())){
                            itemsFound++;
                        }
                        // notes only shows the first 20 char so if startsWith is also checked
                        else if(cellValue.toString().startsWith(distinctItem.toString())){
                            itemsFound++;
                        }
                    }
                }
        }
        
        if(itemsFound == numColsfiltered){
            return true;
        }
        else{
            return false; 
        }
    }
}
