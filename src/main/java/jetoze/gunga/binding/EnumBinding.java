package jetoze.gunga.binding;

import static com.google.common.base.Preconditions.checkState;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import jetoze.attribut.Property;
import jetoze.gunga.widget.Selectable;

/**
 * Binding between an enum property and a group of Selectables associated with the
 * different enum values. Exactly one Selectable must be provided per supported
 * enum value. It is assumed that the group of Selectables is <em> exclusive</em>,
 * meaning only one element in the group can be selected at any given time.
 * <p>
 * This is a two-way binding. Selecting one of the Selectables updates the model
 * with the corresponding value, and vice versa. 
 * <p>
 * Example of use: Binding a model enum property to a group of radio buttons.
 *
 * @param <E> the type of the enum values
 */
public class EnumBinding<E extends Enum<E>> extends AbstractBinding<E> {
    
    /**
     * Installs a binding between the given property and a group of Selectables, and
     * syncs the UI by making selecting the Selectable assoicated with the current
     * value of the property.
     * 
     * @param property  the enum property
     * @param selectors a Map where the key is an enum constant and the value is the
     *                  Selectable associated with that enum constant.
     * @return the resulting Binding.
     */
    public static <E extends Enum<E>> EnumBinding<E> bind(Property<E> property, Map<E, ? extends Selectable> selectors) {
        EnumBinding<E> binding = new EnumBinding<>(property, selectors);
        binding.syncUi();
        return binding;
    }
    
    private final ImmutableMap<E, ? extends Selectable> selectors;
    private final Consumer<Boolean> uiListener;
    
    public EnumBinding(Property<E> property, Map<E, ? extends Selectable> selectors) {
        super(property);
        this.selectors = Maps.immutableEnumMap(selectors);
        this.uiListener = selected -> {
            // XXX: We need to check the selected param because the listener can be notified twice 
            // for each selection: when the current selector is unselected, immediately followed
            // by the new selector being selected. The current listener functionality supported
            // by Selectable is too primitive to tell us which is which.
            if (selected) {
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
