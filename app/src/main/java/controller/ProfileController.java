package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.User;

public class ProfileController {
    @FXML private ImageView avatarView;
    @FXML private Label fullnameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label genderLabel;
    @FXML private Label addressLabel;
    @FXML private Label roleLabel;
    @FXML private Button closeButton;

    /**
     * Method yang dieksekusi secara otomatis saat FXML selesai dimuat.
     * Digunakan untuk menginisialisasi listener atau aksi pada komponen UI,
     * seperti menambahkan fungsi pada tombol tutup.
     */
    @FXML
    public void initialize() {
        // Menambahkan aksi untuk tombol tutup.
        closeButton.setOnAction(event -> {
            // Mengambil stage (jendela) dari tombol dan menutupnya.
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
    }

    /**
     * Mengisi data pengguna ke komponen UI di halaman profil.
     * Metode ini dipanggil dari controller lain untuk mengirimkan data user
     * yang sedang login.
     * @param user Objek User yang datanya akan ditampilkan.
     */
    public void setUser(User user) {
        if (user == null) {
            return;
        }

        // Mengisi semua label dengan data dari objek User.
        fullnameLabel.setText(user.getFullname());
        emailLabel.setText(user.getEmail());
        phoneLabel.setText(user.getPhone());
        genderLabel.setText(user.getGender());
        addressLabel.setText(user.getAddress());

        // Mengubah huruf pertama dari peran (role) menjadi kapital untuk tampilan.
        String role = user.getRole().substring(0, 1).toUpperCase() + user.getRole().substring(1);
        roleLabel.setText(role);

        // Mengatur gambar avatar default untuk pengguna.
        try {
            Image defaultAvatar = new Image(getClass().getResourceAsStream("/images/user.png"));
            avatarView.setImage(defaultAvatar);
        } catch (Exception e) {
            System.err.println("Gagal memuat gambar avatar default: " + e.getMessage());
        }
    }
}