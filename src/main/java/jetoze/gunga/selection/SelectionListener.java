package jetoze.gunga.selection;

public interface SelectionListener<T> {

    // TODO: Should this take a Selection<? extends T> as input?
    public void selectionChanged(Selection<T> selection);
    
}
