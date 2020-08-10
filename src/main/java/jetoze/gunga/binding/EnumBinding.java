package jetoze.gunga.binding;

import static com.google.common.base.Preconditions.checkState;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import jetoze.attribut.Property;
import jetoze.gunga.widget.Selectable;
import jetoze.gunga.widget.ToggleButtonWidget;

public class EnumBinding<E extends Enum<E>> extends AbstractBinding<E> {

    public static <E extends Enum<E>> EnumBinding<E> bind(Property<E> property, Map<E, ToggleButtonWidget> selectors) {
        ToggleButtonWidget.makeExclusive(selectors.values());
        return new EnumBinding<>(property, selectors);
    }

    public static <E extends Enum<E>> EnumBinding<E> bindAndSyncUi(Property<E> property, Map<E, ToggleButtonWidget> selectors) {
        EnumBinding<E> binding = bind(property, selectors);
        binding.syncUi();
        return binding;
    }
    
    private final ImmutableMap<E, ? extends Selectable> selectors;
    private final Consumer<Boolean> uiListener;
    
    public EnumBinding(Property<E> property, Map<E, ? extends Selectable> selectors) {
        super(property);
        this.selectors = Maps.immutableEnumMap(selectors);
        this.uiListener = v -> {
            // XXX: This is necessary because the listener can be notified twice for each
            // selection: when the current selector is unselected, immediately followed
            // by the new selector being selected. The current listener functionality supported
            // by Selectable is too primitive to tell us which is which.
            if (selectors.values().stream().anyMatch(Selectable::isSelected)) {
                if (isUiToModelEnabled()) {
                    syncModel();
                }
            }
        };
        this.selectors.values().forEach(s -> s.addChangeListener(uiListener));
    }

    @Override
    protected void updateUi(E value) {
        Selectable selector = selectors.get(value);
        checkState(selector != null, "No selector provided for value " + value);
        selector.setSelected(true);
        selectors.values().forEach(s -> {
            if (s != selector) {
                s.setSelected(false);
            }
        });
        
    }

    @Override
    protected E getValueFromUi() {
        return selectors.entrySet().stream()
                .filter(e -> e.getValue().isSelected())
                .map(Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No selector is selected"));
    }

    @Override
    protected void removeUiListener() {
        this.selectors.values().forEach(s -> s.removeChangeListener(uiListener));
    }

}
