package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.AuthService;
import util.AlertUtil;

public class RegisterController {
    @FXML private TextField fullnameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> genderBox;
    @FXML private TextField addressField;
    @FXML private ComboBox<String> roleBox;

    private final AuthService authService = new AuthService();

    /**
     * Method yang dieksekusi secara otomatis saat FXML selesai dimuat.
     * Mengisi item pilihan awal untuk ComboBox jenis kelamin dan peran.
     */
    @FXML
    public void initialize() {
        genderBox.getItems().addAll("Laki-laki", "Perempuan");
        roleBox.getItems().addAll("buyer", "seller");
    }

    /**
     * Menangani aksi saat tombol registrasi ditekan.
     * Mengumpulkan semua data dari form, memanggil service untuk mendaftarkan
     * pengguna baru, lalu menampilkan notifikasi dan kembali ke halaman login.
     */
    @FXML
    private void handleRegister() {
        authService.register(
            fullnameField.getText(),
            emailField.getText(),
            phoneField.getText(),
            passwordField.getText(),
            genderBox.getValue(),
            addressField.getText(),
            roleBox.getValue()
        );
        // Tampilkan pesan sukses.
        AlertUtil.showInfo("Registrasi berhasil! Silakan login.");
        // Kembali ke halaman login.
        goToLogin();
    }

    /**
     * Mengarahkan pengguna kembali ke halaman login (login.fxml).
     * Method ini biasanya terhubung dengan tombol atau link "Sudah punya akun? Login".
     */
    @FXML
    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) fullnameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("BEUTRA Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}