package model;

import java.util.List;

/**
 * Kelas model yang merepresentasikan sebuah pesanan (Order).
 * Kelas ini mengimplementasikan interface ITransaksi, yang menandakan bahwa
 * Order adalah sebuah bentuk transaksi dalam sistem.
 */
public class Order implements ITransaksi {
    
    private String id;
    private String buyerId; 
    private List<CartItem> items;
    private double total;
    private String status;
    private String timestamp;
    private String paymentMethod;
    
    /**
     * Konstruktor kosong (no-argument constructor).
     * Seringkali diperlukan oleh library seperti GSON atau Jackson untuk membuat
     * objek dari file JSON (deserialization).
     */
    public Order() {}
    
    /**
     * Konstruktor utama untuk membuat objek Order baru dengan semua detailnya.
     * @param id ID unik untuk pesanan.
     * @param buyerId ID pengguna yang melakukan pesanan.
     * @param items Daftar item yang dipesan.
     * @param total Total harga pesanan.
     * @param status Status pesanan (misal: "paid").
     * @param timestamp Waktu pesanan dibuat.
     * @param paymentMethod Metode pembayaran yang digunakan.
     */
    public Order(String id, String buyerId, List<CartItem> items, double total, String status, String timestamp, String paymentMethod) {
        this.id = id;
        this.buyerId = buyerId;
        this.items = items;
        this.total = total;
        this.status = status;
        this.timestamp = timestamp;
        this.paymentMethod = paymentMethod;
    }

    // --- METHOD DARI INTERFACE ITRANSAKSI ---

    /**
     * Mengembalikan ID unik dari pesanan ini.
     * @return String ID pesanan.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Mengembalikan daftar item yang ada dalam pesanan ini.
     * @return List dari CartItem.
     */
    @Override
    public List<CartItem> getItems() {
        return items;
    }

    /**
     * Mengembalikan total harga dari pesanan ini.
     * @return double total harga.
     */
    @Override
    public double getTotal() {
        return total;
    }
    
    /**
     * Mengembalikan status dari pesanan ini.
     * @return String status pesanan.
     */
    @Override
    public String getStatus() {
        return status;
    }

    /**
     * Mengembalikan waktu (timestamp) saat pesanan ini dibuat.
     * @return String timestamp.
     */
    @Override
    public String getTimestamp() {
        return timestamp;
    }
    
    // --- GETTER & SETTER LAINNYA ---

    /**
     * Mengembalikan ID dari pembeli (buyer).
     * @return String ID pembeli.
     */
    public String getBuyerId() {
        return buyerId;
    }
    
    /**
     * Mengembalikan metode pembayaran yang digunakan untuk pesanan ini.
     * @return String metode pembayaran.
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Mengatur atau memperbarui status pesanan.
     * Berguna untuk mengubah status dari "paid" menjadi "shipped", misalnya.
     * @param status Status baru untuk pesanan.
     */
    public void setStatus(String status) {
        this.status = status;
    }
}