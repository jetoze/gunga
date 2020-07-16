package jetoze.gunga.binding;

import static java.util.Objects.requireNonNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import jetoze.attribut.Property;
import jetoze.gunga.UiThread;

public abstract class AbstractBinding<T> implements Binding {
    
    private final Property<T> property;
    private final ModelListener modelListener = new ModelListener();
    private boolean uiToModelEnabled;
    
    protected AbstractBinding(Property<T> property) {
        this.property = requireNonNull(property);
        this.property.addListener(modelListener);
    }
    
    @Override
    public final void syncUi() {
        T value = property.get();
        setUiToModelEnabled(false);
        try {
            updateUi(value);
        } finally {
            setUiToModelEnabled(true);
        }
    }
    
    protected abstract void updateUi(T value);

    @Override
    public final void syncModel() {
        T value = getValueFromUi();
        setModelToUiEnabled(false);
        try {
            property.set(value);
        } finally {
            setModelToUiEnabled(true);
        }
    }
    
    protected T getValueFromUi() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void setUiToModelEnabled(boolean enabled) {
        this.uiToModelEnabled = enabled;
    }

    public final  boolean isUiToModelEnabled() {
        return uiToModelEnabled;
    }

    @Override
    public final void setModelToUiEnabled(boolean enabled) {
        modelListener.setEnabled(enabled);
    }

    public final boolean isModelToUiEnabled() {
        return modelListener.isEnabled();
    }

    @Override
    public final void dispose() {
        property.removeListener(modelListener);
        removeUiListener();
    }
    
    protected abstract void removeUiListener();
    
    
    private class ModelListener implements PropertyChangeListener {

        private boolean enabled = true;
        
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (enabled) {
                // TODO: If the property is changed from a background thread, do we
                // need to block that thread until the UI has been updated?
                UiThread.run(AbstractBinding.this::syncUi);
            }
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public boolean isEnabled() {
            return enabled;
        }
    }
}
