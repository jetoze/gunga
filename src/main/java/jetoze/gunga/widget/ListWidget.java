package jetoze.gunga.widget;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.google.common.collect.ImmutableSet;

import jetoze.gunga.DefaultAction;
import jetoze.gunga.selection.IndexedSelection;
import jetoze.gunga.selection.Selection;
import jetoze.gunga.selection.SelectionListener;
import jetoze.gunga.selection.SelectionSource;
import jetoze.gunga.selection.Selections;
import jetoze.gunga.selection.Selections.IndexedSelectionBuilder;

public class ListWidget<T> implements Widget, SelectionSource<T> {
    
    public static enum SelectionMode {
        SINGLE(ListSelectionModel.SINGLE_SELECTION),
        SINGLE_INTERVAL(ListSelectionModel.SINGLE_INTERVAL_SELECTION),
        MULTPLE_INTERVAL(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        private final int value;
        
        private SelectionMode(int value) {
            this.value = value;
        }
    }
    
    private final JList<T> list;
    private final List<SelectionListener<T>> selectionListeners = new ArrayList<>();
    @Nullable
    private DefaultAction defaultAction;
    
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
    
    @Override
    public JComponent getUi() {
        return new JScrollPane(list);
    }

    @Override
    public void requestFocus() {
        list.requestFocusInWindow();
        if (getSelection().isEmpty()) {
            selectFirst();
        }
    }

    public ListWidgetModel<T> getModel() {
        return (ListWidgetModel<T>) list.getModel();
    }
    
    public void setModel(ListWidgetModel<T> model) {
        requireNonNull(model);
        list.setModel(model);
    }
    
    /**
     * Installs a new filter into this list widget.
     * 
     * @param filter the new filter, or {@code null} to clear the filter.
     */
    public void setFilter(@Nullable Predicate<? super T> filter) { // TODO: Should I accept nulls?
        // TODO: The current approach for preserving selected items (if they are 
        // accepted by the new filter) does not take into account the possibility
        // that the list contains duplicate values.
        IndexedSelection<T> selection = getSelection();
        getModel().setFilter(filter);
        if (selection.isEmpty()) {
            return;
        }
        // Those selected items that are still visible (i.e. accepted by the new filter)
        ImmutableSet<T> selectedItems = selection.getItems().stream()
                .filter((filter != null) ? filter : t -> true)
                .collect(toImmutableSet());
        if (selectedItems.isEmpty()) {
            setSelection(Selections.emptyIndexedSelection());
        } else {
            // TODO: This linear search obviously doesn't perform well.
            IndexedSelectionBuilder<T> selectionBuilder = Selections.indexedSelectionBuilder();
            for (int n = 0; n < getModel().getSize(); ++n) {
                T element = getModel().getElementAt(n);
                if (selectedItems.contains(element)) {
                    selectionBuilder.add(n, element);
                    if (selectionBuilder.size() == selectedItems.size()) {
                        break;
                    }
                }
            }
            setSelection(selectionBuilder.build());
        }
    }
    
    public void setCellRenderer(ListCellRenderer<? super T> renderer) {
        requireNonNull(renderer);
        list.setCellRenderer(renderer);
    }
    
    public void setVisibleRowCount(int rows) {
        list.setVisibleRowCount(rows);
    }
    
    public void setEnabled(boolean enabled) {
        list.setEnabled(enabled);
    }
    
    public void ensureIndexIsVisible(int index) {
        list.ensureIndexIsVisible(index);
    }
    
    public void setSelectionMode(SelectionMode mode) {
        list.setSelectionMode(mode.value);
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
    
    public void selectFirst() {
        if (getModel().getSize() > 0) {
            list.setSelectedIndex(0);
        }
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
    
    public void setDefaultAction(Action action) {
        requireNonNull(action);
        if (defaultAction != null) {
            defaultAction.dispose();
        }
        defaultAction = DefaultAction.install(list, action);
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
