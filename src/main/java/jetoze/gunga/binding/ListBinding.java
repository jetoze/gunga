package jetoze.gunga.binding;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import jetoze.attribut.Property;
import jetoze.gunga.widget.ListWidget;
import jetoze.gunga.widget.ListWidgetModel;

public class ListBinding<T> extends AbstractBinding<List<T>> {

    public static <T> ListBinding<T> bind(Property<List<T>> property, ListWidget<T> ui) {
        ListBinding<T> binding = new ListBinding<T>(property, ui);
        binding.syncUi();
        return binding;
    }
    
    private final ListWidget<T> ui;
    
    private ListBinding(Property<List<T>> property, ListWidget<T> ui) {
        super(property);
        this.ui = requireNonNull(ui);
    }

    @Override
    protected void updateUi(List<T> value) {
        ListWidgetModel<T> existingModel = ui.getModel();
        Optional<Predicate<? super T>> existingFilter = existingModel.getFilter();
        ListWidgetModel<T> newModel = new ListWidgetModel<>(value);
        existingFilter.ifPresent(ui::setFilter);
        ui.setModel(newModel);
    }

    @Override
    protected void removeUiListener() {
        // no-op, since we are not listening to the UI
    }
}
