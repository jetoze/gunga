package jetoze.gunga.binding;

import static java.util.Objects.requireNonNull;

import java.util.function.Consumer;

import jetoze.attribut.Property;
import jetoze.gunga.widget.CheckBoxWidget;

public class BooleanBinding extends AbstractBinding<Boolean> {

    public static BooleanBinding bind(Property<Boolean> property, CheckBoxWidget ui) {
        return new BooleanBinding(property, ui);
    }

    public static BooleanBinding bindAndSyncUi(Property<Boolean> property, CheckBoxWidget ui) {
        BooleanBinding binding = new BooleanBinding(property, ui);
        binding.syncUi();
        return binding;
    }
    
    private final CheckBoxWidget ui;
    
    private final Consumer<Boolean> uiListener;
    
    private BooleanBinding(Property<Boolean> property, CheckBoxWidget ui) {
        super(property);
        this.ui = requireNonNull(ui);
        // XXX: It feels a bit funky to have the new value here, but then letting
        // the base-class add for the value again from syncModel().
        this.uiListener = b -> syncModel();
        ui.addSelectionListener(uiListener);
    }
    
    @Override
    protected void updateUi(Boolean value) {
        ui.setSelected(value);
    }

    @Override
    protected Boolean getValueFromUi() {
        return ui.isSelected();
    }

    @Override
    protected void removeUiListener() {
        ui.removeSelectionListener(uiListener);
    }

}
