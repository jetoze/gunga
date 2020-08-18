package jetoze.gunga.widget;

import static java.util.Objects.*;

import java.awt.Font;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

public class CheckBoxWidget implements Widget, Customizable, Selectable {

    private final JCheckBox checkBox;
    private final List<Consumer<Boolean>> listeners = new ArrayList<>();
    
    public CheckBoxWidget() {
        this(null, false);
    }
    
    public CheckBoxWidget(String text) {
        this(text, false);
    }
    
    public CheckBoxWidget(String text, boolean selected) {
        checkBox = new JCheckBox(text, selected);
        checkBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED || e.getStateChange() == ItemEvent.DESELECTED) {
                notifyListeners();
            }
        });
    }
    
    private void notifyListeners() {
        boolean selected = checkBox.isSelected();
        listeners.forEach(lst -> lst.accept(selected));
    }
    
    @Override
    public boolean isSelected() {
        return checkBox.isSelected();
    }
    
    @Override
    public void setSelected(boolean selected) {
        checkBox.setSelected(selected);
    }
    
    @Override
    public void addChangeListener(Consumer<Boolean> listener) {
        this.listeners.add(requireNonNull(listener));
    }
    
    @Override
    public void removeChangeListener(Consumer<Boolean> listener) {
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

    @Override
    public Font getFont() {
        return checkBox.getFont();
    }

    @Override
    public void setFont(Font font) {
        checkBox.setFont(requireNonNull(font));
    }
    
    public void setEnabled(boolean enabled) {
        checkBox.setEnabled(enabled);
    }
}
