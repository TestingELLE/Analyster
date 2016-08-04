/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.logic;

import com.elle.analyster.entities.Assignment;
import com.elle.analyster.entities.AssignmentArchived;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 *
 * @author Yi
 * @since 08/02/2016
 * functions to convert between assignment and archive
 * it is for the main window functions of archiveAssignment and activateArchive
 */
public class AssignmentArchiveConverter {
    
    public static Assignment archiveToAssignment(AssignmentArchived archive) {
        Assignment item = new Assignment();
        item.setId(-1);
        item.setSymbol(archive.getSymbol());
        item.setAnalyst(archive.getAnalyst());
        item.setPriority(archive.getPriority());
        item.setDateAssigned(archive.getDateAssigned());
        item.setDateDone(archive.getDateDone());
        item.setNotes(archive.getNotes());
        return item;
        
    }
    
    
    public static AssignmentArchived assignmentToArchive(Assignment assignment) {
        AssignmentArchived item = new AssignmentArchived();
        
         // initialize the dateArchived with todays date that is used for every inserted record
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date todaysDate = new Date();
        String dateArchived = dateFormat.format(todaysDate); 
        
        //set up item
        item.setId(-1);
        item.setSymbol(assignment.getSymbol());
        item.setAnalyst(assignment.getAnalyst());
        item.setPriority(assignment.getPriority());
        item.setDateAssigned(assignment.getDateAssigned());
        item.setDateDone(assignment.getDateDone());
        item.setNotes(assignment.getNotes());
        item.setaId(assignment.getId());
        item.setDateArchived(dateArchived);
        return item;
        
    }
    
}
