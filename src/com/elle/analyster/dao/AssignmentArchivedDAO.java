
package com.elle.analyster.dao;

import com.elle.analyster.database.DBConnection;
import com.elle.analyster.database.ModifiedData;
import com.elle.analyster.entities.Assignment;
import com.elle.analyster.entities.AssignmentArchived;
import com.elle.analyster.logic.LoggingAspect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * AssignmentArchivedDAO
 * @author Carlos Igreja
 * @since  May 7, 2016
 */
public class AssignmentArchivedDAO extends BaseDAO implements AbstractDAO<AssignmentArchived> {

    // database table information
    
    private final String COL_DATE_ARCHIVED = "dateArchived";
    private final String COL_FK_A_ID = "aID";
    private final String COL_SYMBOL = "symbol";
    private final String COL_ANALYST = "analyst";
    private final String COL_PRIORITY = "priority";
    private final String COL_DATE_ASSIGNED = "dateAssigned";
    private final String COL_DATE_DONE = "dateDone";
    private final String COL_NOTES = "notes";
    
    public AssignmentArchivedDAO(){
        DB_TABLE_NAME = "Assignments_Archived";
    }
    
    public AssignmentArchived get(int id) {
        ResultSet rs = null;
        String sql = "";
        
        
            sql = "SELECT * FROM " + DB_TABLE_NAME + " WHERE ID = " + "'" 
            + id + "'";
        AssignmentArchived  assignmentArchived = new AssignmentArchived();
        
        try {

            DBConnection.close();
            DBConnection.open();
            rs = DBConnection.getStatement().executeQuery(sql);
            while(rs.next()){
                
                assignmentArchived.setId(rs.getInt(COL_PK_ID));
                assignmentArchived.setDateArchived(COL_DATE_ARCHIVED);
                assignmentArchived.setaId(rs.getInt(COL_FK_A_ID));
                assignmentArchived.setSymbol(rs.getString(COL_SYMBOL));
                assignmentArchived.setAnalyst(rs.getString(COL_ANALYST));
                assignmentArchived.setPriority(rs.getString(COL_PRIORITY));
                assignmentArchived.setDateAssigned(rs.getString(COL_DATE_ASSIGNED));
                assignmentArchived.setDateDone(rs.getString(COL_DATE_DONE));
                assignmentArchived.setNotes(rs.getString(COL_NOTES));
          
            }
     
        } 
        catch (SQLException e) {
            LoggingAspect.afterThrown(e);
        }
        
        return assignmentArchived;
        
    }
    
    public boolean insert(AssignmentArchived assignmentArchived) {
        
        boolean successful = false;
        DBConnection.close();
        if(DBConnection.open()){
            
            // set issue id
            
            int id = getMaxId() + 1;
            assignmentArchived.setId(id);
            String dateArchived = assignmentArchived.getDateArchived();
            int aId = assignmentArchived.getaId();
            String symbol = assignmentArchived.getSymbol();
            String analyst = assignmentArchived.getAnalyst();
            String priority = assignmentArchived.getPriority();
            String dateAssigned = assignmentArchived.getDateAssigned();
            String dateDone = assignmentArchived.getDateDone();
            String notes = assignmentArchived.getNotes();
            
           
            try {
                
                
            String sql = "INSERT INTO " + DB_TABLE_NAME + " (" + COL_PK_ID + ", " 
                    + COL_FK_A_ID + ", " + COL_DATE_ARCHIVED + ", "
                    + COL_SYMBOL + ", " +  COL_ANALYST + ", " +  COL_PRIORITY + ", " 
                    +  COL_DATE_ASSIGNED + ", " +  COL_DATE_DONE + ", " +  COL_NOTES 
                    +   ") " 
                    + "VALUES (" + id + ", " + aId + ", " +  dateArchived + ", " 
                    +   symbol + ", " +  analyst + ", " 
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
    public boolean update(AssignmentArchived assignmentArchived) {
        
        boolean successful = false;
        DBConnection.close();
        if(DBConnection.open()){
            
            // set issue values
            int id = assignmentArchived.getId();
            int aId = assignmentArchived.getaId();
            String dateArchived = assignmentArchived.getDateArchived();
            String symbol = format(assignmentArchived.getSymbol());
            String analyst = format(assignmentArchived.getAnalyst());
            String priority = format(assignmentArchived.getPriority());
            String dateAssigned = format(assignmentArchived.getDateAssigned());
            String dateDone = format(assignmentArchived.getDateDone());
            String notes = format(assignmentArchived.getNotes());
            

            try {
                String sql = "UPDATE " + DB_TABLE_NAME + " SET " 
                    + COL_FK_A_ID + " = " + aId + ", "
                    + COL_DATE_ARCHIVED + " = " + dateArchived + ", "
                    + COL_SYMBOL + " = " + symbol + ", "
                    + COL_ANALYST + " = " + analyst + ", "
                    + COL_PRIORITY + " = " + priority + ", "
                    + COL_DATE_ASSIGNED + " = " + dateAssigned + ", "
                    + COL_DATE_DONE + " = " + dateDone + ", "
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
    
   
    
    public List<AssignmentArchived> getAll() {
        
        ArrayList<AssignmentArchived> archivedAssignments = new ArrayList<>();
        ResultSet rs = null;
        String sql = " SELECT * FROM " + DB_TABLE_NAME ;
        
        
        try {
            
            
            DBConnection.close();
            DBConnection.open();
            rs = DBConnection.getStatement().executeQuery(sql);
            while(rs.next()){
                AssignmentArchived assignmentArchived = new AssignmentArchived();
                assignmentArchived.setId(rs.getInt(COL_PK_ID));
                assignmentArchived.setaId(rs.getInt(COL_FK_A_ID));
                assignmentArchived.setDateArchived(rs.getString(COL_DATE_ARCHIVED));
                assignmentArchived.setSymbol(rs.getString(COL_SYMBOL));
                assignmentArchived.setAnalyst(rs.getString(COL_ANALYST));
                assignmentArchived.setPriority(rs.getString(COL_PRIORITY));
                assignmentArchived.setDateAssigned(rs.getString(COL_DATE_ASSIGNED));
                assignmentArchived.setDateDone(rs.getString(COL_DATE_DONE));
                assignmentArchived.setNotes(rs.getString(COL_NOTES));
                archivedAssignments.add(assignmentArchived);
            }
            
            LoggingAspect.afterReturn("Loaded table " + DB_TABLE_NAME);

        } 
        catch (SQLException e) {
            LoggingAspect.afterThrown(e);
        }
        
        return archivedAssignments;
    }
    
    
}
