package mapgenerator.ui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class IntegerField extends TextField {
    final private IntegerProperty arvo;
    
    public IntegerField(int oletusarvo) {
        this.arvo = new SimpleIntegerProperty(oletusarvo);
        setText(oletusarvo + "");

        final IntegerField intField = this;

        arvo.addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null) {
                intField.setText("");
            } else {
                if (!(newValue.intValue() == 0 && (textProperty().get() == null)) && !("".equals(textProperty().get()))) {
                    intField.setText(newValue.toString());
                }
            }
        });
        
        this.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null || "".equals(newValue)) {
                arvo.setValue(0);
                return;
            }
            arvo.set(Integer.parseInt(textProperty().get()));
        });
        
        this.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (!"0123456789".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
            }
        });
    }
    
    public int getArvo() {
        return this.arvo.getValue();
    }
}
