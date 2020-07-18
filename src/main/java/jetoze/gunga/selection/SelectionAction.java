package jetoze.gunga.selection;

import static java.util.Objects.*;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;

import javax.annotation.Nullable;
import javax.swing.Action;

import jetoze.gunga.UiThread;

public interface SelectionAction<T> extends Action {

    void setSelectionSource(SelectionSource<T> source);

    public static <T> SelectionAction<T> forSingleItem(String name, Consumer<? super T> handler) {
        requireNonNull(handler);
        return new AbstractSelectionAction<T>(name) {

            @Nullable
            private T selectedItem;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                T item = selectedItem;
                if (item != null) {
                    UiThread.acceptLater(handler, item);
                }
            }

            @Override
            protected void handleSelection(Selection<T> selection) {
                selectedItem = (selection.getItems().size() == 1)
                        ? selection.getItems().get(0)
                        : null;
                setEnabled(selectedItem != null);
            }
        };
    }
    
}
