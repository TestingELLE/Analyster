/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.entities;

/**
 *
 * @author corinne
 */
public class Analyst {
    public enum Status{ACTIVE, INACTIVE}
    int id;
    String name;
    Status status;
   
    public Analyst(int id, String name, Status status){
        this.id = id;
        this.name = name;
        this.status = status;
    }
    
    public String toString(){
        return name;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public int getId (){
        return id;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public void setStatus(Status status){
        this.status = status;
    }
    
    public Status getStatus(){
        return status;
    }
    
}
