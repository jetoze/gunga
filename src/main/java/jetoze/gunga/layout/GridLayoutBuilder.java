package jetoze.gunga.layout;

import static com.google.common.base.Preconditions.*;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

public class GridLayoutBuilder extends AbstractLayoutBuilder<GridLayoutBuilder> {

    private final GridLayout layout;
    private final List<JComponent> components = new ArrayList<>();
    
    public GridLayoutBuilder(int rows, int cols) {
        layout = new GridLayout(rows, cols);
    }
    
    public GridLayoutBuilder withHorizontalGap(int hgap) {
        checkArgument(hgap >= 0);
        layout.setHgap(hgap);
        return this;
    }
    
    public GridLayoutBuilder withVerticalGap(int vgap) {
        checkArgument(vgap >= 0);
        layout.setVgap(vgap);
        return this;
    }
    
    public GridLayoutBuilder add(Object o) {
        components.add(Layouts.toComponent(o));
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
    protected GridLayoutBuilder thisBuilder() {
        return this;
    }

}
