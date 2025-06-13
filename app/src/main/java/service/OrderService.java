package service;

import model.CartItem;
import model.Order;
import model.Product;
import util.JsonUtil;
import java.util.List;

/**
 * Kelas service yang menangani semua logika bisnis terkait pesanan (Order),
 * seperti membuat pesanan baru dan mengambil data pesanan.
 */
public class OrderService {
    private static final String FILE_PATH = "src/main/resources/data/orders.json";

    /**
     * Membuat pesanan baru, menyimpannya ke file JSON, dan mengurangi stok produk.
     * @param cart Daftar item dalam keranjang belanja.
     * @param buyerId ID dari pengguna yang melakukan pesanan.
     */
    public void createOrder(List<CartItem> cart, String buyerId) {
        // Baca daftar pesanan yang sudah ada dari file.
        List<Order> orders = getAllOrders();
        
        // Hitung total harga dari semua item di keranjang.
        double total = cart.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        // Buat satu objek Order baru yang berisi list CartItem.
        Order newOrder = new Order(
                "ORD-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                buyerId,
                cart, // Masukkan seluruh keranjang ke sini.
                total,
                "paid", // Status awal pesanan.
                java.time.LocalDateTime.now().toString(),
                "COD" // Metode pembayaran default.
        );

        // Tambahkan pesanan baru ke dalam daftar pesanan.
        orders.add(newOrder);

        // Tulis kembali seluruh daftar pesanan yang sudah diperbarui ke file JSON.
        JsonUtil.writeJson(FILE_PATH, orders);

        // Logika untuk mengurangi stok produk setelah pesanan dibuat.
        ProductService productService = new ProductService();
        for (CartItem item : cart) {
            Product product = productService.getProductById(item.getProductId());
            if (product != null) {
                int newStock = product.getStock() - item.getQuantity();
                product.setStock(newStock);
                productService.updateProduct(product);
            }
        }
    }

    /**
     * Mengambil daftar semua pesanan dari file JSON.
     * @return List dari semua objek Order.
     */
    public List<Order> getAllOrders() {
        return JsonUtil.readJson(FILE_PATH, Order.class);
    }
}