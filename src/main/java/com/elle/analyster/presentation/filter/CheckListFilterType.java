package com.elle.analyster.presentation.filter;

import java.util.regex.PatternSyntaxException;

/**
 * ENUM CheckListFilterType
 * This is called from TableFilterColumnPopup & DefaultCheckListModel
 */
public class CheckListFilterType {


    public boolean include( String element, String pattern ) {

        if ( element == null || pattern == null ) return false;
        return element.contains(pattern);

    }
}