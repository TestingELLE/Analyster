/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.logic;

/**
 *
 * @author Yi
 */
public class ButtonsState {

    //buttons related state
    private boolean batchEditBtnVisible;
    private boolean addBtnVisible;
    private boolean uploadChangesBtnVisible;
    private boolean revertChangesBtnVisible;
    private boolean editMode;
    //menuItem state
    private boolean activateRecordEnabled;
    private boolean archiveRecordEnabled;
    private boolean openDocumentEnabled;
    private boolean stripSlashEnabled;
    private boolean addSlashEnabled;

    public ButtonsState() {

    }

    public ButtonsState(boolean addRecord, boolean batchEdit, boolean uploadChange, boolean revertChange, 
            boolean editMode, boolean activateRecordEnabled, boolean archiveRecordEnabled, boolean openDocumentEnabled,
            boolean stripSlashEnabled, boolean addSlashEnabled) {
        this.batchEditBtnVisible = batchEdit;
        this.addBtnVisible = addRecord;
        this.uploadChangesBtnVisible = uploadChange;
        this.revertChangesBtnVisible = revertChange;
        this.editMode = editMode;
        this.activateRecordEnabled = activateRecordEnabled;
        this.archiveRecordEnabled = archiveRecordEnabled;
        this.openDocumentEnabled = openDocumentEnabled;
        this.stripSlashEnabled = stripSlashEnabled;
        this.addSlashEnabled = addSlashEnabled;

    }
    
    //this is the common state change for enable or disable editing
    public void enableEdit(boolean canEdit) {
        if (canEdit) {
            this.addBtnVisible = false;
            this.editMode = true;
            this.uploadChangesBtnVisible = true;
            this.revertChangesBtnVisible = true;    
        }
        else{
            this.addBtnVisible = true;
            this.editMode = false;
            this.uploadChangesBtnVisible = false;
            this.revertChangesBtnVisible = false;    
            
        }
        
        
    }

    public boolean isBatchEditBtnVisible() {
        return batchEditBtnVisible;
    }

    public void setBatchEditBtnVisible(boolean batchEditBtnVisible) {
        this.batchEditBtnVisible = batchEditBtnVisible;
    }

    public boolean isAddBtnVisible() {
        return addBtnVisible;
    }

    public void setAddBtnVisible(boolean addBtnVisible) {
        this.addBtnVisible = addBtnVisible;
    }

    public boolean isUploadChangesBtnVisible() {
        return uploadChangesBtnVisible;
    }

    public void setUploadChangesBtnVisible(boolean uploadChangesBtnVisible) {
        this.uploadChangesBtnVisible = uploadChangesBtnVisible;
    }

    public boolean isRevertChangesBtnVisible() {
        return revertChangesBtnVisible;
    }

    public void setRevertChangesBtnVisible(boolean revertChangesBtnVisible) {
        this.revertChangesBtnVisible = revertChangesBtnVisible;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isActivateRecordEnabled() {
        return activateRecordEnabled;
    }

    public void setActivateRecordEnabled(boolean activateRecordEnabled) {
        this.activateRecordEnabled = activateRecordEnabled;
    }

    public boolean isArchiveRecordEnabled() {
        return archiveRecordEnabled;
    }

    public void setArchiveRecordEnabled(boolean archiveRecordEnabled) {
        this.archiveRecordEnabled = archiveRecordEnabled;
    }

    public boolean isOpenDocumentEnabled() {
        return openDocumentEnabled;
    }

    public void setOpenDocumentEnabled(boolean openDocumentEnabled) {
        this.openDocumentEnabled = openDocumentEnabled;
    }

    public boolean isStripSlashEnabled() {
        return stripSlashEnabled;
    }

    public void setStripSlashEnabled(boolean stripSlashEnabled) {
        this.stripSlashEnabled = stripSlashEnabled;
    }

    public boolean isAddSlashEnabled() {
        return addSlashEnabled;
    }

    public void setAddSlashEnabled(boolean addSlashEnabled) {
        this.addSlashEnabled = addSlashEnabled;
    }
    
    

}
