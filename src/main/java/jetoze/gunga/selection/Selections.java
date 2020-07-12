package jetoze.gunga.selection;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static tzeth.preconds.MorePreconditions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public final class Selections {

    private static final Selection<?> EMPTY_SELECTION = new SelectionImpl<>();
    
    private static final IndexedSelection<?> EMPTY_INDEXED_SELECTION = new IndexedSelectionImpl<>();
    
    public static <T> Selection<T> emptySelection() {
        @SuppressWarnings("unchecked")
        Selection<T> s = (Selection<T>) EMPTY_SELECTION;
        return s;
    }
    
    public static <T> IndexedSelection<T> emptyIndexedSelection() {
        @SuppressWarnings("unchecked")
        IndexedSelection<T> s = (IndexedSelection<T>) EMPTY_INDEXED_SELECTION;
        return s;
    }
    
    @SafeVarargs
    public static <T> Selection<T> of(T... items) {
        return new SelectionImpl<>(ImmutableList.copyOf(items));
    }
    
    public static <T> Selection<T> of(Collection<T> items) {
        return new SelectionImpl<>(ImmutableList.copyOf(items));
    }
    
    public static <T> IndexedSelectionBuilder<T> indexedSelectionBuilder() {
        return new IndexedSelectionBuilder<T>();
    }
    

    private static class SelectionImpl<T> implements Selection<T> {
        private final ImmutableList<T> items;

        public SelectionImpl() {
            items = ImmutableList.of();
        }
        
        public SelectionImpl(ImmutableList<T> items) {
            this.items = requireNonNull(items);
        }

        @Override
        public boolean isEmpty() {
            return items.isEmpty();
        }

        @Override
        public ImmutableList<T> getItems() {
            return items;
        }
    }
    
    
    
    private static class IndexedSelectionImpl<T> extends SelectionImpl<T> implements IndexedSelection<T> {
        private final ImmutableSet<Integer> indexes;

        public IndexedSelectionImpl() {
            this.indexes = ImmutableSet.of();
        }
        
        public IndexedSelectionImpl(ImmutableList<T> items, ImmutableSet<Integer> indexes) {
            super(items);
            this.indexes = requireNonNull(indexes);
            checkArgument(items.size() == indexes.size(), "Must provide the same number of indexes and items");
        }

        @Override
        public ImmutableSet<Integer> getIndexes() {
            return indexes;
        }
    }
    
    public static class IndexedSelectionBuilder<T> {
        private final Set<Integer> indexes = new HashSet<>();
        private final List<T> items = new ArrayList<>();
        
        public IndexedSelectionBuilder<T> add(int index, T item) {
            checkNotNegative(index);
            requireNonNull(item);
            checkArgument(!indexes.contains(index), "Duplidate index: " + index);
            indexes.add(index);
            items.add(item);
            return this;
        }
        
        public IndexedSelection<T> build() {
            return new IndexedSelectionImpl<>(ImmutableList.copyOf(items), ImmutableSet.copyOf(indexes));
        }
    }
    
    
    private Selections() {/**/}
    
}
