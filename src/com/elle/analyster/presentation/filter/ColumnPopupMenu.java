/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.presentation.filter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author cigreja
 */
public class ColumnPopupMenu extends JPopupMenu{
    
    // attributes
    CheckBoxList cbList;
    
    /**
     * CONSTRUCTOR
     * ColumnPopupMenu
     * creates a ColumnPopupMenu
     */
    public ColumnPopupMenu(){
        initComponents();
    }
    
    /**
     * initComponents
     * initialize the components of the ColumnPopupMenu
     */
    private void initComponents() {
        
        // create a new JPanel
        JPanel panel = new JPanel(new BorderLayout(3, 3));
        panel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panel.setPreferredSize(new Dimension(250, 300)); // default popup size
        
        // create the checkbox JList 
        cbList = new CheckBoxList(); // JList
        
        // load JList with checkbox items
        // this list is just for testing
        ArrayList<JCheckBox> checkBoxItems = new ArrayList<>();
        for(int i = 0; i<20; i++){
            checkBoxItems.add(new JCheckBox("item " + i));
        }
        cbList.setListData(checkBoxItems.toArray());
        
        // add the check box list to the panel
        panel.add(new JScrollPane(cbList), BorderLayout.CENTER); // add list to center

        // create a new Box for the buttons
        Box boxButtons = new Box(BoxLayout.LINE_AXIS);

        // add horizontal glue to the box
        boxButtons.add(Box.createHorizontalGlue());
        
        // create Apply button
        JButton btnApply = new JButton("Apply");
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //applyColumnFilter();
            }
        });
        
        // create Cancel button
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setVisible(false);
            }
        });
        
        // add buttons and look and feel to box of commands
        boxButtons.add(btnApply);
        boxButtons.add(Box.createHorizontalStrut(5));
        boxButtons.add(btnCancel);
        boxButtons.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
        boxButtons.setBackground(UIManager.getColor("Panel.background"));
        boxButtons.setOpaque(true);
        
        // add the box of buttons to the panel
        panel.add(boxButtons, BorderLayout.SOUTH); // add command buttons to south of panel
        
        // add the panel to the ColumnPopupMenu
        this.add(panel);
        
    }
    
    /**
     * showPopupMenu
     * This method determines what column was clicked 
     * and displays the popup under it according to the location of the column.
     * @param e 
     */
    public void showPopupMenu(MouseEvent e) {
        
        // Determine the header and column model that was clicked
        JTableHeader header = (JTableHeader) (e.getSource());
        TableColumnModel colModel = header.getTable().getColumnModel();

        // The index of the column whose header was clicked
        int vColumnIndex = colModel.getColumnIndexAtX(e.getX());
        if (vColumnIndex < 0) {
            return;
        }

        // Determine if mouse was clicked between column heads
        Rectangle headerRect = header.getTable().getTableHeader().getHeaderRect(vColumnIndex);
        if (vColumnIndex == 0) {
            headerRect.width -= 2;
        } else {
            headerRect.grow(-2, 0);
        }

        // Mouse was clicked between column heads
        if (!headerRect.contains(e.getX(), e.getY())) {
            return;
        }

        // show pop-up
        this.show(header, headerRect.x, header.getHeight());
    }
}
