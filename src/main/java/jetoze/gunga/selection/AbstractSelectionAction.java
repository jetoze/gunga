package jetoze.gunga.selection;

import javax.annotation.Nullable;
import javax.swing.AbstractAction;
import javax.swing.Icon;

public abstract class AbstractSelectionAction<T> extends AbstractAction implements SelectionAction<T> {
    
    @Nullable
    private SelectionSource<T> selectionSource;
    private Selection<T> selection = Selections.emptySelection();
    private final SelectionListener<T> selectionListener = this::handleSelection;
    
    public AbstractSelectionAction() {
        handleSelection(Selections.emptySelection());
    }

    public AbstractSelectionAction(String name) {
        super(name);
        handleSelection(Selections.emptySelection());
    }

    public AbstractSelectionAction(String name, Icon icon) {
        super(name, icon);
        handleSelection(Selections.emptySelection());
    }

    @Override
    public final void setSelectionSource(@Nullable SelectionSource<T> source) {
        if (this.selectionSource != null) {
            this.selectionSource.removeSelectionListener(selectionListener);
        }
        this.selectionSource = source;
        if (source != null) {
            source.addSelectionListener(selectionListener);
            handleSelection(source.getSelection());
        } else {
            handleSelection(Selections.emptySelection());
        }
    }
    
    protected abstract void handleSelection(Selection<T> selection);
    
    protected final Selection<T> getSelection() {
        return selection;
    }

}
