
package com.elle.analyster.presentation.filter;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import javax.swing.JList;
import javax.swing.SwingUtilities;

// * Determines mouse click and 
// * 1. Toggles the check on selected item if clicked once
// * 2. Clears checks and checks selected item if clicked more then once
// * 
// * Created on Feb 4, 2011
// * @author Eugene Ryzhikov
// *
// * This is called from CheckList
// * This is just a listener, maybe it can be added to set terminals in Analyster
// */
final class CheckListMouseAdapter extends MouseAdapter {
    
    // this is just one method that listens for when the check List is clicked
    @Override
    public void mouseClicked(MouseEvent e) {

        // if left mouse click then stop here
        if (!SwingUtilities.isLeftMouseButton(e)) return;

        JList list = (JList) e.getSource();
        
        // if list is not enabled or list model is not an instance of ICheckListModel
        if ( !list.isEnabled() || (!(list.getModel() instanceof ICheckListModel<?>))) 
            return; // then stop here 

        // else continue
        int index = list.locationToIndex(e.getPoint());
        
        // if mouse location index less then 0, stop here.
        if (index < 0) return; 

        // another check to see if the mouse is in the cell bounds
        Rectangle bounds = list.getCellBounds(index, index);

        // if mouse is in the cell bounds
        if ( bounds.contains(e.getPoint()) ) {
            
            @SuppressWarnings("unchecked")
            ICheckListModel<Object> model = (ICheckListModel<Object>) list.getModel();
            
            // set the checked items for the model
            if ( e.getClickCount() > 1 ) {
                // clear all and check selected for more then 1 clicks
                model.setCheckedItems( Arrays.asList( model.getElementAt(index)));
            } else {
                // simple toggle for 1 click
                model.setCheckedIndex(index, !model.isCheckedIndex(index));
            }
            
            e.consume();
        }

    }
    
}
