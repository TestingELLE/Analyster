package com.elle.analyster.logic;

import com.elle.analyster.entities.BackupDBTableRecord;

import javax.swing.*;

/**
 * Created by Carlos on 3/8/2016.
 */
public class BackupTableCheckBoxItem extends JCheckBox {

    BackupDBTableRecord record;

    public BackupTableCheckBoxItem(BackupDBTableRecord record){
        super(record.getBackupTableName());
        this.record = record;
    }

    public BackupTableCheckBoxItem(String backupTableName){
        super(backupTableName);
        record = new BackupDBTableRecord();
        record.setBackupTableName(backupTableName);
    }

    public BackupDBTableRecord getRecord() {
        return record;
    }

    public void setRecord(BackupDBTableRecord record) {
        this.record = record;
    }
}
