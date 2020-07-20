package jetoze.gunga;

import static java.util.Objects.requireNonNull;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JComponent;

public class DoubleClick {

    public static DoubleClick on(JComponent component) {
        requireNonNull(component);
        return new DoubleClick(component);
    }
    
    private final JComponent component;
    private final List<Action> actions = new ArrayList<>();
    private final MouseListener mouseListener = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                handleDoubleClick();
            }
        }
    };
    
    public DoubleClick(JComponent component) {
        this.component = component;
        this.component.addMouseListener(mouseListener);
    }
    
    public DoubleClick toRunAction(Action action) {
        requireNonNull(action);
        this.actions.add(action);
        return this;
    }

    private void handleDoubleClick() {
        ActionEvent e = new ActionEvent(component, ActionEvent.ACTION_FIRST, "actionPerformed");
        actions.stream()
            .filter(Action::isEnabled)
            .forEach(a -> a.actionPerformed(e));
    }
    
    public void dispose() {
        component.removeMouseListener(mouseListener);
    }
}
