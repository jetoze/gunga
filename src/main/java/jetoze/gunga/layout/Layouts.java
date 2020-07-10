package jetoze.gunga.layout;

import static java.util.Objects.requireNonNull;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

public final class Layouts {

    public static BorderLayoutBuilder border() {
        return new BorderLayoutBuilder();
    }

    public static BorderLayoutBuilder border(int hgap, int vgap) {
        return new BorderLayoutBuilder(hgap, vgap);
    }
    
    static JComponent toComponent(Object o) {
        requireNonNull(o);
        if (o instanceof JComponent) {
            return (JComponent) o;
        } else if (o instanceof String) {
            return new JLabel(o.toString());
        } else if (o instanceof Action) {
            return new JButton((Action) o);
        }
        // More special cases here?
        throw new IllegalArgumentException("Cannot add object to layout: " + o);
    }
    
    private Layouts() {/**/}
}
