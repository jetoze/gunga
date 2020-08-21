package jetoze.gunga.binding;

import static java.util.Objects.*;

import java.util.function.Consumer;

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
    
    public static Consumer<Boolean> selectableListener(AbstractBinding<?> b) {
        requireNonNull(b);
        return e -> {
            if (b.isUiToModelEnabled()) {
                b.syncModel();
            }
        };
    }
    
    private UiListeners() {/**/}

}
