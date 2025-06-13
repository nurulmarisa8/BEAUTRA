package controller;

import beautra.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import service.AuthService;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;


    private final AuthService authService = new AuthService();

    /**
     * Menangani aksi saat tombol login ditekan.
     * Method ini mengambil email dan password dari input, memvalidasinya melalui
     * AuthService, dan jika berhasil, akan mengarahkan pengguna ke halaman yang
     * sesuai dengan perannya (buyer atau seller).
     */
    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        User user = authService.login(email, password);

        // Jika user ditemukan (login berhasil)
        if (user != null) {
            MainApp.currentUser = user; // Simpan data user yang login secara global
            try {
                FXMLLoader loader;
                Stage stage = (Stage) emailField.getScene().getWindow();
                // Arahkan ke halaman berdasarkan peran (role)
                switch (user.getRole()) {
                    case "buyer":
                        loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
                        break;
                    case "seller":
                        loader = new FXMLLoader(getClass().getResource("/fxml/seller_dashboard.fxml"));
                        break;
                    default:
                        return; // Jika peran tidak dikenali, jangan lakukan apa-apa
                }
                stage.setScene(new Scene(loader.load()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Jika login gagal (user null), idealnya ada notifikasi error untuk pengguna,
        // namun saat ini tidak ada aksi yang dilakukan.
    }

    /**
     * Mengarahkan pengguna ke halaman registrasi (register.fxml).
     * Method ini dipanggil saat pengguna menekan tombol atau link untuk mendaftar.
     */
    @FXML
    private void goToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Register");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}