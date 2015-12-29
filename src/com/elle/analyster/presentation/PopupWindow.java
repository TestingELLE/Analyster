
package com.elle.analyster.presentation;

import com.elle.analyster.logic.CheckBoxItem;
import com.elle.analyster.logic.CheckBoxList;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.ScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

/**
 *
 * @author Carlos
 */
public class PopupWindow extends JFrame{
    
    // this uses border layout basically for testing
    public PopupWindow(){
        // create a new JPanel
        JPanel panel = new JPanel(new BorderLayout(3, 3));
        panel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panel.setPreferredSize(new Dimension(250, 300)); // default popup size
        
        // create the checkbox JList 
        CheckBoxList checkBoxList = new CheckBoxList(); // JList
        
        // check box item array
        ArrayList<CheckBoxItem> checkBoxItems = new ArrayList<>();
        checkBoxItems.add(new CheckBoxItem("item 1"));
        
        checkBoxList.setListData(checkBoxItems.toArray());
        
        //checkBoxList.add(new CheckBoxItem("item 1"));
        
        // add mouseListener to the list
        checkBoxList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e)
            {
                // get the checkbox item index
               int index = checkBoxList.locationToIndex(e.getPoint());

               // index cannot be null
               if (index != -1) {
                   
                   // get the check box item at this index
                  JCheckBox checkbox = (JCheckBox)
                              checkBoxList.getModel().getElementAt(index);
                  
                  // check if the (All) selection was checked
                  if(checkbox.getText().equals("(All)")){
                      if(checkbox.isSelected()){
                          //removeAllChecks(getColumnIndex());
                      }
                      else{
                          //checkAll(getColumnIndex());
                      }
                  }
                  else{
                      // toogle the check for the checkbox item
                      checkbox.setSelected(!checkbox.isSelected());
                  }
                  repaint(); // redraw graphics
               }
            }
        });
        
        // add the check box list to the panel
        panel.add(new JScrollPane(checkBoxList), BorderLayout.CENTER); // add list to center

        // create a new Box for the buttons
        Box boxButtons = new Box(BoxLayout.LINE_AXIS);

        // add horizontal glue to the box
        boxButtons.add(Box.createHorizontalGlue());
        
        // create Apply button
        JButton btnApply = new JButton("Apply");
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //applyColumnFilter(getColumnIndex());
                setVisible(false);
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
        add(panel);
        
        // frame
        setSize(new Dimension(500, 500));
        setVisible(true);
    }
    
    // this uses super title
    public PopupWindow(String title){
        super(title);
    }
    
    // this uses gridbag layout
    public PopupWindow(String title, String message, CheckBoxList checkBoxList, JButton[] buttons, Dimension dimension){
        
        // set title
        this.setTitle(title);
        
        // change layout of frame
        this.setLayout(new GridBagLayout());
        
        // add message
        addMessage(message);
        
        // add component
        addComponent(checkBoxList);
        
        // add buttons
        addButtons(buttons);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(dimension);

        this.pack();
    }
    
    private void addMessage(String message){
        
    }
    
    private void addComponent(CheckBoxList checkBoxList){
        
        // set constraints for the component
        GridBagConstraints scrollPanelConstraints = new GridBagConstraints();
        scrollPanelConstraints.fill = GridBagConstraints.BOTH;
        scrollPanelConstraints.weightx = 1; // takes up whole x axis
        scrollPanelConstraints.weighty = 1; // takes up most y axis with room for buttons
        scrollPanelConstraints.gridx = 0; // first col cell
        scrollPanelConstraints.gridy = 0; // first row cell
        
        // wtf
        JList list = new JList(new Object[]{"item 1", "item 2", "item 3", "item 4"});
        checkBoxList.setVisible(true);
        
        CheckBoxList cbl = new CheckBoxList();
        cbl.add(new CheckBoxItem("item 1"));

        // add component panel to frame
        this.add(checkBoxList, scrollPanelConstraints);
    }
    
    private void addButtons(JButton[] buttons){
        
        // create a panel for buttons
        JPanel panelButtons = new JPanel();

        // add buttons to panel
        for(JButton button: buttons){
            panelButtons.add(button);
        }

        // set constraints for the buttons panel
        GridBagConstraints buttonsPanelConstraints = new GridBagConstraints();
        buttonsPanelConstraints.fill = GridBagConstraints.BOTH;
        buttonsPanelConstraints.weightx = 1; // takes up whole x axis
        buttonsPanelConstraints.weighty = 1; // takes up enough y axis just for buttons
        buttonsPanelConstraints.gridx = 0; // first col cell
        buttonsPanelConstraints.gridy = 1; // second row cell

        // add panel to the frame
        this.add(panelButtons,buttonsPanelConstraints);
    }
    
}
