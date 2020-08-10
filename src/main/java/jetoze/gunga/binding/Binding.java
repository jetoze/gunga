package jetoze.gunga.binding;

import static java.util.Objects.*;

import java.util.function.Consumer;

import jetoze.attribut.Property;

public interface Binding {

    // TODO: Is there a way we can make this a class rather than an interface?
    
    void syncUi();
    
    void syncModel();
    
    void setUiToModelEnabled(boolean enabled);
    
    void setModelToUiEnabled(boolean enabled);
    
    void dispose();
    
    
    public static <T> Binding oneWayBinding(Property<T> property, Consumer<? super T> ui) {
        requireNonNull(property);
        requireNonNull(ui);
        AbstractBinding<T> binding = new AbstractBinding<>(property) {

            @Override
            protected void updateUi(T value) {
                ui.accept(value);
            }

            @Override
            protected void removeUiListener() {
                // not applicable
            }
            
        };
        binding.syncUi();
        return binding;
    }
}
