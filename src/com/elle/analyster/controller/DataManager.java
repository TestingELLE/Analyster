/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.controller;

import com.elle.analyster.entities.Assignment;
import com.elle.analyster.entities.AssignmentArchived;
import com.elle.analyster.entities.Report;
import com.elle.analyster.logic.ArchiveConverter;
import com.elle.analyster.logic.AssignmentConverter;
import com.elle.analyster.logic.Converter;
import com.elle.analyster.logic.ReportConverter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Yi
 */
public class DataManager {
    private final String ASSIGNMENT = "assignment";
    private final String REPORT = "report";
    private final String ARCHIVE = "archive";
    private static DataManager instance = null;
    
    private Map<String, DBTableController> controllers;
    private Map<String, Converter> converters;

    public DataManager(){
        //initialize 
        controllers = new HashMap();
        converters = new HashMap();
        
        
        //adding controllers
        controllers.put(ASSIGNMENT, new AssignmentTableController());
        controllers.put(REPORT, new ReportTableController());
        controllers.put(ARCHIVE, new ArchiveTableController());
        
        //adding converters
        converters.put(ASSIGNMENT, new AssignmentConverter());
        converters.put(REPORT, new ReportConverter());
        converters.put(ARCHIVE, new ArchiveConverter());
        
        instance = this;
    }
    
    
    public static DataManager getInstance() {
        return instance;
    }
    
    
    
    
    //assignments related
    public List<Object[]> getAssignments() {
        //get assignments from tablecontroller
        AssignmentTableController controller = (AssignmentTableController) controllers.get(ASSIGNMENT);
        AssignmentConverter converter = (AssignmentConverter) converters.get(ASSIGNMENT);
        
        List<Assignment> assignments = (List<Assignment>) controller.getOnlineItems().values();
        
        ArrayList<Object[]> tableData = new ArrayList();
        assignments.stream().forEach((item) -> {
            tableData.add(converter.convertToRow(item));
        });
        
        return tableData;
    }
    
    public Object[] getSelectedAssignment(int id) {
        //get assignments from tablecontroller
        AssignmentTableController controller = (AssignmentTableController) controllers.get(ASSIGNMENT);
        AssignmentConverter converter = (AssignmentConverter) converters.get(ASSIGNMENT);
        
        Assignment item = controller.get(id);
        return converter.convertToRow(item);
    }
    
    public void deleteAssignments(int[] ids) {
        AssignmentTableController controller = (AssignmentTableController) controllers.get(ASSIGNMENT);
        for(int id: ids)
            controller.delete(id);
    }
    
    
    //reports related
    public List<Object[]> getReports(){
        
        ReportTableController controller = (ReportTableController) controllers.get(REPORT);
        ReportConverter converter = (ReportConverter) converters.get(REPORT);
        
        List<Report> reports = (List<Report>) controller.getOnlineItems().values();
        
        ArrayList<Object[]> tableData = new ArrayList();
        reports.stream().forEach((item) -> {
            tableData.add(converter.convertToRow(item));
        });
        
        return tableData;
    }
    
    
    
    public Object[] getSelectedReport(int id) {
         
        ReportTableController controller = (ReportTableController) controllers.get(REPORT);
        ReportConverter converter = (ReportConverter) converters.get(REPORT);
        
        Report item = controller.get(id);
        return (converter.convertToRow(item));
        
    }
    
    public void deleteReports(int[] ids) {
        ReportTableController controller = (ReportTableController) controllers.get(REPORT);
        for(int id: ids)
            controller.delete(id);
    }
    
    
    //archives related
    public List<Object[]> getArchives(){
        ArchiveTableController controller = (ArchiveTableController) controllers.get(ARCHIVE);
        ArchiveConverter converter = (ArchiveConverter) converters.get(ASSIGNMENT);
        
        List<AssignmentArchived> archives = (List<AssignmentArchived>) controller.getOnlineItems().values();
        
        ArrayList<Object[]> tableData = new ArrayList();
        archives.stream().forEach((item) -> {
            tableData.add(converter.convertToRow(item));
        });
        
        return tableData;
        
    }
    
    public Object[] getSelectedArchive(int id) {
        ArchiveTableController controller = (ArchiveTableController) controllers.get(ARCHIVE);
        ArchiveConverter converter = (ArchiveConverter) converters.get(ASSIGNMENT);
        
        AssignmentArchived item = controller.get(id);
        return converter.convertToRow(item);
    }
    
    public void deleteArchives(int[] ids) {
        ArchiveTableController controller = (ArchiveTableController) controllers.get(ARCHIVE);
        for(int id: ids)
            controller.delete(id);
    }
    
}
