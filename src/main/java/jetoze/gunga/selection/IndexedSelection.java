package jetoze.gunga.selection;

import com.google.common.collect.ImmutableList;

public interface IndexedSelection<T> extends Selection<T> {

    ImmutableList<Integer> getIndexes();
    
}
