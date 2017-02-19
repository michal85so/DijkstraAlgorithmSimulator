package pl.pk.isk;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextField;

public class Validators {
    public static boolean validateInputValues(Integer nodes, Integer connections) {
        if (nodes == null || nodes.intValue() < 2) {
            Dialogs.showError(Dialogs.Text.numberOfNodesLessThanTwo);
            return true;
        }
        if (connections == null || connections < 1 ||  connections >= nodes) {
            Dialogs.showError(Dialogs.Text.wrongNumberOfConnections);
            return true;
        }
        return false;
    }

    public static ChangeListener<String> createStringValidatorChangeListener(TextField textField) {
        return (observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                textField.setText(newValue.replaceAll("[^\\d]", ""));
        };
    }
}
