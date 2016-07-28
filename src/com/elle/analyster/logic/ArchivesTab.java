/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.logic;

import java.util.List;
import javax.swing.JTable;

/**
 *
 * @author Yi
 */
public class ArchivesTab extends BaseTab {
    
    public ArchivesTab(JTable table) {
        super(table);
    }

    @Override
    protected void initializeTabData() {
        setSearchFields(ARCHIVE_SEARCH_FIELDS);
        setBatchEditFields(ARCHIVE_BATCHEDIT_CB_FIELDS);
        setColWidthPercent(COL_WIDTH_PER_ARCHIVE);
        state = new ButtonsState(false, false, false, false, false, true, false, false, false, false);
        
    }

    @Override
    protected List<Object[]> getData() {
        return dataManager.getArchives();
    }

    @Override
    protected void uploadData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Object[] getSelectedData(int id) {
        return dataManager.getSelectedArchive(id);
    }

    //no support
   @Override
    protected void leftCtrlMouseClick() {
        
    }

    
}
