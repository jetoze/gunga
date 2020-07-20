package jetoze.gunga;

import static com.google.common.base.Preconditions.*;
import static java.util.Objects.requireNonNull;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.annotation.Nullable;
import javax.swing.Action;
import javax.swing.JComponent;

public class DoubleClick {

    public static DoubleClick on(JComponent component) {
        requireNonNull(component);
        return new DoubleClick(component);
    }
    
    private final JComponent component;
    @Nullable
    private Action action;
    @Nullable
    private Runnable job;
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
        checkState(this.action == null, "An Action has already been provided");
        this.action = action;
        return this;
    }
    
    public DoubleClick toRun(Runnable job) {
        requireNonNull(job);
        checkState(this.job == null, "A Runnable has already been provided");
        this.job = job;
        return this;
    }

    private void handleDoubleClick() {
        if (action != null && action.isEnabled()) {
            ActionEvent e = new ActionEvent(component, ActionEvent.ACTION_FIRST, "actionPerformed");
            action.actionPerformed(e);
        }
        if (job != null) {
            job.run();
        }
    }
    
    public void dispose() {
        component.removeMouseListener(mouseListener);
        action = null;
        job = null;
    }
}
