package jetoze.gunga.widget;

import static java.util.Objects.*;

import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

public class CheckBoxWidget implements Widget {

    private final JCheckBox checkBox;
    private final List<Consumer<Boolean>> listeners = new ArrayList<>();
    
    public CheckBoxWidget(String text) {
        this(text, false);
    }
    
    public CheckBoxWidget(String text, boolean selected) {
        checkBox = new JCheckBox(text, selected);
        checkBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                notifyListeners();
            }
        });
    }
    
    private void notifyListeners() {
        boolean selected = checkBox.isSelected();
        listeners.forEach(lst -> lst.accept(selected));
    }
    
    public boolean isSelected() {
        return checkBox.isSelected();
    }
    
    public void setSelected(boolean selected) {
        checkBox.setSelected(selected);
    }
    
    public void addSelectionListener(Consumer<Boolean> listener) {
        this.listeners.add(requireNonNull(listener));
    }
    
    public void removeSelectionListener(Consumer<Boolean> listener) {
        this.listeners.remove(requireNonNull(listener));
    }
    
    @Override
    public JComponent getUi() {
        return checkBox;
    }

    @Override
    public void requestFocus() {
        checkBox.requestFocusInWindow();
    }

}
