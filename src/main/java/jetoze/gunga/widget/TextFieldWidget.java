package jetoze.gunga.widget;

import static java.util.Objects.*;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class TextFieldWidget implements TextWidget {

    private final JTextField textField;
    private FocusListener selectAllWhenFocusedListener;
    private DocumentListener binding;
    
    public TextFieldWidget() {
        this(null, 0);
    }
    
    public TextFieldWidget(String text) {
        this(requireNonNull(text), 0);
    }
    
    public TextFieldWidget(int columns) {
        this(null, columns);
    }
    
    public TextFieldWidget(String text, int columns) {
        textField = new JTextField(requireNonNull(text), columns);        
    }
    
    @Override
    public JComponent getUi() {
        return textField;
    }
    
    @Override
    public String getText() {
        return textField.getText();
    }
    
    @Override
    public void setText(String text) {
        textField.setText(requireNonNull(text));
    }

    @Override
    public void requestFocus() {
        textField.requestFocusInWindow();
    }
    
    public void selectAllWhenFocused() {
        // TODO: Does this need to be a property that can be turned on and off?
        if (selectAllWhenFocusedListener == null) {
            selectAllWhenFocusedListener = new FocusAdapter() {

                @Override
                public void focusGained(FocusEvent e) {
                    textField.selectAll();
                }
            };
            textField.addFocusListener(selectAllWhenFocusedListener);
        }
    }

    @Override
    public Document getDocument() {
        return textField.getDocument();
    }
    
}