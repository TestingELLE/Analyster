/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.controller;

import com.elle.analyster.dao.ReportDAO;
import com.elle.analyster.entities.Report;

/**
 *
 * @author Yi
 */
public class ReportTableController extends DBTableController<Report> {
    
    public ReportTableController(){
        super();
        tableName = REPORTS_TABLE_NAME;
        onlineDAO = new ReportDAO();
       
        //load issues from db to map
        getAll();
    }
    
}
