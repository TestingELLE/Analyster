package com.elle.analyster.database;

/**
 * Created with IntelliJ IDEA.
 * User: danielabecker
 * Date: 6/5/15
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class ModifiedData {
    private int id;
    private int columnIndex;
    private Object valueModified;
    private String tableName;

    public ModifiedData() {

    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Object getValueModified() {
        return valueModified;
    }

    public void setValueModified(Object valueModified) {
        this.valueModified = valueModified;
    }
}
