package jetoze.gunga.widget;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.Nullable;
import javax.swing.AbstractListModel;

import tzeth.exceptions.NotImplementedYetException;

public class ListWidgetModel<T> extends AbstractListModel<T> {
    private final List<T> elements;
    @Nullable
    private Predicate<? super T> filter;
    @Nullable
    private List<Integer> filteredIndexes;
    
    public ListWidgetModel() {
        elements = new ArrayList<>();
    }
    
    public ListWidgetModel(List<T> elements) {
        requireNonNull(elements);
        checkArgument(elements.stream().allMatch(Objects::nonNull), "Null element not allowed");
        this.elements = new ArrayList<>(elements);
    }
    
    public Optional<Predicate<? super T>> getFilter() {
        return Optional.ofNullable(filter);
    }
    
    public void setFilter(@Nullable Predicate<? super T> filter) {
        if (Objects.equals(filter, this.filter)) {
            return;
        }
        this.filter = filter;
        if (filter == null) {
            filteredIndexes = null;
        } else {
            calculateFilteredIndexes();
        }
        fireContentsChanged(this, -1, -1);
    }
    
    private void calculateFilteredIndexes() {
        assert filter != null;
        filteredIndexes = new ArrayList<>();
        int index = 0;
        for (T e : elements) {
            if (filter.test(e)) {
                filteredIndexes.add(index);
            }
            ++index;
        }
    }
    
    @Override
    public int getSize() {
        return (filteredIndexes == null)
                ? elements.size()
                : filteredIndexes.size();
    }

    @Override
    public T getElementAt(int index) {
        int n = (filteredIndexes == null)
                ? index
                : filteredIndexes.get(index);
        return elements.get(n);
    }
    
    public void addElement(T element) {
        requireNonNull(element);
        int index = getSize();
        elements.add(element);
        if (filter == null) {
            fireIntervalAdded(this, index, index);
        } else if (filter.test(element)) {
            filteredIndexes.add(index);
            fireIntervalAdded(this, index, index);
        } else {
            // Nothing to do -- the new element is not visible, so not necessary
            // to notify any listeners.
        }
    }
    
    public void removeElement(T item) {
        throw new NotImplementedYetException();
    }
    
    public void removeAll() {
        int index = getSize() - 1;
        elements.clear();
        if (index >= 0) {
            fireIntervalRemoved(this, 0, index);
        }
    }
}
