package jetoze.gunga.widget;

import javax.swing.JComponent;

public interface Widget {

    JComponent getUi();
    
    void requestFocus();
    
}
