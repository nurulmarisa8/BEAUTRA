package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.CartItem;
import model.Product;
import service.OrderService;
import service.ProductService;
import util.AlertUtil;

import java.util.List;

/**
 * Controller ini bertanggung jawab untuk halaman Checkout.
 * Tugasnya adalah menampilkan ringkasan pesanan, mengambil data pengiriman dari pengguna,
 * dan mendelegasikan proses pembuatan pesanan ke OrderService.
 */
public class CheckoutController {

    //======================================================================
    // 1. DEKLARASI KOMPONEN FXML DAN SERVICE
    //======================================================================

    // Komponen UI dari file checkout.fxml
    @FXML private TextField nameField, phoneField, addressField;
    @FXML private ComboBox<String> paymentBox;
    @FXML private VBox orderSummaryBox;
    @FXML private Label subtotalLabel, shippingLabel, totalLabel;
    @FXML private Button placeOrderBtn;

    // Service untuk menjalankan logika bisnis
    private final OrderService orderService = new OrderService();
    private final ProductService productService = new ProductService();
    
    // Konstanta untuk biaya pengiriman
    private static final double SHIPPING_COST = 15000;

    // Variabel untuk menyimpan data keranjang belanja (cart)
    private List<CartItem> cart;


    //======================================================================
    // 2. METHOD UTAMA
    //======================================================================

    /**
     * Method ini dipanggil dari HomeController untuk memulai proses checkout.
     * @param cartData Keranjang belanja dari halaman sebelumnya.
     * @param buyerId ID pengguna yang melakukan checkout.
     */
    public void checkout(List<CartItem> cartData, String buyerId) {
        this.cart = cartData;
        
        // Memperbarui tampilan ringkasan pesanan di sisi kanan layar
        updateOrderSummary();

        // Mengisi pilihan metode pembayaran
        paymentBox.getItems().setAll("COD", "QRIS", "Transfer Bank");
        paymentBox.getSelectionModel().selectFirst();

        // Menetapkan aksi untuk tombol "Place Order"
        placeOrderBtn.setOnAction(e -> {
            
            // Langkah 1: Validasi input pengguna
            // Pastikan semua field pengiriman sudah diisi.
            if (nameField.getText().isEmpty() || phoneField.getText().isEmpty() || addressField.getText().isEmpty()) {
                AlertUtil.showInfo("Harap lengkapi semua data pengiriman!");
                return; // Hentikan proses jika data tidak lengkap
            }

            // Langkah 2: Delegasikan proses ke Service Layer
            // Cukup panggil satu method ini. OrderService akan menangani semuanya:
            // - Membuat pesanan dengan ID unik.
            // - Mengurangi stok produk (hanya sekali).
            // - Menyimpan transaksi ke file orders.json dengan format yang benar.
            orderService.createOrder(this.cart, buyerId);
            
            // Langkah 3: Beri notifikasi ke pengguna dan tutup jendela checkout
            AlertUtil.showInfo("Pesanan berhasil dibuat!");
            placeOrderBtn.getScene().getWindow().hide();
        });
    }


    /**
     * Method ini hanya untuk memperbarui TAMPILAN ringkasan pesanan di UI.
     * Tidak ada logika bisnis di sini.
     */
    private void updateOrderSummary() {
        orderSummaryBox.getChildren().clear();
        double subtotal = 0;

        for (CartItem ci : cart) {
            // Cari nama produk untuk ditampilkan
            Product p = productService.getProductById(ci.getProductId());

            // Buat baris tampilan untuk setiap item
            HBox row = new HBox(12);
            Label name = new Label((p != null ? p.getName() : "Produk tidak ditemukan") + " x" + ci.getQuantity());
            name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            Label price = new Label("Rp" + String.format("%,.0f", ci.getPrice() * ci.getQuantity()));
            price.setStyle("-fx-font-size: 16px;");
            row.getChildren().addAll(name, price);
            orderSummaryBox.getChildren().add(row);

            // Akumulasi subtotal
            subtotal += ci.getPrice() * ci.getQuantity();
        }

        // Tampilkan total biaya ke Label yang sesuai
        subtotalLabel.setText("Rp" + String.format("%,.0f", subtotal));
        shippingLabel.setText("Rp" + String.format("%,.0f", SHIPPING_COST));
        totalLabel.setText("Rp" + String.format("%,.0f", subtotal + SHIPPING_COST));
    }

}