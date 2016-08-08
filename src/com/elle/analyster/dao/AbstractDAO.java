/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.dao;

import java.util.List;

/**
 *
 * @author Yi
 */
public interface AbstractDAO<T> {
    //implemented in every DAO
    public abstract boolean insert(T item);
    public abstract boolean update(T item);
    public abstract boolean delete(int id);
    public abstract List<T> getAll();
    public abstract T get(int id);
  
}
