package controller;

import beautra.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.User;
import service.AuthService;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ListView<?> cartList;
    @FXML private Label cartTotalLabel;

    private final AuthService authService = new AuthService();
    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        User user = authService.login(email, password);

        if (user != null) {
            MainApp.currentUser = user;
            try {
                FXMLLoader loader;
                Stage stage = (Stage) emailField.getScene().getWindow();
                switch (user.getRole()) {
                    case "buyer":
                        loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
                        break;
                    case "seller":
                        loader = new FXMLLoader(getClass().getResource("/fxml/seller_dashboard.fxml"));
                        break;
                    default:
                        return;
                }
                stage.setScene(new Scene(loader.load()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

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

    @FXML
    private void handleForgotPassword() {
        String email = emailField.getText();
        if (email == null || email.isEmpty()) {
            util.AlertUtil.showInfo("Masukkan email Anda terlebih dahulu.");
            return;
        }
    }
}

