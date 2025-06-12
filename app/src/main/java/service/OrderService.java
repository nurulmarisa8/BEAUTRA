package service;

import model.CartItem;
import model.Order;
import model.Product;
import util.JsonUtil;
import java.util.List;


public class OrderService {
    private static final String FILE_PATH = "src/main/resources/data/orders.json";

    // Di dalam kelas OrderService.java

public void createOrder(List<CartItem> cart, String buyerId) {
        // Baca daftar pesanan yang sudah ada
        List<Order> orders = getAllOrders(); // getAllOrders() sekarang mengembalikan List<Order>
        
        // Hitung total harga dari keranjang
        double total = cart.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        // Buat SATU objek Order baru yang berisi list CartItem
        Order newOrder = new Order(
                "ORD-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                buyerId,
                cart, // Masukkan seluruh keranjang ke sini
                total,
                "paid", // Status awal
                java.time.LocalDateTime.now().toString(),
                "COD" // Metode pembayaran default
        );

        // Tambahkan pesanan baru ke daftar
        orders.add(newOrder);

        // Tulis kembali seluruh daftar pesanan ke file JSON
        JsonUtil.writeJson(FILE_PATH, orders);

        // --- Logika pengurangan stok ---
        ProductService productService = new ProductService();
        for (CartItem item : cart) {
            Product product = productService.getProductById(item.getProductId());
            if (product != null) {
                int newStock = product.getStock() - item.getQuantity();
                product.setStock(newStock);
                // Panggil update dengan ID produk, ini sudah benar
                productService.updateProduct(product.getId(), product);
            }
        }
    }



    // Method untuk mengambil semua orders
    public List<Order> getAllOrders() {
        return JsonUtil.readJson(FILE_PATH, Order.class);
    }

    // Method untuk mengambil order berdasarkan id
    public Order getOrderById(String orderId) {
        List<Order> orders = JsonUtil.readJson(FILE_PATH, Order.class);
        for (Order order : orders) {
            if (order.getId().equals(orderId)) {
                return order;
            }
        }
        return null;
    }
}
