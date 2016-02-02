package com.elle.analyster.logic;

import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * This tool opens a file with the default application on the system.
 * @author Carlos Igreja
 * @since  1-20-2016
 */
public class OpenDocumentTool {
    
    private String elle_folder;
    private String doc_path;
    private String doc_file;
    private Component parent = null;
    
    public OpenDocumentTool(String elle_folder, String doc_path, String doc_file){
        
        this.elle_folder = elle_folder;
        this.doc_path = doc_path;
        this.doc_file = doc_file;
    }
    
    public boolean open(){
        
        // is windows
        boolean isWindows = FilePathFormat.isWindows();
        
        String path = getPath(isWindows);
        
        try {
            File file = new File(path);
            if(!file.exists()){
                JOptionPane.showMessageDialog(parent, "File does not exist!");
                return false;
            }
            Desktop.getDesktop().open(file);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(OpenDocumentTool.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent, ex.getMessage());
            return false;
        }
    }
    
    public String getPath(boolean forWindows){
        String path = "";
        path += FilePathFormat.convert(elle_folder, forWindows);
        path += FilePathFormat.convert(doc_path, forWindows);
        path += doc_file;
        return path;
    }

    public void setParent(Component parent) {
        this.parent = parent;
    }
}
