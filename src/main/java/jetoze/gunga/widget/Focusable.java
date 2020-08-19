package jetoze.gunga.widget;

import static java.util.Objects.*;

import javax.swing.JComponent;

public interface Focusable {

    void requestFocus();
    
    static Focusable of(JComponent component) {
        requireNonNull(component);
        return component::requestFocusInWindow;
    }
    
}
