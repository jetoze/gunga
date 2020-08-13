package jetoze.gunga;

import static java.util.Objects.requireNonNull;

import java.awt.event.ActionEvent;

import javax.annotation.Nullable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

/**
 * Utilities around {@link Action}s. 
 */
public class Actions {

    /**
     * Creates an action that runs the given job when invoked.
     */
    public static AbstractAction toAction(Runnable job) {
        requireNonNull(job);
        return createAction(null, null, job);
    }
    
    /**
     * Creates an action with the given name, that runs the given job when invoked.
     */
    public static AbstractAction toAction(String name, Runnable job) {
        requireNonNull(name);
        requireNonNull(job);
        return createAction(name, null, job);
    }

    /**
     * Creates an action with the given name and icon, that runs the given job when invoked.
     */
    public static AbstractAction toAction(String name, Icon icon, Runnable job) {
        requireNonNull(name);
        requireNonNull(job);
        requireNonNull(icon);
        return createAction(name, icon, job);
    }

    /**
     * Creates an action with the given icon, that runs the given job when invoked.
     */
    public static AbstractAction toAction(Icon icon, Runnable job) {
        requireNonNull(icon);
        requireNonNull(job);
        return createAction(null, icon, job);
    }

    private static AbstractAction createAction(@Nullable String name, @Nullable Icon icon, Runnable job) {
        return new AbstractAction(name, icon) {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                // Wrap in an invokeLater for better response. (Learned by experience.)
                UiThread.runLater(job);
            }
        };
    }
    
    private Actions() {/**/}

}
