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
 *
 * @author Carlos Igreja
 */
public interface IAdminComponent {
    
    public abstract void setComponent(AddRecordsWindow window);
    public abstract void setComponent(AnalysterWindow window);
    public abstract void setComponent(BackupDBTablesDialog window);
    public abstract void setComponent(BatchEditWindow window);
    public abstract void setComponent(EditDatabaseWindow window);
    public abstract void setComponent(LogWindow window);
    public abstract void setComponent(LoginWindow window);
    public abstract void setComponent(ReportWindow window);
}
