/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.controller;


import com.elle.analyster.dao.AbstractDAO;
import com.elle.analyster.entities.DbEntity;
import com.elle.analyster.logic.ITableConstants;
import com.elle.analyster.logic.LoggingAspect;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Yi
 */
public abstract class DBTableController<T extends DbEntity> implements ITableConstants {
    
    protected Map<Integer, T> onlineItems;
    
    protected AbstractDAO onlineDAO;
    
    protected String tableName;
    
    
    public DBTableController() {
        onlineItems = new HashMap();
       
    }
    
    
    /*
    ** online data operations
    */
    private boolean insertOnline(T item) {
        if(onlineDAO.insert(item)) {
            onlineItems.put(item.getId(), item);  
            
            return true;
                
        }
        return false;
    }
    
    private boolean updateOnline(T item) {
        if(onlineDAO.update(item)) {
            onlineItems.put(item.getId(), item);
           
            return true;
        }
        
        return false;
        
    }
    
    private boolean deleteOnline(int id) {
        if(onlineDAO.delete(id)){
            onlineItems.remove(id);
            
            return true;
            
        }
        return false;
    }
    
    
    
    
    //populate data from db
    public void getAll() {
        //reset data
        onlineItems = new HashMap();

        //get data
        List<T> items = onlineDAO.getAll();

        for (T item : items) {
            onlineItems.put(item.getId(), item);
        }

        System.out.println("Table " + tableName + " is loaded from database.");
    }
    
   
   
    public T get(int id) {
        return (T) onlineItems.get(id);
    };
    
    
    
    
    public int totalOnlineCnt() {
        return onlineItems.size();
    }
    
    
    /*
    ** interface methods for crud
    */
    public void create(T item) {
   
        if (insertOnline(item)) {
            
            LoggingAspect.afterReturn("New record #" + item.getId() + " is inserted into table " + tableName + ".");
       
        }
        else{
            LoggingAspect.afterReturn("Could not create the record, please try again later.");
        }

    }

    
    public void update(T item) {
    
        int id = item.getId();
        
        if(updateOnline(item)) {
             
            LoggingAspect.afterReturn("Record #" + item.getId() + " is updated in table " + tableName + ".");
        }
        else{
            LoggingAspect.afterReturn("Could not update the record, please try again later.");
        }
 
    }

    public void delete(int id) {
        
        if(deleteOnline(id)) {
            LoggingAspect.afterReturn("Record #" + id + " is deleted from table " + tableName + ".");
        }
        else{
            LoggingAspect.afterReturn("Record #" + id + " failed to be deleted from " + tableName + ", please try again later.");
        }
     
    }

    
    /*
    **getters and setters
    */
    public Map<Integer, T> getOnlineItems() {
        return onlineItems;
    }

    public void setOnlineItems(Map<Integer, T> onlineItems) {
        this.onlineItems = onlineItems;
    }

    
    
    
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

   
   
}
