package jetoze.gunga.widget;

import static java.util.Objects.*;

import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JCheckBoxMenuItem;

public interface Selectable {

    boolean isSelected();
    
    void setSelected(boolean selected);
    
    void addChangeListener(Consumer<Boolean> listener);
    
    void removeChangeListener(Consumer<Boolean> listener);
    
    
    // TODO: Once we have our own menu item widgets we can probably remove this one.
    public static Selectable of(JCheckBoxMenuItem menuItem) {
        requireNonNull(menuItem);
        return new Selectable() {
            
            private final List<Consumer<Boolean>> listeners = new ArrayList<>();
            
            {
                menuItem.addItemListener(e -> {
                    if (e.getStateChange() == ItemEvent.SELECTED || e.getStateChange() == ItemEvent.DESELECTED) {
                        boolean selected = isSelected();
                        listeners.forEach(lst -> lst.accept(selected));
                    }
                });
            }
            
            @Override
            public boolean isSelected() {
                return menuItem.isSelected();
            }
            
            @Override
            public void setSelected(boolean selected) {
                menuItem.setSelected(selected);
            }
            
            @Override
            public void addChangeListener(Consumer<Boolean> listener) {
                listeners.add(requireNonNull(listener));
            }
            
            @Override
            public void removeChangeListener(Consumer<Boolean> listener) {
                listeners.remove(requireNonNull(listener));
            }
        };
    }
    
}
