package jetoze.gunga;

import static java.util.Objects.*;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public final class KeyBindings {
    
    private final InputMap inputMap;
    private final ActionMap actionMap;
    private final List<KeyBinding> installedKeyBindings = new ArrayList<>();
    private final List<OriginalKeyBinding> originalKeyBindings = new ArrayList<>();

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
        Object originalActionMapKey = inputMap.get(keyBinding.getKeyStroke());
        Action originalAction = actionMap.get(originalActionMapKey);
        originalKeyBindings.add(new OriginalKeyBinding(
                keyBinding.getKeyStroke(), originalActionMapKey, originalAction));
        keyBinding.install(inputMap, actionMap);
        installedKeyBindings.add(keyBinding);
        return this;
    }
    
    public void dispose() {
        installedKeyBindings.forEach(kb -> kb.dispose(inputMap, actionMap));
        installedKeyBindings.clear();
        originalKeyBindings.forEach(OriginalKeyBinding::restore);
        originalKeyBindings.clear();
    }
    
    
    private class OriginalKeyBinding {
        private KeyStroke keyStroke;
        @Nullable
        private Object actionMapKey;
        @Nullable
        private Action action;
        
        public OriginalKeyBinding(KeyStroke keyStroke, @Nullable Object actionMapKey, @Nullable Action action) {
            this.keyStroke = requireNonNull(keyStroke);
            this.actionMapKey = actionMapKey;
            this.action = action;
        }
        
        public void restore() {
            inputMap.put(keyStroke, actionMapKey);
            actionMap.put(actionMapKey, action);
        }
    }
    
}
