
package com.elle.analyster.dao;

import com.elle.analyster.database.DBConnection;
import com.elle.analyster.entities.AccessLevel;
import com.elle.analyster.logic.LoggingAspect;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * AccessLevelDAO
 * @author Carlos Igreja
 * @since  May 12, 2016
 */
public class AccessLevelDAO {

    // database table information

    public static final String DB_ACCOUNTS = "accounts";
    public static final String COL_USERNAME = "username";
    public static final String COL_TYPE = "type";

    public static String get(String user) throws Exception {
        String sql = "SELECT * FROM " + DB_ACCOUNTS +
                      " WHERE " + COL_USERNAME + " = '" + user +"';";

        ResultSet rs = null;
        AccessLevel accessLevel = new AccessLevel();
        
        try {

            DBConnection.close();
            DBConnection.open();
            rs = DBConnection.getStatement().executeQuery(sql);
            
            while(rs.next()){
                accessLevel.setUser(rs.getString(COL_USERNAME));
                accessLevel.setAccessLevel(rs.getString(COL_TYPE));
            }
            
            
            LoggingAspect.afterReturn("Loaded access level from " + DB_ACCOUNTS + " for " + accessLevel.getUser());
        } 
        catch (SQLException e) {
            LoggingAspect.afterThrown(e);
        }
        
        return accessLevel.getAccessLevel();
    }
}
