# 💄 Beautra — Beauty Aura E-Commerce JavaFX

**Beautra** (singkatan dari **Beauty Aura**) adalah aplikasi desktop e-commerce berbasis JavaFX yang menyediakan platform modern bagi pengguna untuk membeli dan menjual produk kecantikan. Aplikasi ini memiliki dua peran utama, yaitu **Pembeli (Buyer)** dan **Penjual (Seller)**, dengan tampilan serta fitur yang disesuaikan untuk masing-masing peran.

---

## 🚀 Fitur Utama

### 🔐 Fitur Umum
- **Login & Registrasi** — Pengguna bisa membuat akun, login, serta logout. Setelah login, aplikasi otomatis mengarahkan user ke halaman pembeli atau penjual sesuai peran.
- **Routing Dinamis** — Pengalaman pengguna berbeda antara pembeli dan penjual.

### 🛍️ Fitur Pembeli (Buyer)
- **Dashboard Produk** — Menampilkan semua produk dalam format grid yang menarik.
- **Pencarian & Filter** — Cari produk berdasarkan nama atau kategori (SkinCare, BodyCare, Hair Care, Make Up).
- **Detail Produk** — Melihat info detail produk: gambar, nama, harga, stok, deskripsi.
- **Keranjang Belanja** — Tambahkan produk ke keranjang, ubah jumlah, hapus, serta validasi stok otomatis.
- **Checkout** — Isi data pengiriman, pilih metode pembayaran, cek ringkasan pesanan, lalu buat pesanan (stok otomatis berkurang).
- **Profil** — Melihat dan mengedit detail akun (nama, email, dll).
- **Tampilkan Profile** — Pengguna dapat menampilkan halaman profil pribadi secara lengkap.

### 🧾 Fitur Penjual (Seller)
- **Dashboard Penjual** — Melihat daftar produk yang dijual dan pesanan yang masuk.
- **Manajemen Produk** — Tambah, edit, atau hapus produk. Validasi input otomatis.
- **Manajemen Pesanan** — Melihat detail pesanan, status, jumlah, dan data pembeli.

### 🛠️ Utilities & Service
- **AlertUtil** — Utility untuk menampilkan pop-up alert (informasi, error, konfirmasi) secara konsisten di seluruh aplikasi.
- **JsonUtil** — Utility untuk memudahkan baca/tulis data ke file JSON (`users.json`, `products.json`, `orders.json`).
- **ProductService & OrderService** — Pengelolaan data produk & order, CRUD, filtering, dsb.

---

## ⚙️ Cara Menjalankan Aplikasi

### 1. Prasyarat
- **Java Development Kit (JDK) 21+**  
  (Diuji pada OpenJDK 21.0.7 Temurin LTS)
- **Gradle 8.8+** untuk build dan dependency management
- **JavaFX** sudah terkonfigurasi di IDE/proyek (modular/VM options)
- Library eksternal: [`Gson`](https://github.com/google/gson)


### 2. Struktur Direktori
```
src
└── main
    ├── java
    │   ├── beautra
    │   │   └── MainApp.java
    │   ├── controller        # Logic tampilan & aksi (Login, Dashboard, Produk, dsb.)
    │   ├── model             # Data model (User, Product, Order)
    │   ├── service           # Service untuk produk & order
    │   └── util              # AlertUtil, JsonUtil, dsb.
    └── resources
        ├── css/
        ├── data/             # users.json, products.json, orders.json
        ├── fxml/             # UI layout JavaFX
        └── images/
```

### 3. Menjalankan

- **Buka project di VS Code** (atau IDE lain yang kompatibel)
- Pastikan sudah install [Java Extension Pack](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) di VS Code
- Buka terminal di VS Code pada root project
- Jalankan perintah berikut untuk build ulang dan menjalankan aplikasi:

  ```sh
  ./gradlew clean run
  ```

  atau di Windows:

  ```bat
  .\gradlew clean run
  ```

- Setelah aplikasi berjalan, ikuti instruksi di layar untuk login atau registrasi


---

## 🧪 Pengujian Fitur

| No | Skenario Pengujian        | Hasil Diharapkan                | Status |
|----|--------------------------|---------------------------------|--------|
| **A. Login & Register**       |                                 |        |
| 1  | Login pembeli            | Masuk dashboard pembeli         | ✅     |
| 2  | Login penjual            | Masuk dashboard penjual         | ✅     |
| 3  | Login salah              | Tampil error, tidak masuk       | ✅     |
| 4  | Login kosong             | Tidak ada aksi                  | ✅     |
| 5  | Registrasi valid         | Akun tersimpan                  | ✅     |
| **B. Buyer**                  |                                 |        |
| 6  | Tampilkan profile        | Halaman profil muncul lengkap   | ✅     |
| 7  | Cari produk              | Grid hanya tampil hasil pencarian| ✅    |
| 8  | Filter produk            | Grid sesuai kategori            | ✅     |
| 9  | Tambah ke keranjang      | Kontrol jumlah tampil           | ✅     |
| 10 | Edit/hapus keranjang     | Update sesuai aksi              | ✅     |
| 11 | Checkout data kosong     | Peringatan input wajib          | ✅     |
| 12 | Checkout valid           | Order terekam & stok berkurang  | ✅     |
| 13 | Logout                   | Kembali ke login                | ✅     |
| **C. Seller**                 |                                 |        |
| 14 | Tambah produk valid      | Produk baru muncul              | ✅     |
| 15 | Tambah produk kosong     | Validasi error tampil           | ✅     |
| 16 | Edit produk              | Update produk sukses            | ✅     |
| 17 | Hapus produk             | Konfirmasi & hapus produk       | ✅     |
| 18 | Lihat pesanan            | Tabel pesanan tampil            | ✅     |
| 19 | Logout                   | Kembali ke login                | ✅     |


---

## Halaman Login

![Login](https://github.com/nurulmarisa8/BEAUTRA/blob/main/BEAUTRA/readme/Login.png)

## Halaman Register

![Register](https://github.com/nurulmarisa8/BEAUTRA/blob/main/BEAUTRA/readme/Register.png)

## Menu Utama (Buyer)

![Home](https://github.com/nurulmarisa8/BEAUTRA/blob/main/BEAUTRA/readme/Home.png)

## Tampilan Lihat Profile (Buyer)

![Profile](https://github.com/nurulmarisa8/BEAUTRA/blob/main/BEAUTRA/readme/Profil.png)

## Tampilan Keranjang (Buyer)

![Keranjang](https://github.com/nurulmarisa8/BEAUTRA/blob/main/BEAUTRA/readme/Keranjang.png)

## Checkout / Pembelian (Buyer)

![Checkout](https://github.com/nurulmarisa8/BEAUTRA/blob/main/BEAUTRA/readme/Checkout.png)

## Dashboard Penjualan (Seller)

![SellerDasbor](https://github.com/nurulmarisa8/BEAUTRA/blob/main/BEAUTRA/readme/SellerDasbor.png)

## Tambahkan Produk (Seller)

![TambahProduk](https://github.com/nurulmarisa8/BEAUTRA/blob/main/BEAUTRA/readme/TambahProduk.png)

## Edit Produk (Seller)

![EditProduk](https://github.com/nurulmarisa8/BEAUTRA/blob/main/BEAUTRA/readme/EditProduk.png)

---

## 📄 Lisensi

Proyek ini dikembangkan untuk keperluan tugas final lab. Bebas digunakan dan dimodifikasi selama untuk pembelajaran.

