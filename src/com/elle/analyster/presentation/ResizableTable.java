package com.elle.ProjectManager.presentation;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.Container;
import static javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS;
/**
 *
 * @author corinne
 */
public class ResizableTable extends JTable{
    public ResizableTable(){
        
    }
    

    @Override
    public void doLayout(){
        // Viewport size changed. Change title and description column widths
        if (tableHeader != null
        &&  tableHeader.getResizingColumn() == null) {
            TableColumn resizingColumn = new TableColumn();
           
            TableColumnModel tcm = getColumnModel();

           // if(this.getColumnName(0) != null){
                for (int column = 0; column < tcm.getColumnCount(); column++){
                    if(this.getColumnName(column).equalsIgnoreCase("description")){
                        resizingColumn = tcm.getColumn(column);
                    }
                 }
                
         /*   }else{  
                resizingColumn = tcm.getColumn(3);
                } */    

            int delta = getParent().getWidth() - tcm.getTotalColumnWidth();   
            resizingColumn.setPreferredWidth(resizingColumn.getPreferredWidth() + delta);
            resizingColumn.setWidth(resizingColumn.getPreferredWidth());
            }
            else{
            super.doLayout();
        }
        
    }
}
