package jetoze.gunga.layout;

import static java.util.Objects.requireNonNull;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;

public class FormLayoutBuilder extends AbstractLayoutBuilder<FormLayoutBuilder> {

    // TODO: Add more options:
    //          + Bold labels
    //          + Non-JLabel labels
    //          + Right-aligned labels
    //          + Two columns of Label-Component pairs.
    
    private final GridBagLayout layout = new GridBagLayout();
    private final Map<Component, GridBagConstraints> components = new HashMap<>();
    private int rowCount;
    private Insets insets = new Insets(0, 0, 5, 5);

    public FormLayoutBuilder addRow(String label, Object object) {
        requireNonNull(label);
        requireNonNull(object);

        GridBagConstraints left = newConstraints();
        left.gridx = 0;
        components.put(new JLabel(label), left);

        GridBagConstraints right = newConstraints();
        right.gridx = 1;
        right.weightx = 1.0;
        components.put(Layouts.toComponent(object), right);
        
        ++rowCount;
        return this;
    }
    
    private GridBagConstraints newConstraints() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = rowCount;
        c.insets = insets;
        c.fill = GridBagConstraints.HORIZONTAL;
        return c;
    }
    
    public FormLayoutBuilder withInsets(Insets insets) {
        this.insets = requireNonNull(insets);
        return this;
    }

    @Override
    protected LayoutManager getLayout() {
        return layout;
    }

    @Override
    protected <T extends Container> void addComponents(T container) {
        components.forEach(container::add);
    }

    @Override
    protected FormLayoutBuilder thisBuilder() {
        return this;
    }

}
