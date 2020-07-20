package jetoze.gunga;

import javax.swing.Action;
import javax.swing.JComponent;

public class DefaultAction {

    public static DefaultAction install(JComponent component, Action action) {
        return new DefaultAction(component, action);
    }
    
    private final KeyBindings keyBindings;
    private final DoubleClick doubleClick;
    
    private DefaultAction(JComponent component, Action action) {
        keyBindings = KeyBindings.whenFocused(component)
                .add(KeyStrokes.ENTER, "gunga-default-action-key", action);
        doubleClick = DoubleClick.on(component).toRunAction(action);
    }

    public void dispose() {
        keyBindings.dispose();
        doubleClick.dispose();
    }
}
