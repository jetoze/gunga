package jetoze.gunga.selection;

import com.google.common.collect.ImmutableList;

public interface Selection<T> {

    boolean isEmpty();
    
    ImmutableList<T> getItems();
    
}
