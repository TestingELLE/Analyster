package com.elle.analyster.logic;

/**
 * Converts a file path format to either windows or non-windows format.
 * Windows uses backslash for file paths.
 * Non-Windows uses forward slash for file paths.
 * This class can be used to convert file path formats.
 * @author Carlos Igreja
 * @since  1-20-2016
 */
public class FilePathFormat {
    
    /**
     * Converts a file path format to either windows or non-windows format.
     * @param path path string to convert
     * @param toWindows true converts to windows path and false to non-windows
     * @return path converted to specified platform's file path format
     */
    public static String convert(String path, Boolean toWindows){
        
        final String F_SLASH = "/";   // forward slash for non- windows path
        final String B_SLASH = "\\";  // backslash for windows path

        // get the path directories
        String[] dirs; 
        if(path.contains(F_SLASH))
            dirs = path.split(F_SLASH);
        else
            dirs = path.split(B_SLASH + B_SLASH); // regex = \\\\ -> \\
        
        // get path slash
        String slash = (toWindows)? B_SLASH : F_SLASH;
        
        // get the new path
        path = ""; 
        for (String dir : dirs){
            path += dir + slash;
        }
        
        return path;
    }
}
