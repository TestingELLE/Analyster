/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.presentation.filter;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class CheckBoxList extends JList
{
    
   protected static Border noFocusBorder =
                                 new EmptyBorder(1, 1, 1, 1);

   public CheckBoxList()
   {
       // set cell renderer
      setCellRenderer(new CellRenderer());

      // add mouse listener for what checkbox is clicke in the List
      addMouseListener(new MouseAdapter()
         {
            public void mousePressed(MouseEvent e)
            {
               int index = locationToIndex(e.getPoint());

               if (index != -1) {
                  JCheckBox checkbox = (JCheckBox)
                              getModel().getElementAt(index);
                  checkbox.setSelected(
                                     !checkbox.isSelected());
                  repaint();
               }
            }
         }
      );

      // selection mode is single selection
      setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
   }

   protected class CellRenderer implements ListCellRenderer
   {
      public Component getListCellRendererComponent(
                    JList list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus)
      {
         JCheckBox checkbox = (JCheckBox) value;
         checkbox.setBackground(isSelected ?
                 getSelectionBackground() : getBackground());
         checkbox.setForeground(isSelected ?
                 getSelectionForeground() : getForeground());
         checkbox.setEnabled(isEnabled());
         checkbox.setFont(getFont());
         checkbox.setFocusPainted(false);
         checkbox.setBorderPainted(true);
         checkbox.setBorder(isSelected ?
          UIManager.getBorder(
           "List.focusCellHighlightBorder") : noFocusBorder);
         return checkbox;
      }
   }
}