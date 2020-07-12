package jetoze.gunga.selection;

public interface SelectionSource<T> {

    Selection<T> getSelection();
    
    // Consider moving this to a sub-interface MutableSelectionSource.
    void setSelection(Selection<T> selection);
    
    void addSelectionListener(SelectionListener<T> listener);
    
    void removeSelectionListener(SelectionListener<T> listener);
    
}
