
package com.elle.analyster.newBackupDBTableDesign;

/**
 * BackupDBTableRecord
 * @author Carlos Igreja
 * @since  Mar 7, 2016
 */
public class BackupDBTableRecord {

    private String id;
    private String tableName;
    private String backupTableName;

    public BackupDBTableRecord(String id, String tableName, String backupTableName) {
        this.id = id;
        this.tableName = tableName;
        this.backupTableName = backupTableName;
    }
    
    public BackupDBTableRecord(){
        this(null,null,null);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getBackupTableName() {
        return backupTableName;
    }

    public void setBackupTableName(String backupTableName) {
        this.backupTableName = backupTableName;
    }
    
    
}
