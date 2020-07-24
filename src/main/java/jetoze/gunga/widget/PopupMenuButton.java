package jetoze.gunga.widget;

import static java.util.Objects.*;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class PopupMenuButton implements Widget {

    private final JButton button;
    private final JPopupMenu popupMenu = new JPopupMenu();
    
    public PopupMenuButton(String text, List<JMenuItem> menuItems) {
        this.button = new JButton(text);
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
    
    public void setFont(Font font) {
        requireNonNull(font);
        button.setFont(font);
    }
    
    public void setToolTipText(String tooltip) {
        requireNonNull(tooltip);
        button.setToolTipText(tooltip);
    }
}
