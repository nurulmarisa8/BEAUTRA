package util;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

public class AlertUtil {

    public static void showInfo(String message) {
        // Membuat alert seperti biasa
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null); // Menghilangkan header default agar lebih bersih
        alert.setContentText(message);

        // ===================================================================
        // PERBAIKAN UTAMA: Menerapkan CSS ke Dialog Alert
        // ===================================================================
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
            AlertUtil.class.getResource("/css/style.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");

        // Mengatur agar window alert memiliki icon aplikasi (jika diinginkan)
        // Anda bisa menambahkan icon di sini jika punya, contoh:
        // stage.getIcons().add(new Image(AlertUtil.class.getResourceAsStream("/images/logo.png")));
        
        alert.showAndWait();
    }
}