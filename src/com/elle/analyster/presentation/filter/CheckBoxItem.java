package com.elle.analyster.presentation.filter;

import javax.swing.JCheckBox;

public class CheckBoxItem  extends JCheckBox{
    
    // attributes
    private String value; // item value
    
    /**
     * CONSTRUCTOR
     * CheckBoxItem
     * @param value 
     */
    public CheckBoxItem(String value) {
        super(value);
        this.value = value;
    }

    /**
     * getValue
     * @return 
     */
    public String getValue() {
        return value;
    }

    /**
     * setValue
     * @param value 
     */
    public void setValue(String value) {
        this.value = value;
    }
    
    
}
