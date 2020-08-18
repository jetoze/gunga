package jetoze.gunga.binding;

import static java.util.Objects.*;

import javax.swing.event.ChangeListener;

public class UiListeners {

    public static ChangeListener changeListener(AbstractBinding<?> b) {
        requireNonNull(b);
        return e -> {
            if (b.isUiToModelEnabled()) {
                b.syncModel();
            }
        };
    }
    
    private UiListeners() {/**/}

}
