package jetoze.gunga.widget;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

import javax.annotation.Nullable;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.ListCellRenderer;

import jetoze.gunga.selection.IndexedSelection;
import jetoze.gunga.selection.Selection;
import jetoze.gunga.selection.SelectionListener;
import jetoze.gunga.selection.SelectionSource;
import jetoze.gunga.selection.Selections;

public class ComboBoxWidget<T> implements Widget, SelectionSource<T> {

    private final JComboBox<T> comboBox;
    
    private final List<SelectionListener<T>> selectionListeners = new ArrayList<>();
    
    private final ItemListener selectionDispatcher = e -> {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            Selection<T> selection = getSelection();
            selectionListeners.forEach(lst -> lst.selectionChanged(selection));
        }
    };
    
    public ComboBoxWidget() {
        comboBox = new JComboBox<>();
        comboBox.addItemListener(selectionDispatcher);
    }
    
    public ComboBoxWidget(Collection<T> items) {
        comboBox = new JComboBox<>(new Vector<>(items));
        comboBox.addItemListener(selectionDispatcher);
    }

    public Optional<T> getSelectedItem() {
        int index = comboBox.getSelectedIndex();
        return (index >= 0)
                ? Optional.of(comboBox.getModel().getElementAt(index))
                : Optional.empty();
    }
    
    public void setSelectedItem(@Nullable T item) {
        comboBox.setSelectedItem(item);
    }
    
    @Override
    public IndexedSelection<T> getSelection() {
        int index = comboBox.getSelectedIndex();
        return (index == -1)
                ? Selections.emptyIndexedSelection()
                : Selections.<T>indexedSelectionBuilder()
                    .add(index, comboBox.getModel().getElementAt(index))
                    .build();
    }

    @Override
    public void setSelection(Selection<T> selection) {
        if (selection.isEmpty()) {
            comboBox.setSelectedIndex(-1);
        }
        checkArgument(selection.getItems().size() == 1, "Can only select one item");
        if (selection instanceof IndexedSelection) {
            comboBox.setSelectedIndex(((IndexedSelection<?>) selection).getIndexes().get(0));
        } else {
            comboBox.setSelectedItem(selection.getItems().get(0));
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
    
    public void setRenderer(ListCellRenderer<? super T> renderer) {
        requireNonNull(renderer);
        comboBox.setRenderer(renderer);
    }

    @Override
    public JComponent getUi() {
        return comboBox;
    }

    @Override
    public void requestFocus() {
        comboBox.requestFocusInWindow();
    }
}
