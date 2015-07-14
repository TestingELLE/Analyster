package com.elle.analyster.presentation.filter;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
//This class it was design to assign table data to filter./////

public class TableRowFilterSupport {
    
    // class attributes
    private boolean actionsVisible;
    private int filterIconPlacement;
    private boolean useTableRenderers;
    
    // class objects
    private JTableFilter filter;
    private TableFilterColumnPopup filterPopup;


    /**
     * CONSTRUCTOR
     * @param table 
     */
    public TableRowFilterSupport( JTable table ) {
        
        // initialize class variables
        filter = new JTableFilter(table);
        actionsVisible = true; // this should start at false and turned on
        filterIconPlacement = SwingConstants.LEADING;
        useTableRenderers = false;
    }
    
    /************************************************************************
     ************************ Setters ***************************************
     ************************************************************************/
    
    public void setFilter(JTableFilter filter){this.filter = filter;}
    public void setActionsVisible( boolean visible ) {actionsVisible = visible;}

    /**
     * Set the placement of the filter icon with respect to the existing icon
     * in the column label.
     *
     * @param filterIconPlacement either SwingConstants.LEADING or
     *         SwingConstants.TRAILING.
     */
    public void filterIconPlacement(int filterIconPlacement) {
        if (filterIconPlacement != SwingConstants.LEADING &&
                filterIconPlacement != SwingConstants.TRAILING) {
            throw new UnsupportedOperationException("The filter icon " +         
                    "placement can only take the values of " +                   
                    "SwingConstants.LEADING or SwingConstants.TRAILING");
        }
        this.filterIconPlacement = filterIconPlacement;
    }

    public void useTableRenderers( boolean value ) { useTableRenderers = value;}

    /************************************************************************
     ************************ Getters ***************************************
     ************************************************************************/
    
    public JTableFilter getFilter(){return filter;}
    public boolean getActionsVisible(){return actionsVisible;}
    public int getFilterIconPlacement(){return filterIconPlacement;}
    public boolean getUseTableRenderers(){return useTableRenderers;}
            
    /************************************************************************
     ************************ Methods ***************************************
     ************************************************************************/        
            
    public void apply() {

        // this should not be called and set here
        filterPopup = new TableFilterColumnPopup(filter);
        filterPopup.setEnabled(true);
        filterPopup.setActionsVisible(actionsVisible);
        filterPopup.setUseTableRenderers( useTableRenderers );

        JTable table = filter.getTable();
        
        FilterTableHeaderRenderer headerRenderer =
                new FilterTableHeaderRenderer(filter, filterIconPlacement);
        
        filter.modelChanged( table.getModel() );  // wouldn't this be the same filter?

        for( TableColumn c:  Collections.list( table.getColumnModel().getColumns()) ) {
            c.setHeaderRenderer( headerRenderer );
        }

        table.addPropertyChangeListener("model", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                filter.modelChanged( (TableModel) e.getNewValue() ); 
            } 
        }); // end addPropertyChangeListener
    } // end apply
}