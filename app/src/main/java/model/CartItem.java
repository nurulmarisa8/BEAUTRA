package model;

/**
 * Kelas model yang merepresentasikan satu item di dalam keranjang belanja.
 * Menyimpan informasi tentang produk, kuantitas, dan harga saat ditambahkan.
 */
public class CartItem {
    private String productId;
    private int quantity;
    private double price;

    /**
     * Konstruktor untuk membuat objek CartItem baru.
     * @param productId ID dari produk yang ditambahkan.
     * @param quantity Jumlah produk yang ditambahkan.
     * @param price Harga per unit produk.
     */
    public CartItem(String productId, int quantity, double price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    /**
     * Mengembalikan ID produk dari item ini.
     * @return String ID produk.
     */
    public String getProductId() { return productId; }

    /**
     * Mengembalikan kuantitas dari item ini.
     * @return integer jumlah kuantitas.
     */
    public int getQuantity() { return quantity; }

    /**
     * Mengembalikan harga per unit dari item ini.
     * @return double harga produk.
     */
    public double getPrice() { return price; }

    /**
     * Mengatur atau memperbarui kuantitas dari item ini.
     * @param quantity Kuantitas baru.
     */
    public void setQuantity(int quantity) { this.quantity = quantity; }
}