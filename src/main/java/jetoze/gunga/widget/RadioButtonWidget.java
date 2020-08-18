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
import javax.swing.JRadioButton;

public class RadioButtonWidget implements Widget, Selectable, Customizable {

    private final JRadioButton radioButton;
    private final SelectableButtonSupport selectionSupport;

    public RadioButtonWidget() {
        radioButton = new JRadioButton();
        selectionSupport = new SelectableButtonSupport(radioButton);
    }
    
    public RadioButtonWidget(Icon icon) {
        this(icon, false);
    }
    
    public RadioButtonWidget(Icon icon, boolean selected) {
        radioButton = new JRadioButton(requireNonNull(icon), selected);
        selectionSupport = new SelectableButtonSupport(radioButton);
    }
    
    public RadioButtonWidget(Action action) {
        radioButton = new JRadioButton(requireNonNull(action));
        selectionSupport = new SelectableButtonSupport(radioButton);
    }

    public RadioButtonWidget(String text) {
        this(text, false);
    }
    
    public RadioButtonWidget(String text, boolean selected) {
        radioButton = new JRadioButton(requireNonNull(text), selected);
        selectionSupport = new SelectableButtonSupport(radioButton);
    }
    
    public RadioButtonWidget(String text, Icon icon) {
        this(text, icon, false);
    }
    
    public RadioButtonWidget(String text, Icon icon, boolean selected) {
        radioButton = new JRadioButton(requireNonNull(text), requireNonNull(icon), selected);
        selectionSupport = new SelectableButtonSupport(radioButton);
    }
    
    @Override
    public boolean isSelected() {
        return radioButton.isSelected();
    }

    @Override
    public void setSelected(boolean selected) {
        radioButton.setSelected(selected);
    }

    @Override
    public void addChangeListener(Consumer<Boolean> listener) {
        selectionSupport.addListener(listener);
    }

    @Override
    public void removeChangeListener(Consumer<Boolean> listener) {
        selectionSupport.removeListener(listener);
    }

    @Override
    public JComponent getUi() {
        return radioButton;
    }

    @Override
    public void requestFocus() {
        radioButton.requestFocusInWindow();
    }

    @Override
    public Font getFont() {
        return radioButton.getFont();
    }

    @Override
    public void setFont(Font font) {
        radioButton.setFont(font);
    }
    
    public void setEnabled(boolean enabled) {
        radioButton.setEnabled(enabled);
    }

    public static ButtonGroup makeExclusive(RadioButtonWidget... buttons) {
        return makeExclusive(Arrays.asList(buttons));
    }
    
    public static ButtonGroup makeExclusive(Collection<RadioButtonWidget> buttons) {
        checkArgument(buttons.size() >= 2, "Must provide at least two buttons (got %s)", buttons.size());
        ButtonGroup group = new ButtonGroup();
        buttons.forEach(b -> group.add(b.radioButton));
        return group;
    }

}
