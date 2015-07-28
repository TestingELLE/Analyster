
package com.elle.analyster.presentation.filter;

/**
 *CLASS: CheckBoxItem
 * This class to store information about check box item objects.
 * @author cigreja
 */
public class CheckBoxItem {
    
    // attributes
    private String distinct;      // original distinct value used by filter
    private String capped;        // capped value that is diplayed for checkbox item selection
    private int count;            // count of distinct items to display along side the check box selections

    public CheckBoxItem(String distinct, String capped, int count) {
        this.distinct = distinct;
        this.capped = capped;
        this.count = count;
    }

    public String getDistinct() {
        return distinct;
    }

    public void setDistinct(String distinct) {
        this.distinct = distinct;
    }

    public String getCapped() {
        return capped;
    }

    public void setCapped(String capped) {
        this.capped = capped;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    
    
    
}
