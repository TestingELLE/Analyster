/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.logic;

import com.elle.analyster.entities.AssignmentArchived;

/**
 *
 * @author Yi
 */
public class ArchiveConverter implements Converter<AssignmentArchived>{
    
    @Override
    public Object[] convertToRow(AssignmentArchived item) {
        Object[] rowData = new Object[9];
        rowData[0] = item.getId();
        rowData[1] = item.getDateArchived();
        rowData[2] = item.getaId();
        rowData[3] = item.getSymbol();
        rowData[4] = item.getAnalyst();
        rowData[5] = item.getPriority();
        rowData[6] = item.getDateAssigned();
        rowData[7] = item.getDateDone();
        rowData[8] = item.getNotes();
        
        return rowData;
       
    }

    
    @Override
    public AssignmentArchived convertFromRow(Object[] rowData) {
        AssignmentArchived item = new AssignmentArchived();
        item.setId((int) rowData[0]);
        item.setDateArchived((String) rowData[1]);
        item.setaId((int) rowData[2]);
        item.setSymbol((String) rowData[3]);
        item.setAnalyst((String) rowData[4]);
        item.setPriority((String) rowData[5]);
        item.setDateAssigned((String) rowData[6]);
        item.setDateDone((String) rowData[7]);
        item.setNotes((String) rowData[8]);   
        return item;
        
        
    }
 
}
