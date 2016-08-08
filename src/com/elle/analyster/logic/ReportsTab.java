/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.logic;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author Yi
 */
public class ReportsTab extends BaseTab {
    public ReportsTab(JTable table) {
        super(table);
        
    }

    @Override
    protected void initializeTabData() {
        setSearchFields(REPORTS_SEARCH_FIELDS);
        setBatchEditFields(REPORTS_BATCHEDIT_CB_FIELDS);
        setColWidthPercent(COL_WIDTH_PER_REPORTS);
        state = new ButtonsState(true, true, false, false, false, false, false, true, true, true);
    }

    @Override
    protected List<Object[]> getData() {
        return dataManager.getReports();
    }

    @Override
    protected void uploadData(List<Object[]> rowsData) {
        dataManager.updateReports(rowsData);
    }

    @Override
    protected Object[] getSelectedData(int id) {
        return dataManager.getSelectedReport(id);
    }

    
    
     @Override
    protected void leftCtrlMouseClick() {
        openDocumentTool();
    }
    
    @Override
    protected void setTableBodyListeners(){
        super.setTableBodyListeners();
        
        //below is for hand cursor , only for reports tab
        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();

                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (table.getLocation().y < 1 && table.getLocation().y > -8155) {
                    if (col > 3 && col < 6) {
                        table.clearSelection();
                        table.setRowSelectionInterval(row, row);
                        Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
                        table.setCursor(cursor);

                    } else {
                        Cursor cursor = Cursor.getDefaultCursor();
                        table.setCursor(cursor);

                    }
                } else {
                    Cursor cursor = Cursor.getDefaultCursor();
                    table.setCursor(cursor);
                }
            }

        });

        
    }
    
    public void openDocumentTool() {
        // must be on reports tab
        
            
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

                if (elleFolderFound == false) {
                    JOptionPane.showMessageDialog(analysterWindow, "ELLE ANALYSES folder not found.");
                }
                if (elleFolderFound) {
                    Object pathToDoc = table.getValueAt(row, 4); // path column
                    Object document = table.getValueAt(row, 5); // document column
                    if (document == null) {
                        JOptionPane.showMessageDialog(analysterWindow, "No document in selected row");
                    } else {
                        OpenDocumentTool docTool = new OpenDocumentTool(elle_folder, pathToDoc.toString(), document.toString());
                        docTool.setParent(analysterWindow);
                        if (!docTool.open()) {
                            JOptionPane.showMessageDialog(analysterWindow, "Could not open file!");
                        }
                    }
                    String text = "Opening " + document.toString() + " from path: " + pathToDoc.toString();
                    LoggingAspect.afterReturn(text);
                }
            } else {
                JOptionPane.showMessageDialog(analysterWindow, "No row was selected.");
            }
      
    }
    
}
