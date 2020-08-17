package jetoze.gunga;

import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

public final class KeyStrokes {

    public static final KeyStroke ENTER = KeyStrokes.forKey(KeyEvent.VK_ENTER);
    public static final KeyStroke ESCAPE = KeyStrokes.forKey(KeyEvent.VK_ESCAPE);
    
    public static KeyStroke forKey(int keyCode) {
        return KeyStroke.getKeyStroke(keyCode, 0);
    }
    
    public static KeyStroke forChar(char c) {
        return KeyStroke.getKeyStroke(c);
    }
    
    public static KeyStroke ctrlDown(int keyCode) {
        return KeyStroke.getKeyStroke(keyCode, InputEvent.CTRL_DOWN_MASK);
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
