
package com.elle.analyster.admissions;

import com.elle.analyster.presentation.AddRecordsWindow;
import com.elle.analyster.presentation.AnalysterWindow;
import com.elle.analyster.presentation.BackupDBTablesDialog;
import com.elle.analyster.presentation.BatchEditWindow;
import com.elle.analyster.presentation.EditDatabaseWindow;
import com.elle.analyster.presentation.LogWindow;
import com.elle.analyster.presentation.LoginWindow;
import com.elle.analyster.presentation.ReportWindow;

/**
 * User
 * The user access level configuration
 * @author Carlos Igreja
 * @since  Mar 1, 2016
 */
public class User implements IAdminComponent{

    @Override
    public void setComponent(AddRecordsWindow window) {
    }

    @Override
    public void setComponent(AnalysterWindow window) {
        
        //#A363
        //2. Set the access level for the following menu commands to "developer"
        //FILE: Select Connection, Print, Save File
        window.getMenuSelectConn().setEnabled(false);
        window.getMenuPrint().setEnabled(false);
        window.getMenuItemSaveFile().setEnabled(false);
        //EDIT: Manage databases
        window.getMenuItemManageDBs().setEnabled(false);
        //Tools: Log, SQL command
        window.getMenuItemLogChkBx().setEnabled(false);
        window.getMenuItemSQLCmdChkBx().setEnabled(false);
    }

    @Override
    public void setComponent(BackupDBTablesDialog window) {
    }

    @Override
    public void setComponent(BatchEditWindow window) {
    }

    @Override
    public void setComponent(EditDatabaseWindow window) {
    }

    @Override
    public void setComponent(LogWindow window) {
    }

    @Override
    public void setComponent(LoginWindow window) {
    }

    @Override
    public void setComponent(ReportWindow window) {
    }

}
