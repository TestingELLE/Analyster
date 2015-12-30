/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.presentation;

import com.elle.analyster.logic.CheckBoxItem;
import com.elle.analyster.logic.CheckBoxList;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Carlos
 */
public class PopupWindowTest {
    
    public PopupWindowTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() {
        
        String title = "title here";
        String message = "message here";
        
        // CheckBoxList
        CheckBoxList checkBoxList = new CheckBoxList();
        
        //checkBoxList.setSize(new Dimension(100, 100));
        
        // check box item array
        ArrayList<CheckBoxItem> checkBoxItems = new ArrayList<>();
        checkBoxItems.add(new CheckBoxItem("item 1"));
        checkBoxItems.add(new CheckBoxItem("item 2"));
        checkBoxItems.add(new CheckBoxItem("item 3"));
        checkBoxItems.add(new CheckBoxItem("item 4"));
        
        checkBoxList.setListData(checkBoxItems.toArray());
        
        // buttons
        JButton[] buttons = new JButton[]{new JButton("Button 1"), new JButton("Button 2"), new JButton("Button 3")};
        
        // dimension
        Dimension dimension = new Dimension(500,500);
        
        PopupWindow popup = new PopupWindow(title, message, checkBoxList, buttons, dimension);
        popup.setLocationRelativeTo(null);
        popup.setVisible(true);
        
        PopupWindow popup2BorderLayout = new PopupWindow();
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
        
        

    }
    
}
