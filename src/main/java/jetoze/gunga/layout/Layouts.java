package jetoze.gunga.layout;

import static java.util.Objects.requireNonNull;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import jetoze.gunga.widget.Widget;

public final class Layouts {

    public static BorderLayoutBuilder border() {
        return new BorderLayoutBuilder();
    }

    public static BorderLayoutBuilder border(int hgap, int vgap) {
        return new BorderLayoutBuilder(hgap, vgap);
    }
    
    public static GridLayoutBuilder grid(int rows, int cols) {
        return new GridLayoutBuilder(rows, cols);
    }
    
    public static GridLayoutBuilder oneColumnGrid() {
        return new GridLayoutBuilder(0, 1);
    }
    
    public static GridLayoutBuilder twoColumnGrid() {
        return new GridLayoutBuilder(0, 2);
    }
    
    public static GridLayoutBuilder oneRowGrid() {
        return new GridLayoutBuilder(1, 0);
    }
    
    public static GridLayoutBuilder twoRowGrid() {
        return new GridLayoutBuilder(2, 0);
    }
    
    public static FormLayoutBuilder form() {
        return new FormLayoutBuilder();
    }
    
    static JComponent toComponent(Object o) {
        requireNonNull(o);
        if (o instanceof JComponent) {
            return (JComponent) o;
        } else if (o instanceof Widget) {
            return ((Widget) o).getUi();
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
