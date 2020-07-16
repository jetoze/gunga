package jetoze.gunga.widget;

import static java.util.Objects.requireNonNull;

import java.util.function.Consumer;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DocumentContentChangeListener implements DocumentListener {

    private final Consumer<? super DocumentEvent> handler;
    
    public DocumentContentChangeListener(Consumer<? super DocumentEvent> handler) {
        this.handler = requireNonNull(handler);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        // Does nothing.
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        handler.accept(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        handler.accept(e);
    }

}