package com.elle.analyster.presentation.filter;

/**
 * INTERFACE IListFilter
 * This is implemented in CheckListFilterType
 */
public interface IListFilter {
    public boolean include(String element, String pattern);
}