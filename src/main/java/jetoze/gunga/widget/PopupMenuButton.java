package jetoze.gunga.widget;

import static com.google.common.base.Preconditions.*;
import static java.util.Objects.requireNonNull;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class PopupMenuButton implements Widget, Customizable {

    private final JButton button;
    private final JPopupMenu popupMenu = new JPopupMenu();
    
    public PopupMenuButton(String text, JMenuItem... menuItems) {
        this(text, Arrays.asList(menuItems));
    }
    
    public PopupMenuButton(String text, List<JMenuItem> menuItems) {
        this.button = new JButton(text);
        checkArgument(!menuItems.isEmpty(), "Must provide at least one menu item");
        menuItems.forEach(popupMenu::add);
        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }

    @Override
    public JComponent getUi() {
        return button;
    }

    @Override
    public void requestFocus() {
        button.requestFocusInWindow();
    }
    
    @Override
    public Font getFont() {
        return button.getFont();
    }
    
    @Override
    public void setFont(Font font) {
        requireNonNull(font);
        button.setFont(font);
    }
    
    public void setToolTipText(String tooltip) {
        requireNonNull(tooltip);
        button.setToolTipText(tooltip);
    }
}
