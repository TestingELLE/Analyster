/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.controller;

import com.elle.analyster.dao.AssignmentArchivedDAO;
import com.elle.analyster.entities.AssignmentArchived;

/**
 *
 * @author Yi
 */
public class ArchiveTableController extends DBTableController<AssignmentArchived> {
    public ArchiveTableController(){
        super();
        tableName = ARCHIVE_TABLE_NAME;
        onlineDAO = new AssignmentArchivedDAO();
       
        //load issues from db to map
        getAll();
       
    }
    
}
