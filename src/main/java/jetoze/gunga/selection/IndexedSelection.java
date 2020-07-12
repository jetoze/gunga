package jetoze.gunga.selection;

import com.google.common.collect.ImmutableSet;

public interface IndexedSelection<T> extends Selection<T> {

    ImmutableSet<Integer> getIndexes();
    
}
