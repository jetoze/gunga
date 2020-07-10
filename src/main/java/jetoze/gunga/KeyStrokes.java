package jetoze.gunga;

import java.awt.Toolkit;
import java.awt.event.InputEvent;

import javax.swing.KeyStroke;

public final class KeyStrokes {

    public static KeyStroke forKey(int keyCode) {
        return KeyStroke.getKeyStroke(keyCode, 0);
    }
    
    public static KeyStroke forChar(char c) {
        return KeyStroke.getKeyStroke(c);
    }
    
    public static KeyStroke commandDown(int keyCode) {
        return KeyStroke.getKeyStroke(keyCode, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
    }
    
    public static KeyStroke commandDown(char c) {
        return KeyStroke.getKeyStroke(c, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
    }
    
    public static KeyStroke commandShiftDown(int keyCode) {
        return KeyStroke.getKeyStroke(keyCode, 
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | InputEvent.SHIFT_DOWN_MASK);
    }
    
    public static KeyStroke commandShiftDown(char c) {
        return KeyStroke.getKeyStroke(c, 
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | InputEvent.SHIFT_DOWN_MASK);
    }
    
    private KeyStrokes() {/**/}
    
}
