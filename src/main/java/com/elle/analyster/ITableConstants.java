/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster;

/**
 * @author cigreja
 * @since  July 16, 2015
 * 
 * This interface stores all the table constants
 */
public interface ITableConstants {
    
    final String ASSIGNMENTS_TABLE_NAME = "Assignments";
    final String REPORTS_TABLE_NAME = "Reports";
    final String ARCHIVE_TABLE_NAME = "Assignments_Archived";
    
    // column header name constants
    final String SYMBOL_COLUMN_NAME = "Symbol";
    
    // column width percent constants
    final float[] COL_WIDTH_PER_ASSIGNMENTS = {35, 65, 80, 70, 99, 99};
    final float[] COL_WIDTH_PER_REPORTS = {35, 65, 80, 100, 160, 120, 123};
    final float[] COL_WIDTH_PER_ARCHIVE = {35, 65, 80, 70, 99, 99};
}
