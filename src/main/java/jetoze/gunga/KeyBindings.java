package jetoze.gunga;

import static java.util.Objects.*;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public final class KeyBindings {
    // TODO: dispose() method. Keep track of existing bindings that we are replacing,
    // and restore them as part of dispose().
    
    private final InputMap inputMap;
    private final ActionMap actionMap;

    public static KeyBindings whenAncestorOfFocusedComponent(JComponent component) {
        return of(component, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    
    public static KeyBindings whenInFocusedWindow(JComponent component) {
        return of(component, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
    
    public static KeyBindings of(JComponent component) {
        return whenFocused(component);
    }
    
    public static KeyBindings whenFocused(JComponent component) {
        return of(component, JComponent.WHEN_FOCUSED);
    }
    
    public static KeyBindings of(JComponent component, int condition) {
        return new KeyBindings(component.getInputMap(condition), component.getActionMap());
    }
    
    public KeyBindings(InputMap inputMap, ActionMap actionMap) {
        this.inputMap = requireNonNull(inputMap);
        this.actionMap = requireNonNull(actionMap);
    }
    
    public KeyBindings add(KeyStroke keyStroke, String actionMapKey, Action action) {
        return add(new KeyBinding(keyStroke, actionMapKey, action));
    }
    
    public KeyBindings add(KeyStroke keyStroke, String actionMapKey, Runnable action) {
        return add(new KeyBinding(keyStroke, actionMapKey, action));
    }
    
    public KeyBindings add(KeyBinding keyBinding) {
        keyBinding.install(inputMap, actionMap);
        return this;
    }
    
}
