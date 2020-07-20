package jetoze.gunga;

import static java.util.Objects.*;
import static tzeth.preconds.MorePreconditions.*;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;

public final class KeyBinding {
    private final KeyStroke keyStroke;
    private final String actionMapKey;
    private final Action action;

    public KeyBinding(KeyStroke keyStroke, String actionMapKey, Action action) {
        this.keyStroke = requireNonNull(keyStroke);
        this.actionMapKey = checkNotBlank(actionMapKey, "actionMapKey cannot be null or blank");
        this.action = requireNonNull(action);
    }

    public KeyBinding(KeyStroke keyStroke, String actionMapKey, Runnable action) {
        this(keyStroke, actionMapKey, toAction(action));
    }
    
    private static Action toAction(Runnable action) {
        requireNonNull(action);
        return new AbstractAction() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        };
    }
    
    public void install(InputMap inputMap, ActionMap actionMap) {
        inputMap.put(keyStroke, actionMapKey);
        actionMap.put(actionMapKey, action);
    }
    
    public void dispose(InputMap inputMap, ActionMap actionMap) {
        inputMap.remove(getKeyStroke());
        actionMap.remove(actionMapKey);
    }
    
    public KeyStroke getKeyStroke() {
        return keyStroke;
    }
    
    public Object getActionMapKey() {
        return actionMapKey;
    }
}
