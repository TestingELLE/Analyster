
package com.elle.analyster.database;

import com.elle.analyster.logic.CheckBoxItem;
import com.elle.analyster.logic.CheckBoxList;
import java.sql.Connection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Carlos
 */
public class BackupDBTableOrganizerTest {
    
    public BackupDBTableOrganizerTest() {
        testSomeMethod();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() {
        
        // create BackupDBTables object with parameters to create a connection
        BackupDBTables backupDBTables = new BackupDBTables("localhost", "dummy", "root", "", null);
        Connection connection = backupDBTables.getConnection();
        
        // test the dialog box
        BackupDBTableOrganizer backup = new BackupDBTableOrganizer(connection);
        
        // checkboxlist
        CheckBoxList cbl = new CheckBoxList();
        cbl.add(new CheckBoxItem("item 1"));
        cbl.add(new CheckBoxItem("item 2"));
        cbl.add(new CheckBoxItem("item 3"));
        cbl.add(new CheckBoxItem("item 4"));
        cbl.add(new CheckBoxItem("item 5"));
        
        
        //backup.showDialogBox(null, cbl);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
