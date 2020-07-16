package jetoze.gunga.widget;

import javax.swing.text.Document;

public interface TextWidget extends Widget {

    String getText();
    
    void setText(String text);
    
    Document getDocument();
    
}
