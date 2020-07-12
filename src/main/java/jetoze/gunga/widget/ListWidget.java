package jetoze.gunga.widget;

import static com.google.common.base.Preconditions.*;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jetoze.gunga.selection.IndexedSelection;
import jetoze.gunga.selection.Selection;
import jetoze.gunga.selection.SelectionListener;
import jetoze.gunga.selection.SelectionSource;
import jetoze.gunga.selection.Selections;
import jetoze.gunga.selection.Selections.IndexedSelectionBuilder;

public class ListWidget<T> implements SelectionSource<T> {
    // TODO: Add methods for selection mode, and of course something that allows us to add
    // a ListWidget to a layout.
    
    private final JList<T> list;
    private final List<SelectionListener<T>> selectionListeners = new ArrayList<>();
    
    public ListWidget() {
        this(new ListWidgetModel<T>());
    }
    
    public ListWidget(List<T> elements) {
        this(new ListWidgetModel<>(elements));
    }
    
    public ListWidget(ListWidgetModel<T> model) {
        requireNonNull(model);
        list = new JList<>(model);
        list.addListSelectionListener(new SelectionDispatcher());
    }
    
    public ListWidgetModel<T> getModel() {
        return (ListWidgetModel<T>) list.getModel();
    }
    
    public void setModel(ListWidgetModel<T> model) {
        requireNonNull(model);
        list.setModel(model);
    }
    
    public void setFilter(@Nullable Predicate<? super T> filter) {
        getModel().setFilter(filter);
    }
    
    public void setVisibleRowCount(int rows) {
        list.setVisibleRowCount(rows);
    }

    @Override
    public IndexedSelection<T> getSelection() {
        int[] indexes = list.getSelectedIndices();
        if (indexes.length == 0) {
            return Selections.emptyIndexedSelection();
        }
        ListWidgetModel<T> model = getModel();
        IndexedSelectionBuilder<T> builder = Selections.indexedSelectionBuilder();
        for (int index : indexes) {
            builder.add(index, model.getElementAt(index));
        }
        return builder.build();
    }

    @Override
    public void setSelection(Selection<T> selection) {
        requireNonNull(selection);
        if (selection.isEmpty()) {
            list.clearSelection();
        }
        checkArgument(selection instanceof IndexedSelection, "Not an IndexedSelection");
        IndexedSelection<?> is = (IndexedSelection<?>) selection;
        int[] indexes = new int[is.getIndexes().size()];
        int n = 0;
        for (Integer i : is.getIndexes()) {
            indexes[n] = i;
            ++n;
        }
        list.setSelectedIndices(indexes);
    }

    @Override
    public void addSelectionListener(SelectionListener<T> listener) {
        requireNonNull(listener);
        selectionListeners.add(listener);
    }

    @Override
    public void removeSelectionListener(SelectionListener<T> listener) {
        requireNonNull(listener);
        selectionListeners.remove(listener);
    }
    
    
    private class SelectionDispatcher implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                return;
            }
            IndexedSelection<T> newSelection = getSelection();
            for (SelectionListener<T> lst : selectionListeners) {
                lst.selectionChanged(newSelection);
            }
        }
    }
}
