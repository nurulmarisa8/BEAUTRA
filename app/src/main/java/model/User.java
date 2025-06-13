package model;

/**
 * Kelas model yang merepresentasikan seorang pengguna (User) dalam aplikasi.
 * Berisi semua informasi terkait pengguna, seperti detail pribadi dan peran.
 */
public class User {
    private String id;
    private String fullname;
    private String email;
    private String phone;
    private String password;
    private String gender;
    private String address;
    private String role;

    /**
     * Konstruktor untuk membuat objek User baru.
     * @param id ID unik pengguna.
     * @param fullname Nama lengkap pengguna.
     * @param email Alamat email pengguna (untuk login).
     * @param phone Nomor telepon pengguna.
     * @param password Kata sandi pengguna (untuk login).
     * @param gender Jenis kelamin pengguna.
     * @param address Alamat pengguna.
     * @param role Peran pengguna dalam aplikasi (misal: "buyer" atau "seller").
     */
    public User(String id, String fullname, String email, String phone, String password, String gender, String address, String role) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.address = address;
        this.role = role;
    }

    // --- GETTERS (Menyediakan akses baca ke properti pengguna) ---

    public String getId() { return id; }
    public String getFullname() { return fullname; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
    public String getRole() { return role; }

}