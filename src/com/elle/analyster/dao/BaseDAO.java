/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.dao;

import com.elle.analyster.database.DBConnection;
import com.elle.analyster.database.ModifiedData;
import com.elle.analyster.logic.LoggingAspect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Yi
 */
public class BaseDAO {
    protected String DB_TABLE_NAME;
    protected final String COL_PK_ID = "ID";
    
    //common functions for all database dao
    
    
    /**
     * get max id from issues table
     * @return max id from issues table
     */
    public int getMaxId() {
        
        int id = -1;
        DBConnection.close();
        if(DBConnection.open()){

            String sql = "SELECT MAX(" + COL_PK_ID + ") "
                       + "FROM " + DB_TABLE_NAME + ";";

            ResultSet result = null;

            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement statement = con.prepareStatement(sql);
                result = statement.executeQuery();
                if(result.next()){
                    id = result.getInt(1);
                }
            }
            catch (SQLException ex) {
                LoggingAspect.afterThrown(ex);
            }
        }
        DBConnection.close();
        return id;
    }
    
    
    
    /**
     * delete
     * @param ids 
     */
    public boolean delete(int[] ids){

        String sqlDelete = ""; // String for the SQL Statement

        if (ids.length != -1) {
            for (int i = 0; i < ids.length; i++) {
                if (i == 0) // this is the first rowIndex
                {
                    sqlDelete += "DELETE FROM " + DB_TABLE_NAME
                            + " WHERE " + COL_PK_ID + " IN (" + ids[i]; 
                } else // this adds the rest of the rows
                {
                    sqlDelete += ", " + ids[i];
                }
            }
            sqlDelete += ");";

            try {

                // delete records from database
                DBConnection.close();
                DBConnection.open();
                DBConnection.getStatement().executeUpdate(sqlDelete);
                LoggingAspect.afterReturn(ids.length + " Record(s) Deleted");
                String levelMessage = "3:" + sqlDelete;
                LoggingAspect.addLogMsgWthDate(levelMessage);
                return true;

            } catch (SQLException e) {
                LoggingAspect.afterThrown(e);
                return false;
            }
        }
        else{
            // ids were passed in empty
            return false;
        }
    }
    
    public boolean delete(int id) {
        String sqlDelete = "DELETE FROM " + DB_TABLE_NAME
                + " WHERE " + COL_PK_ID + " =" + id + ";";

        try {

            // delete records from database
            DBConnection.close();
            DBConnection.open();
            DBConnection.getStatement().executeUpdate(sqlDelete);
            LoggingAspect.afterReturn("Record #" + id + " is Deleted");
            return true;

        } catch (SQLException e) {
            LoggingAspect.afterThrown(e);
            return false;
        }
    }
    
    
    
    /**
     * update
     * @param tableName
     * @param modifiedData
     * @return 
     */
    public boolean update(String tableName,ModifiedData modifiedData) {
        
        boolean updateSuccessful = true;
        String sqlChange = null;

        DBConnection.close();
        if (DBConnection.open()) {

            String columnName = modifiedData.getColumnName();
            Object value = modifiedData.getValue();
            value = processCellValue(value);
            int id = modifiedData.getId();

            try {

                if (value.equals("")) {
                    value = null;
                    sqlChange = "UPDATE " + tableName + " SET " + columnName
                            + " = " + value + " WHERE ID = " + id + ";";
                } else {
                    sqlChange = "UPDATE " + tableName + " SET " + columnName
                            + " = '" + value + "' WHERE ID = " + id + ";";
                }

                DBConnection.getStatement().executeUpdate(sqlChange);
                LoggingAspect.afterReturn(sqlChange);

            } catch (SQLException e) {
                LoggingAspect.addLogMsgWthDate("3:" + e.getMessage());
                LoggingAspect.addLogMsgWthDate("3:" + e.getSQLState() + "\n");
                LoggingAspect.addLogMsgWthDate(("Upload failed! " + e.getMessage()));
                LoggingAspect.afterThrown(e);
                updateSuccessful = false;
            }
            if (updateSuccessful) {
                LoggingAspect.afterReturn(("Edits uploaded successfully!"));
            }
        } else {
            // connection failed
            LoggingAspect.afterReturn("Failed to connect");
        }
        // finally close connection
        DBConnection.close();
        return updateSuccessful;
    }

    
    
    
     /**
     * Formats string to return null or single quotes.
     * This will work for now as all the defaults for
     * the issues table is null. However his could change.
     * This was a last minute fix to get the factoring out.
     * @param s
     * @return 
     */
    protected String format(String s){
        s=processCellValue(s);
        return (s.equals(""))?null:"'"+s+"'";
    }

    protected Object processCellValue(Object cellValue) {
        return cellValue.toString().replaceAll("'", "''");
    }
    
    protected String processCellValue(String cellValue) {

        return cellValue.replaceAll("'", "''");
    }
    
    
}
