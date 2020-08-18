package jetoze.gunga.widget;

import static java.util.Objects.requireNonNull;

import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JToggleButton;

class SelectableButtonSupport {
    
    private final List<Consumer<Boolean>> listeners = new ArrayList<>();
    private final JToggleButton button;

    SelectableButtonSupport(JToggleButton button) {
        this.button = button;
        button.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED || e.getStateChange() == ItemEvent.DESELECTED) {
                notifyListeners();
            }
        });
    }
    
    private void notifyListeners() {
        boolean selected = button.isSelected();
        listeners.forEach(lst -> lst.accept(selected));
    }

    void addListener(Consumer<Boolean> listener) {
        this.listeners.add(requireNonNull(listener));
    }

    void removeListener(Consumer<Boolean> listener) {
        this.listeners.remove(requireNonNull(listener));
    }

}
