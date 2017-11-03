
package com.elle.analyster.dao;

import com.elle.analyster.database.DBConnection;
import com.elle.analyster.entities.Assignment;
import com.elle.analyster.logic.LoggingAspect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.text.DateFormatter;

/**
 * AssignmentDAO
 * @author Carlos Igreja
 * @since  May 7, 2016
 * @author Yi
 * @07/25/2016
 * @added crud functions
 */
public class AssignmentDAO extends BaseDAO implements AbstractDAO<Assignment> {

    // database table information
    
    private final String COL_SYMBOL = "symbol";
    private final String COL_ANALYST = "analyst";
    private final String COL_PRIORITY = "priority";
    private final String COL_DATE_ASSIGNED = "dateAssigned";
    private final String COL_DATE_DONE = "dateDone";
    private final String COL_NOTES = "notes";
    
    public AssignmentDAO() {
        DB_TABLE_NAME = "Assignments";
        
    }
    
   
    
    public Assignment get(int id) {
        ResultSet rs = null;
        String sql = "";
        
        
            sql = "SELECT * FROM " + DB_TABLE_NAME + " WHERE ID = " + "'" 
            + id + "'";
        Assignment  assignment = new Assignment();
        
        try {

            DBConnection.close();
            DBConnection.open();
            rs = DBConnection.getStatement().executeQuery(sql);
            while(rs.next()){
                
                assignment.setId(rs.getInt(COL_PK_ID));
                assignment.setSymbol(rs.getString(COL_SYMBOL));
                assignment.setAnalyst(rs.getString(COL_ANALYST));
                assignment.setPriority(rs.getString(COL_PRIORITY));
                assignment.setDateAssigned(rs.getString(COL_DATE_ASSIGNED));
                assignment.setDateDone(rs.getString(COL_DATE_DONE));
                assignment.setNotes(rs.getString(COL_NOTES));
          
            }
     
        } 
        catch (SQLException e) {
            LoggingAspect.afterThrown(e);
        }
        
        return assignment;
        
    }
    
    public boolean insert(Assignment assignment) {
        
        boolean successful = false;
        int id = getMaxId() + 1;
        
        DBConnection.close();
        if(DBConnection.open()){
            
            // set issue id
            
            
            assignment.setId(id);
            String symbol = format(assignment.getSymbol());
            String analyst = format(assignment.getAnalyst());
            String priority = format(assignment.getPriority());
            String dateAssigned = format(assignment.getDateAssigned());
            String dateDone = format(assignment.getDateDone());
            String notes = format(assignment.getNotes());
            
           
            try {
                
                
            String sql = "INSERT INTO " + DB_TABLE_NAME + " (" + COL_PK_ID + ", " 
                    + COL_SYMBOL + ", " +  COL_ANALYST + ", " +  COL_PRIORITY + ", " 
                    +  COL_DATE_ASSIGNED + ", " +  COL_DATE_DONE + ", " +  COL_NOTES 
                    +   ") " 
                    + "VALUES (" + id + ", " + symbol + ", " +  analyst + ", " 
                    +   priority + ", " +  dateAssigned + ", " +  dateDone + ", " 
                    +  notes  +  ") ";
            
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
    public boolean update(Assignment assignment) {
        
        boolean successful = false;
        DBConnection.close();
        if(DBConnection.open()){
            
            // set issue values
            int id = assignment.getId();
            String symbol = format(assignment.getSymbol());
            String analyst = format(assignment.getAnalyst());
            String priority = format(assignment.getPriority());
            String dateAssigned = assignment.getDateAssigned();
            String dateDone = assignment.getDateDone();
            String notes = format(assignment.getNotes());
            

            try {
                String sql = "UPDATE " + DB_TABLE_NAME + " SET " 
                    + COL_SYMBOL + " = " + symbol + ", "
                    + COL_ANALYST + " = " + analyst + ", "
                    + COL_PRIORITY + " = " + priority + ", "
                    + COL_DATE_ASSIGNED + " = ? ,"
                    + COL_DATE_DONE + " = ? , "
                    + COL_NOTES + " = " + notes + " "
                    + "WHERE " + COL_PK_ID + " = " + id + ";";
                
                System.out.println("update : " + sql );
                Connection con = DBConnection.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);
                if((dateAssigned == null)||(dateAssigned.equals("")))
                {
                    pstmt.setDate(1, null);
                }
                else
                {
                    pstmt.setString(1, dateAssigned);
                }
                if((dateDone == null)||(dateDone.equals("")))
                {
                    pstmt.setDate(2, null);
                }
                else
                {
                    pstmt.setString(2, dateDone);
                }
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
   
    
    public List<Assignment> getAll() {
        
        ArrayList<Assignment> assignments = new ArrayList<>();
        ResultSet rs = null;
        String sql = " SELECT * FROM " + DB_TABLE_NAME ;
        
        
        try {
            
            
            DBConnection.close();
            DBConnection.open();
            rs = DBConnection.getStatement().executeQuery(sql);
            while(rs.next()){
                Assignment assignment = new Assignment();
                assignment.setId(rs.getInt(COL_PK_ID));
                assignment.setSymbol(rs.getString(COL_SYMBOL));
                assignment.setAnalyst(rs.getString(COL_ANALYST));
                assignment.setPriority(rs.getString(COL_PRIORITY));
                assignment.setDateAssigned(rs.getString(COL_DATE_ASSIGNED));
                assignment.setDateDone(rs.getString(COL_DATE_DONE));
                assignment.setNotes(rs.getString(COL_NOTES));
                assignments.add(assignment);
            }
            
            LoggingAspect.afterReturn("Loaded table " + DB_TABLE_NAME);

        } 
        catch (SQLException e) {
            LoggingAspect.afterThrown(e);
        }
        
        return assignments;
    }

    
}
