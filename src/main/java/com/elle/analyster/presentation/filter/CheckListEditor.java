
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
// */
final class CheckListEditor extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {

        if (!SwingUtilities.isLeftMouseButton(e)) return;

        JList list = (JList) e.getSource();
        if ( !list.isEnabled() || (!(list.getModel() instanceof ICheckListModel<?>))) return;

        int index = list.locationToIndex(e.getPoint());
        if (index < 0) return;

        Rectangle bounds = list.getCellBounds(index, index);

        if ( bounds.contains(e.getPoint()) ) {
            
            @SuppressWarnings("unchecked")
            ICheckListModel<Object> model = (ICheckListModel<Object>) list.getModel();
            
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
