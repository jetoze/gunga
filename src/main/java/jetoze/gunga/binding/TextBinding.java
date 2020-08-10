package jetoze.gunga.binding;

import static java.util.Objects.requireNonNull;

import javax.swing.event.DocumentListener;

import jetoze.attribut.Property;
import jetoze.gunga.widget.DocumentContentChangeListener;
import jetoze.gunga.widget.TextWidget;

public class TextBinding extends AbstractBinding<String> {
    
    public static TextBinding bind(Property<String> property, TextWidget widget) {
        TextBinding b = new TextBinding(property, widget);
        b.syncUi();
        return b;
    }
    
    private final TextWidget widget;
    private final DocumentListener uiListener;
    
    private TextBinding(Property<String> property, TextWidget widget) {
        super(property);
        this.widget = requireNonNull(widget);
        this.uiListener = new DocumentContentChangeListener(e -> {
            if (isUiToModelEnabled()) {
                syncModel();
            }
        });
        widget.getDocument().addDocumentListener(uiListener);
    }

    @Override
    protected void updateUi(String value) {
        widget.setText(value);
    }

    @Override
    protected String getValueFromUi() {
        return widget.getText();
    }

    @Override
    protected void removeUiListener() {
        widget.getDocument().removeDocumentListener(uiListener);
    }

}
