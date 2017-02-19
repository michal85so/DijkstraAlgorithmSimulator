package pl.pk.isk;

import javafx.scene.control.Alert;

public class Dialogs {
    public enum Text{
        numberOfNodesLessThanTwo("Walidacja!", "Liczba wierzchłków musi być większa niż 1!"),
        wrongNumberOfConnections("Walidacja!", "Nieprawidłowa liczba połączeń!");

        private String type;
        private String text;

        private Text(String type, String text) {
            this.type = type;
            this.text = text;
        }
    }

    public static void showError(Text text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(text.type);
        alert.setHeaderText(null);
        alert.setContentText(text.text);

        alert.showAndWait();
    }
}
