/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.logic;

import com.elle.analyster.entities.Report;

/**
 *
 * @author Yi
 */
public class ReportConverter implements Converter<Report>{
    
    @Override
    public Object[] convertToRow(Report item) {
        Object[] rowData = new Object[8];
        rowData[0] = item.getId();
        rowData[1] = item.getSymbol();
        rowData[2] = item.getAnalyst();
        rowData[3] = item.getAnalysisDate();
        rowData[4] = item.getPath();
        rowData[5] = item.getDocument();
        rowData[6] = item.getDecision();
        rowData[7] = item.getNotes();
        
        return rowData;
       
    }

    
    @Override
    public Report convertFromRow(Object[] rowData) {
        Report item = new Report();
        item.setId((int) rowData[0]);
        item.setSymbol((String) rowData[1]);
        item.setAnalyst((String) rowData[2]);
        item.setAnalysisDate((String) rowData[3]);
        item.setPath((String) rowData[4]);
        item.setDocument((String) rowData[5]);
        item.setDecision((String) rowData[6]);
        item.setNotes((String) rowData[7]);   
        return item;
        
        
    }

}
