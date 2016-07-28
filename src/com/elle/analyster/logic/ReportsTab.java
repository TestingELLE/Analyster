/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.logic;

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
    protected void uploadData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Object[] getSelectedData(int id) {
        return dataManager.getSelectedReport(id);
    }

    
    
     @Override
    protected void leftCtrlMouseClick() {
        openDocumentTool();
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
