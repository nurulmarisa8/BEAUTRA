package model;

/**
 * Kelas model yang merepresentasikan sebuah produk yang dijual dalam aplikasi.
 * Berisi semua atribut yang dimiliki oleh sebuah produk.
 */
public class Product {
    private String id, name, category, brand, description, image, sellerId;
    private double price;
    private int stock;

    /**
     * Konstruktor untuk membuat objek Produk baru dengan semua detailnya.
     * @param id ID unik produk.
     * @param name Nama produk.
     * @param category Kategori produk (misal: "SkinCare").
     * @param brand Merek produk.
     * @param description Deskripsi detail produk.
     * @param price Harga produk.
     * @param stock Jumlah stok yang tersedia.
     * @param image Path ke file gambar produk.
     * @param sellerId ID dari penjual produk.
     */
    public Product(String id, String name, String category, String brand, String description, double price, int stock, String image, String sellerId) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.image = image;
        this.sellerId = sellerId;
    }

    // --- GETTERS (Untuk mendapatkan nilai) ---
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getBrand() { return brand; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getImage() { return image; }
    public String getSellerId() { return sellerId; }

    // --- SETTERS (Untuk mengubah nilai) ---
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
    public void setImage(String image) { this.image = image; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId;}
}