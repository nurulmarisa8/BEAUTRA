package service;

import model.User;
import util.JsonUtil;

import java.util.List;
import java.util.UUID;

/**
 * Kelas service yang menangani semua logika bisnis terkait otentikasi pengguna,
 * seperti login dan registrasi.
 */
public class AuthService {
    private static final String FILE_PATH = "src/main/resources/data/users.json";

    /**
     * Memvalidasi kredensial pengguna untuk proses login.
     * @param email Email yang dimasukkan pengguna.
     * @param password Password yang dimasukkan pengguna.
     * @return Objek User jika login berhasil, atau null jika gagal.
     */
    public User login(String email, String password) {
        List<User> users = JsonUtil.readJson(FILE_PATH, User.class);
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user; // Kredensial cocok, kembalikan user
            }
        }
        return null; // Tidak ada user yang cocok
    }

    /**
     * Mendaftarkan pengguna baru ke dalam sistem.
     * Method ini memeriksa duplikasi email sebelum membuat user baru.
     * @param fullname Nama lengkap pengguna.
     * @param email Alamat email.
     * @param phone Nomor telepon.
     * @param password Kata sandi.
     * @param gender Jenis kelamin.
     * @param address Alamat.
     * @param role Peran dalam aplikasi (buyer/seller).
     */
    public void register(String fullname, String email, String phone, String password, String gender, String address, String role) {
        List<User> users = JsonUtil.readJson(FILE_PATH, User.class);

        // Cek jika email sudah terdaftar untuk menghindari duplikat.
        boolean exists = users.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
        if (exists) {
            System.out.println("Email sudah digunakan.");
            // Idealnya, ini akan menampilkan alert ke pengguna, bukan hanya cetak di konsol.
            return;
        }

        // Buat ID unik untuk pengguna baru.
        String id = "USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        User newUser = new User(id, fullname, email, phone, password, gender, address, role.toLowerCase());
        users.add(newUser);

        // Tulis kembali daftar pengguna yang sudah diperbarui ke file JSON.
        JsonUtil.writeJson(FILE_PATH, users);
        System.out.println("Registrasi berhasil untuk " + fullname);
    }

    /**
     * Mengambil daftar semua pengguna yang terdaftar dari file JSON.
     * @return List dari semua objek User.
     */
    public List<User> getAllUsers() {
        return JsonUtil.readJson(FILE_PATH, User.class);
    }

    /**
     * Mencari dan mengembalikan satu pengguna berdasarkan ID uniknya.
     * @param userId ID pengguna yang ingin dicari.
     * @return Objek User jika ditemukan, atau null jika tidak.
     */
    public User getUserById(String userId) {
        List<User> users = getAllUsers();
        return users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }
}