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
public class AssignmentsTab extends BaseTab{
    
    public AssignmentsTab(JTable table) {
        super(table);
    }

    @Override
    protected void initializeTabData() {
        setSearchFields(ASSIGNMENTS_SEARCH_FIELDS);
        setBatchEditFields(ASSIGNMENTS_BATCHEDIT_CB_FIELDS);
        setColWidthPercent(COL_WIDTH_PER_ASSIGNMENTS);
        state = new ButtonsState(true, true, false, false, false, false, true, false, false, false);
    }

    @Override
    protected List<Object[]> getData() {
        return dataManager.getAssignments();
    }

    @Override
    protected void uploadData(List<Object[]> rowsData) {
        dataManager.updateAssignments(rowsData);
    }

    @Override
    protected Object[] getSelectedData(int id) {
        return dataManager.getSelectedAssignment(id);
    }

    //no support 
    @Override
    protected void leftCtrlMouseClick() {
        // no implementation
    }
    
}
