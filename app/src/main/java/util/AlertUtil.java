package util;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

/**
 * Kelas utilitas untuk menampilkan dialog notifikasi (Alert) yang seragam
 * di seluruh aplikasi.
 */
public class AlertUtil {

    /**
     * Menampilkan dialog informasi (Information Alert) dengan gaya (style) kustom.
     * @param message Pesan yang akan ditampilkan di dalam dialog.
     */
    public static void showInfo(String message) {
        // Membuat alert standar JavaFX.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null); // Menghilangkan teks header default.
        alert.setContentText(message);

        // Menerapkan file CSS kustom ke panel dialog.
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
            AlertUtil.class.getResource("/css/style.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");
        
        // Menampilkan dialog dan menunggu sampai pengguna menutupnya.
        alert.showAndWait();
    }
}