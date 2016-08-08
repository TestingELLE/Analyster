/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.logic;

import com.elle.analyster.entities.Assignment;

/**
 *
 * @author Yi
 */
public class AssignmentConverter implements Converter<Assignment> {
    
    
    @Override
    public Object[] convertToRow(Assignment item) {
        Object[] rowData = new Object[7];
        rowData[0] = item.getId();
        rowData[1] = item.getSymbol();
        rowData[2] = item.getAnalyst();
        rowData[3] = item.getPriority();
        rowData[4] = item.getDateAssigned();
        rowData[5] = item.getDateDone();
        rowData[6] = item.getNotes();
        
        return rowData;
       
    }

    
    @Override
    public Assignment convertFromRow(Object[] rowData) {
        Assignment item = new Assignment();
        item.setId((int) rowData[0]);
        item.setSymbol((String) rowData[1]);
        item.setAnalyst((String) rowData[2]);
        item.setPriority((String) rowData[3]);
        item.setDateAssigned((String) rowData[4]);
        item.setDateDone((String) rowData[5]);
        item.setNotes((String) rowData[6]);   
        return item;
        
        
    }
    
    
}
