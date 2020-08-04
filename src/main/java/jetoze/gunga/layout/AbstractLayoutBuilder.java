package jetoze.gunga.layout;

import static java.util.Objects.*;

import java.awt.Container;
import java.awt.LayoutManager;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import javax.swing.border.Border;

public abstract class AbstractLayoutBuilder<B extends AbstractLayoutBuilder<B>> {

    private Border border;
    
    protected abstract LayoutManager getLayout();

    public final JPanel build() {
        JPanel panel = new JPanel(getLayout());
        addComponents(panel);
        return panel;
    }
    
    public final <T extends Container> T buildIn(T container) {
        container.removeAll();
        container.invalidate();
        container.setLayout(getLayout());
        addComponents(container);
        if ((border != null) && container instanceof JComponent) {
            ((JComponent) container).setBorder(border);
        }
        return container;
    }
    
    public final void buildAsContent(RootPaneContainer rootPaneContainer) {
        buildIn(rootPaneContainer.getContentPane());
    }    

    protected abstract <T extends Container> void addComponents(T container);

    public final B withBorder(Border border) {
        this.border = requireNonNull(border);
        return thisBuilder();
    }
    
    protected abstract B thisBuilder();

}
