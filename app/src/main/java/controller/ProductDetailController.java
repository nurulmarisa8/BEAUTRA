package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import model.CartItem;
import model.Product;
import util.AlertUtil;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class ProductDetailController {

    // --- Komponen FXML yang sudah ada ---
    @FXML private ImageView productImageView;
    @FXML private Label nameLabel;
    @FXML private Label priceLabel;
    @FXML private Label categoryLabel;
    @FXML private Label stockLabel;
    @FXML private Label descriptionLabel;

    // --- Komponen FXML Baru untuk Kontrol Keranjang ---
    @FXML private StackPane cartControlContainer;
    @FXML private Button addToCartBtn;
    @FXML private HBox quantityControlBox;
    @FXML private Button minusBtn;
    @FXML private Button plusBtn;
    @FXML private Label quantityLabel;

    // --- Variabel untuk menampung data ---
    private Product currentProduct;
    private List<CartItem> cart;

    /**
     * Metode utama untuk menginisialisasi halaman detail.
     * Menerima data produk dan referensi keranjang dari HomeController, lalu
     * mengisi semua komponen UI dengan data tersebut.
     * @param product Produk yang akan ditampilkan.
     * @param cart Keranjang belanja yang sama dengan yang ada di HomeController.
     */
    public void initializeDetails(Product product, List<CartItem> cart) {
        this.currentProduct = product;
        this.cart = cart;

        if (product == null) return;

        // Set teks untuk semua label di UI.
        nameLabel.setText(product.getName());
        priceLabel.setText("Rp " + String.format("%,.0f", product.getPrice()));
        categoryLabel.setText(product.getCategory()); 
        stockLabel.setText(String.valueOf(product.getStock())); 
        descriptionLabel.setText(product.getDescription());
        descriptionLabel.setWrapText(true);

        // Memanggil method untuk memuat gambar produk.
        loadProductImage(product.getImage());

        // Memanggil method untuk mengatur tombol-tombol interaksi keranjang.
        setupCartControls();
    }

    /**
     * Mengatur logika dan tampilan untuk tombol-tombol keranjang (+, -, dan Tambah).
     * Method ini juga mendefinisikan aksi (event handler) untuk setiap tombol.
     */
    private void setupCartControls() {
        // Terapkan styling agar konsisten.
        String quantityBtnStyle = "-fx-background-color: #ff6666; -fx-text-fill: white; -fx-background-radius: 50; -fx-font-weight: bold; -fx-min-width: 30px; -fx-min-height: 30px; -fx-cursor: hand;";
        minusBtn.setStyle(quantityBtnStyle);
        plusBtn.setStyle(quantityBtnStyle);
        quantityLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        addToCartBtn.setStyle("-fx-background-color: #ff6666; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-size: 14px; -fx-cursor: hand; -fx-font-weight: bold;");

        // Logika untuk memperbarui tampilan tombol (menampilkan tombol 'Tambah' atau '+/-').
        Runnable updateView = () -> {
            Optional<CartItem> itemInCart = findCartItem(currentProduct.getId());
            if (itemInCart.isPresent()) {
                quantityLabel.setText(String.valueOf(itemInCart.get().getQuantity()));
                addToCartBtn.setVisible(false);
                quantityControlBox.setVisible(true);
            } else {
                addToCartBtn.setVisible(true);
                quantityControlBox.setVisible(false);
            }
        };

        // Atur aksi untuk setiap tombol.
        addToCartBtn.setOnAction(e -> {
            addItemToCart();
            updateView.run();
        });

        plusBtn.setOnAction(e -> {
            increaseItemQuantity();
            updateView.run();
        });

        minusBtn.setOnAction(e -> {
            decreaseItemQuantity();
            updateView.run();
        });

        // Panggil sekali di awal untuk mengatur tampilan yang benar saat jendela dibuka.
        updateView.run();
    }

    /**
     * Mencari item di dalam keranjang berdasarkan ID produk.
     * @param productId ID produk yang akan dicari.
     * @return Optional yang berisi CartItem jika ditemukan.
     */
    private Optional<CartItem> findCartItem(String productId) {
        return cart.stream().filter(ci -> ci.getProductId().equals(productId)).findFirst();
    }

    /**
     * Menambahkan produk saat ini ke dalam keranjang jika stok tersedia.
     */
    private void addItemToCart() {
        if (currentProduct.getStock() > 0) {
            cart.add(new CartItem(currentProduct.getId(), 1, currentProduct.getPrice()));
        } else {
            AlertUtil.showInfo("Stok produk habis.");
        }
    }

    /**
     * Menambah kuantitas produk yang sudah ada di keranjang, dengan batas maksimal stok.
     */
    private void increaseItemQuantity() {
        findCartItem(currentProduct.getId()).ifPresent(item -> {
            if (item.getQuantity() < currentProduct.getStock()) {
                item.setQuantity(item.getQuantity() + 1);
            } else {
                AlertUtil.showInfo("Jumlah di keranjang sudah mencapai batas stok.");
            }
        });
    }

    /**
     * Mengurangi kuantitas produk di keranjang. Jika kuantitas menjadi 1,
     * item akan dihapus dari keranjang.
     */
    private void decreaseItemQuantity() {
        findCartItem(currentProduct.getId()).ifPresent(item -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
            } else {
                cart.remove(item);
            }
        });
    }

    /**
     * Memuat gambar produk ke dalam ImageView dari path yang diberikan.
     * Akan memuat gambar default jika path tidak valid atau terjadi error.
     * @param imagePath Path lokasi file gambar.
     */
    private void loadProductImage(String imagePath) {
        try {
            if (imagePath != null && !imagePath.isEmpty()) {
                if (!imagePath.startsWith("/")) {
                    imagePath = "/" + imagePath;
                }
                InputStream imageStream = getClass().getResourceAsStream(imagePath);
                if (imageStream != null) {
                    productImageView.setImage(new Image(imageStream));
                } else {
                    loadDefaultImage();
                }
            } else {
                loadDefaultImage();
            }
        } catch (Exception e) {
            System.err.println("Gagal memuat gambar detail: " + e.getMessage());
            loadDefaultImage();
        }
    }

    /**
     * Memuat gambar default sebagai fallback jika gambar produk utama tidak tersedia.
     */
    private void loadDefaultImage() {
        try {
            InputStream defaultImageStream = getClass().getResourceAsStream("/images/default-product.png");
            if (defaultImageStream != null) {
                productImageView.setImage(new Image(defaultImageStream));
            }
        } catch (Exception ex) {
            productImageView.setImage(null);
        }
    }
}