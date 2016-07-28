
package com.elle.analyster.dao;

import com.elle.analyster.database.DBConnection;
import com.elle.analyster.entities.Report;
import com.elle.analyster.logic.LoggingAspect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ReportDAO
 * @author Carlos Igreja
 * @since  May 7, 2016
 */
public class ReportDAO extends BaseDAO implements AbstractDAO<Report> {

    // database table information
    
    private final String COL_SYMBOL = "symbol";
    private final String COL_ANALYST = "analyst";
    private final String COL_ANALYSIS_DATE = "analysisDate";
    private final String COL_PATH = "path";
    private final String COL_DOCUMENT = "document";
    private final String COL_DECISION = "decision";
    private final String COL_NOTES = "notes";
    
    
    public ReportDAO(){
        DB_TABLE_NAME = "Reports";
    }
    public Report get(int id) {
        ResultSet rs = null;
        String sql = "";
        
        
            sql = "SELECT * FROM " + DB_TABLE_NAME + " WHERE ID = " + "'" 
            + id + "'";
        Report report = new Report();
        
        try {

            DBConnection.close();
            DBConnection.open();
            rs = DBConnection.getStatement().executeQuery(sql);
            while(rs.next()){
                
                report.setId(rs.getInt(COL_PK_ID));
                report.setSymbol(rs.getString(COL_SYMBOL));
                report.setAnalyst(rs.getString(COL_ANALYST));
                report.setAnalysisDate(rs.getString(COL_ANALYSIS_DATE));
                report.setPath(rs.getString(COL_PATH));
                report.setDocument(rs.getString(COL_DOCUMENT));
                report.setDecision(rs.getString(COL_DECISION));
                report.setNotes(rs.getString(COL_NOTES));
          
            }
     
        } 
        catch (SQLException e) {
            LoggingAspect.afterThrown(e);
        }
        
        return report;
        
    }
    
    public boolean insert(Report report) {
        
        boolean successful = false;
        DBConnection.close();
        if(DBConnection.open()){
            
            // set issue id
            
            int id = getMaxId() + 1;
            report.setId(id);
            String symbol = report.getSymbol();
            String analyst = report.getAnalyst();
            String analysisDate = report.getAnalysisDate();
            String path = report.getPath();
            String document = report.getDocument();
            String decision = report.getDecision();
            String notes = report.getNotes();
            
           
            try {
                
                
            String sql = "INSERT INTO " + DB_TABLE_NAME + " (" + COL_PK_ID + ", " 
                    + COL_SYMBOL + ", " +  COL_ANALYST + ", " +  COL_ANALYSIS_DATE + ", " 
                    +  COL_PATH + ", " + COL_DOCUMENT + ", " + COL_DECISION + ", " +  COL_NOTES 
                    +   ") " 
                    + "VALUES (" + id + ", " + symbol + ", " +  analyst + ", " 
                    +   analysisDate + ", " +  path + ", " +  document + ", " 
                    +   decision + ", " +  notes  +  ") ";
            
                Connection con = DBConnection.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.execute();
                LoggingAspect.afterReturn("Upload Successful!");
                successful = true;
                
                     
            }
            catch (SQLException ex) {
                LoggingAspect.afterThrown(ex);
                successful = false;
            }
        }
        DBConnection.close();
        
        return successful;
    }
    
    

    /**
     * update
     * @param issue 
     */
    public boolean update(Report report) {
        
        boolean successful = false;
        DBConnection.close();
        if(DBConnection.open()){
            
            // set issue values
            int id = report.getId();
            String symbol = format(report.getSymbol());
            String analyst = format(report.getAnalyst());
            String analysisDate = format(report.getAnalysisDate());
            String path = format(report.getPath());
            String document = format(report.getDocument());
            String decision = format(report.getDecision());
            String notes = format(report.getNotes());
            

            try {
                String sql = "UPDATE " + DB_TABLE_NAME + " SET " 
                    + COL_SYMBOL + " = " + symbol + ", "
                    + COL_ANALYST + " = " + analyst + ", "
                    + COL_ANALYSIS_DATE + " = " + analysisDate + ", "
                    + COL_PATH + " = " + path + ", "
                    + COL_DOCUMENT + " = " + document + ", "
                    + COL_DECISION + " = " + decision + ", "
                    + COL_NOTES + " = " + notes + " "
                    + "WHERE " + COL_PK_ID + " = " + id + ";";
                
                System.out.println("update : " + sql );
                Connection con = DBConnection.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);
                
                pstmt.execute();
                
                LoggingAspect.afterReturn("Upload Successful!");
                
                
                successful = true;
            }
            catch (SQLException ex) {
                LoggingAspect.afterThrown(ex);
                successful = false;
            }
        }
        DBConnection.close();
        
        return successful;
    }
    
    
    
    public List<Report> getAll() {
        
        ArrayList<Report> reports = new ArrayList<>();
        ResultSet rs = null;
        String sql = " SELECT * FROM " + DB_TABLE_NAME ;
        
        
        try {
            
            
            DBConnection.close();
            DBConnection.open();
            rs = DBConnection.getStatement().executeQuery(sql);
            while(rs.next()){
                Report report = new Report();
                report.setId(rs.getInt(COL_PK_ID));
                report.setSymbol(rs.getString(COL_SYMBOL));
                report.setAnalyst(rs.getString(COL_ANALYST));
                report.setAnalysisDate(rs.getString(COL_ANALYSIS_DATE));
                report.setPath(rs.getString(COL_PATH));
                report.setDocument(rs.getString(COL_DOCUMENT));
                report.setDecision(rs.getString(COL_DECISION));
                report.setNotes(rs.getString(COL_NOTES));
                reports.add(report);
            }
            
            LoggingAspect.afterReturn("Loaded table " + DB_TABLE_NAME);

        } 
        catch (SQLException e) {
            LoggingAspect.afterThrown(e);
        }
        
        return reports;
    }
    
    
}
