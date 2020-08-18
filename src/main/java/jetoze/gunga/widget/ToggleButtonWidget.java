package jetoze.gunga.widget;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.awt.Font;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JToggleButton;

public class ToggleButtonWidget implements Widget, Selectable, Customizable {

    private final JToggleButton button;
    private final SelectableButtonSupport selectionSupport;
    
    public ToggleButtonWidget() {
        button = new JToggleButton();
        selectionSupport = new SelectableButtonSupport(button);
    }
    
    public ToggleButtonWidget(Icon icon) {
        button = new JToggleButton(requireNonNull(icon));
        selectionSupport = new SelectableButtonSupport(button);
    }
    
    public ToggleButtonWidget(Icon icon, boolean selected) {
        button = new JToggleButton(requireNonNull(icon), selected);
        selectionSupport = new SelectableButtonSupport(button);
    }
    
    public ToggleButtonWidget(Action action) {
        button = new JToggleButton(requireNonNull(action));
        selectionSupport = new SelectableButtonSupport(button);
    }
    
    public ToggleButtonWidget(String text) {
        button = new JToggleButton(requireNonNull(text));
        selectionSupport = new SelectableButtonSupport(button);
    }
    
    public ToggleButtonWidget(String text, boolean selected) {
        button = new JToggleButton(requireNonNull(text), selected);
        selectionSupport = new SelectableButtonSupport(button);
    }
    
    public ToggleButtonWidget(String text, Icon icon, boolean selected) {
        button = new JToggleButton(requireNonNull(text), requireNonNull(icon), selected);
        selectionSupport = new SelectableButtonSupport(button);
    }

    @Override
    public JComponent getUi() {
        return button;
    }

    @Override
    public void requestFocus() {
        button.requestFocusInWindow();
    }

    @Override
    public Font getFont() {
        return button.getFont();
    }

    @Override
    public void setFont(Font font) {
        button.setFont(requireNonNull(font));
    }

    @Override
    public boolean isSelected() {
        return button.isSelected();
    }

    @Override
    public void setSelected(boolean selected) {
        button.setSelected(selected);
    }

    @Override
    public void addChangeListener(Consumer<Boolean> listener) {
        selectionSupport.addListener(listener);
    }

    @Override
    public void removeChangeListener(Consumer<Boolean> listener) {
        selectionSupport.removeListener(listener);
    }
    
    public void setEnabled(boolean enabled) {
        button.setEnabled(enabled);
    }

    public static ButtonGroup makeExclusive(ToggleButtonWidget... buttons) {
        return makeExclusive(Arrays.asList(buttons));
    }
    
    public static ButtonGroup makeExclusive(Collection<ToggleButtonWidget> buttons) {
        checkArgument(buttons.size() >= 2, "Must provide at least two buttons (got %s)", buttons.size());
        ButtonGroup group = new ButtonGroup();
        buttons.forEach(b -> group.add(b.button));
        return group;
    }
    
}
