package jetoze.gunga.layout;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import static java.awt.BorderLayout.WEST;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

public final class BorderLayoutBuilder {

    private final BorderLayout layout;
    
    BorderLayoutBuilder() {
        this(0, 0);
    }
    
    BorderLayoutBuilder(int hgap, int vgap) {
        this.layout = new BorderLayout(hgap, vgap);
    }
    
    public BorderLayoutBuilder east(Object object) {
        return add(object, EAST);
    }
    
    public BorderLayoutBuilder west(Object object) {
        return add(object, WEST);
    }
    
    public BorderLayoutBuilder north(Object object) {
        return add(object, NORTH);
    }
    
    public BorderLayoutBuilder south(Object object) {
        return add(object, SOUTH);
    }
    
    public BorderLayoutBuilder center(Object object) {
        return add(object, CENTER);
    }
    
    public BorderLayoutBuilder eastToWest(Object east, Object center, Object west) {
        add(east, EAST);
        add(center, CENTER);
        add(west, WEST);
        return this;
    }
    
    public BorderLayoutBuilder northToSouth(Object north, Object center, Object south) {
        add(north, NORTH);
        add(center, CENTER);
        add(south, SOUTH);
        return this;
    }
    
    public BorderLayoutBuilder withHorizontalGap(int hgap) {
        layout.setHgap(hgap);
        return this;
    }
    
    public BorderLayoutBuilder withVerticalGap(int vgap) {
        layout.setVgap(vgap);
        return this;
    }
    
    private BorderLayoutBuilder add(Object object, String where) {
        JComponent component = Layouts.toComponent(object);
        layout.addLayoutComponent(component, where);
        return this;
    }
    
    public JPanel build() {
        return new JPanel(layout);
    }
    
    public JPanel buildIn(JPanel panel) {
        panel.setLayout(layout);
        return panel;
    }
    
}
