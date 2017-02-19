package pl.pk.isk;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;

/**
 * Created by michael on 19.02.17.
 */
public final class ControlsFactory {
    public static TextField createTextField() {
        TextField textField = new TextField();
        textField.setMaxWidth(50);
        textField.setAlignment(Pos.CENTER);
        textField.setPadding(new Insets(0,0,5,0));
        return textField;
    }
}
