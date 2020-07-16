package jetoze.gunga.binding;

public interface Binding {

    void syncUi();
    
    void syncModel();
    
    void setUiToModelEnabled(boolean enabled);
    
    void setModelToUiEnabled(boolean enabled);
    
    void dispose();
}
