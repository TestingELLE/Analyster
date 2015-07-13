package com.elle.analyster.presentation.filter;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.*;

public class ActionCheckListModel<T> implements ICheckListModel<T> {
    
    private final List<ListDataListener> listeners = Collections.synchronizedList( new ArrayList<ListDataListener>());
    private final ICheckListModel<T> originalModel;
    
    private final ICheckListAction<T> actionCheckAll = new ICheckListAction.CheckAll<T>();
    
    @SuppressWarnings("unchecked")
    private final List<ICheckListAction<T>> actionItems = Arrays.asList( actionCheckAll );
    private final Set<ICheckListAction<T>> checks = new HashSet<ICheckListAction<T>>();
    
    public ActionCheckListModel( final ICheckListModel<T> originalModel ) {
        
        if ( originalModel == null ) throw new NullPointerException();
        this.originalModel = originalModel;
        
        //react on original model changes
        this.originalModel.addListDataListener( new ListDataListener () {
            
            @Override
            public void intervalAdded(ListDataEvent e) {
                ListDataEvent event = toDecoratedEvent(e);
                for( ListDataListener l: listeners ) {
                    l.intervalAdded(event);
                }
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                ListDataEvent event = toDecoratedEvent(e);
                for( ListDataListener l: listeners ) {
                    l.intervalRemoved(event);
                }
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                ListDataEvent event = toDecoratedEvent(e);
                for( ListDataListener l: listeners ) {
                    l.contentsChanged(event);
                }
                if ( originalModel.getCheckedItems().size() < originalModel.getSize() ) {
                    checks.remove(actionCheckAll);
                }  else {
                    checks.add(actionCheckAll);
                }
                fireListDataChanged();
            }
        });
    }
    
    @Override
    public int getSize() {
        return originalModel.getSize() + actionItems.size();
    }

    @Override
    public Object getElementAt(int index) {
        if ( isDecoratedIndex(index)) {
            return actionItems.get(index);
        } else {
            return originalModel.getElementAt( toOriginalIndex(index));
        }
    }
    
    private int toOriginalIndex( int index ) {
        return index - actionItems.size();
    }

    private int toDecoratedIndex( int index ) {
        return index + actionItems.size();
    }
    
    private boolean isDecoratedIndex( int index ) {
        int size = actionItems.size();
        return size > 0 && index >= 0 && index < size; 
    }

    
    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }
    
    private void fireListDataChanged() {
        ListDataEvent e = new ListDataEvent( this, 0, 0, getSize() );
        for( ListDataListener l: listeners ) {
            l.contentsChanged(e);
        }
    }

    private ListDataEvent toDecoratedEvent( ListDataEvent e ) {
        return new ListDataEvent(
            e.getSource(), 
            e.getType(), 
            toDecoratedIndex(e.getIndex0()), 
            toDecoratedIndex(e.getIndex1()));
    }
    
    @Override
    public boolean isCheckedIndex(int index) {
        if ( isDecoratedIndex(index)) {
            return checks.contains(actionItems.get(index));
        } else {
            return originalModel.isCheckedIndex( toOriginalIndex(index));
        }
        
    }

    @Override
    public void setCheckedIndex(int index, boolean value) {
        if ( isDecoratedIndex(index)) {
            ICheckListAction<T> item = actionItems.get(index);
            item.check(originalModel, value);
            if ( value ) checks.add(item); else checks.remove(item);
            fireListDataChanged();
        } else {
             originalModel.setCheckedIndex( toOriginalIndex(index), value);
        }
    }

    @Override
    public Collection<T> getCheckedItems() {
        return originalModel.getCheckedItems();
    }

    @Override
    public void setCheckedItems(Collection<T> items) {
        originalModel.setCheckedItems(items);
    }

    @Override
    public void filter(String pattern, IListFilter listFilter) {
        originalModel.filter(pattern, listFilter);
    }

}