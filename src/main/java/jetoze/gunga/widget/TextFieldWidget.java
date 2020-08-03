package jetoze.gunga.widget;

import static java.util.Objects.requireNonNull;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.Document;

public class TextFieldWidget implements TextWidget, Customizable {

    private static final Validator NO_VALIDATION = new Validator() {

        @Override
        public boolean isRequired() {
            return false;
        }

        @Override
        public boolean isValid(String text) {
            return true;
        }
    };
    
    private final JTextField textField;
    private FocusListener selectAllWhenFocusedListener;
    private Validator validator = NO_VALIDATION;
    
    public TextFieldWidget() {
        this("", 0);
    }
    
    public TextFieldWidget(String text) {
        this(requireNonNull(text), 0);
    }
    
    public TextFieldWidget(int columns) {
        this("", columns);
    }
    
    public TextFieldWidget(String text, int columns) {
        textField = new JTextFieldImpl(requireNonNull(text), columns);
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
    
    public void setValidator(Validator validator) {
        requireNonNull(validator);
        if (validator != this.validator) {
            this.validator = validator;
            textField.repaint();
        }
    }

    @Override
    public Document getDocument() {
        return textField.getDocument();
    }

    @Override
    public Font getFont() {
        return textField.getFont();
    }

    @Override
    public void setFont(Font font) {
        textField.setFont(requireNonNull(font));
    }
    
    
    public static interface Validator {
        // TODO: Just include isValid() here, and make required a property of TextFieldWidget?
        boolean isRequired();
        boolean isValid(String text);
    }
    
    
    private class JTextFieldImpl extends JTextField {

        public JTextFieldImpl(String text, int columns) {
            super(text, columns);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if ((getDocument().getLength() == 0) && validator.isRequired()) {
                renderRequiredMarker((Graphics2D) g);
            }
        }
        
        private void renderRequiredMarker(Graphics2D g2) {
            Stroke originalStroke = g2.getStroke();
            Color originalColor = g2.getColor();
            g2.setStroke(new BasicStroke(4.f));
            g2.setColor(Color.RED);
            int x = getWidth() - 4;
            g2.drawLine(x, getInsets().top, x, getHeight() - getInsets().bottom);
            g2.setColor(originalColor);
            g2.setStroke(originalStroke);
        }
    }
}
