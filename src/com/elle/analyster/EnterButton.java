/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.elle.analyster;

/**
 *
 * @author Tina
 */


import com.elle.analyster.presentation.filter.CreateDocumentFilter;

import javax.swing.*;
import javax.swing.text.AbstractDocument;

/**
 *  I am not sure why this is used but it must be legacy code
 *  A JButton should work fine for future implementation.
 * @author cigreja
 */
public class EnterButton {

	int commandStart;
	String command;

	public EnterButton() {
		commandStart = 0;
		command = "";
	}

	public String getCommand(JTextArea text) {

		commandStart = text.getText().lastIndexOf(">>") + 2;
		command = text.getText().substring(commandStart);
		return command;
	}

	public void adjustText(JTextArea text) {
		text.append("\n>>");
		((AbstractDocument) text.getDocument())
				.setDocumentFilter(new CreateDocumentFilter(text.getText()
						.length()));
	}

	public boolean isCreateTable(JTextArea text) {
		if (getCommand(text).toLowerCase().contains("select"))
			return true;
		else
			return false;
	}

}

