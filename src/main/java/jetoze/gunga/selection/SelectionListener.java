package jetoze.gunga.selection;

public interface SelectionListener<T> {

    // TODO: Also pass along the SelectionSource?
    // TODO: Should this take a Selection<? extends T> as input?
    void selectionChanged(Selection<T> selection);
    
}
