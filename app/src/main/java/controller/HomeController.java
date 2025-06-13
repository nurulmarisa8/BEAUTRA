package controller;

import beautra.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.CartItem;
import model.Product;
import service.ProductService;
import util.AlertUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HomeController {

    @FXML private TextField searchField;
    @FXML private FlowPane productGrid;
    @FXML private Button forYouBtn, skinCareBtn, bodyCareBtn, hairCareBtn, makeUpBtn;
    @FXML private Button cartIconBtn;
    @FXML private Button logoutBtn;

    // Instance statis untuk memungkinkan akses dari controller lain
    private static HomeController instance;

    private final ProductService productService = new ProductService();
    private List<Product> allProducts;
    private final List<CartItem> cart = new ArrayList<>();

    /**
     * Method yang dieksekusi secara otomatis saat FXML selesai dimuat.
     * Menginisialisasi state awal controller, seperti mengambil semua data produk,
     * menambahkan listener ke search field, dan menampilkan produk awal.
     */
    @FXML
    public void initialize() {
        // Inisialisasi instance statis untuk bisa diakses dari controller lain.
        instance = this;

        allProducts = productService.getAllProducts();
        searchField.textProperty().addListener((obs, oldV, newV) -> searchProducts(newV));
        showProducts(allProducts);
        setActiveCategory(forYouBtn);
    }

    /**
     * Mengembalikan instance dari HomeController yang sedang aktif.
     * Digunakan untuk memungkinkan controller lain memanggil method publik di sini,
     * seperti refreshProducts().
     * @return instance dari HomeController.
     */
    public static HomeController getInstance() {
        return instance;
    }

    /**
     * Memuat ulang data produk dari sumbernya (JSON) dan menampilkan ulang ke UI.
     * Method ini berguna untuk me-refresh tampilan setelah ada perubahan data,
     * misalnya setelah checkout atau menutup jendela detail produk.
     */
    public void refreshProducts() {
        allProducts = productService.getAllProducts();
        showProducts(allProducts);
    }

    /**
     * Menyaring daftar produk berdasarkan teks pencarian dan memperbarui grid.
     * @param searchText Teks yang diketik pengguna di search field.
     */
    private void searchProducts(String searchText) {
        List<Product> filteredList;
        if (searchText == null || searchText.trim().isEmpty()) {
            filteredList = allProducts;
        } else {
            String lowerCaseFilter = searchText.toLowerCase().trim();
            filteredList = allProducts.stream().filter(p ->
                (p.getName() != null && p.getName().toLowerCase().contains(lowerCaseFilter)) ||
                (p.getCategory() != null && p.getCategory().toLowerCase().contains(lowerCaseFilter))
            ).collect(Collectors.toList());
        }
        showProducts(filteredList);
    }

    /**
     * Menampilkan daftar produk ke dalam FlowPane (productGrid).
     * Method ini akan menghapus semua produk lama dan mengisi grid dengan produk baru.
     * @param products List produk yang akan ditampilkan.
     */
    private void showProducts(List<Product> products) {
        productGrid.getChildren().clear();
        for (Product product : products) {
            productGrid.getChildren().add(createProductCard(product));
        }
    }

    /**
     * Membuat satu kartu (card) produk secara dinamis untuk ditampilkan di grid.
     * Kartu ini berisi gambar, nama, harga, stok, dan tombol interaksi keranjang.
     * @param product Objek produk yang datanya akan ditampilkan.
     * @return VBox yang merupakan representasi visual dari kartu produk.
     */
    private VBox createProductCard(Product product) {
        VBox card = new VBox(8);
        card.setPrefWidth(180);
        card.setPrefHeight(250);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(150);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        // Memuat gambar produk dengan penanganan error jika gambar tidak ditemukan.
        try {
            String imagePath = product.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                if (!imagePath.startsWith("/")) imagePath = "/" + imagePath;
                InputStream imageStream = getClass().getResourceAsStream(imagePath);
                if (imageStream != null) imageView.setImage(new Image(imageStream));
                else imageView.setImage(new Image(getClass().getResourceAsStream("/images/default-product.png")));
            } else {
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/default-product.png")));
            }
        } catch (Exception e) {
            imageView.setImage(new Image(getClass().getResourceAsStream("/images/default-product.png")));
        }

        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(150);

        Label priceLabel = new Label("Rp " + String.format("%,.0f", product.getPrice()));
        priceLabel.setStyle("-fx-text-fill: #ff6666; -fx-font-weight: bold; -fx-font-size: 13px;");

        Label stockLabel = new Label("Stok: " + product.getStock());
        stockLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 11px;");
        
        // Kontrol untuk menambah item ke keranjang.
        Button addToCartBtn = new Button("+ Keranjang");
        addToCartBtn.setStyle("-fx-background-color: #ff6666; -fx-text-fill: white; -fx-background-radius: 6; -fx-font-size: 11px; -fx-cursor: hand;");
        addToCartBtn.setPrefWidth(150);
        
        // Kontrol untuk mengubah kuantitas item (kurang/tambah).
        Label quantityLabel = new Label();
        quantityLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Button minusBtn = new Button("−");
        Button plusBtn = new Button("+");
        String quantityBtnStyle = "-fx-background-color: #ff6666; -fx-text-fill: white; -fx-background-radius: 50; -fx-font-weight: bold; -fx-min-width: 28px; -fx-min-height: 28px; -fx-cursor: hand;";
        minusBtn.setStyle(quantityBtnStyle);
        plusBtn.setStyle(quantityBtnStyle);
        
        HBox quantityControlBox = new HBox(10, minusBtn, quantityLabel, plusBtn);
        quantityControlBox.setAlignment(Pos.CENTER);
        
        StackPane cartControlContainer = new StackPane(addToCartBtn, quantityControlBox);
        cartControlContainer.setAlignment(Pos.CENTER);
        
        // Logika untuk menampilkan tombol 'tambah' atau kontrol kuantitas.
        Runnable updateView = () -> {
            Optional<CartItem> itemInCart = findCartItem(product.getId());
            if (itemInCart.isPresent()) {
                quantityLabel.setText(String.valueOf(itemInCart.get().getQuantity()));
                addToCartBtn.setVisible(false);
                quantityControlBox.setVisible(true);
            } else {
                addToCartBtn.setVisible(true);
                quantityControlBox.setVisible(false);
            }
        };

        addToCartBtn.setOnAction(e -> {
            addItemToCart(product);
            updateView.run();
        });

        plusBtn.setOnAction(e -> {
            increaseItemQuantity(product);
            updateView.run();
        });

        minusBtn.setOnAction(e -> {
            decreaseItemQuantity(product);
            updateView.run();
        });

        updateView.run();

        card.getChildren().addAll(imageView, nameLabel, priceLabel, stockLabel, cartControlContainer);
        // Menambahkan aksi klik pada kartu untuk membuka detail produk.
        card.setOnMouseClicked(event -> {
             if (event.getTarget() instanceof Button || event.getTarget() instanceof Label) return;
             showProductDetail(product);
        });
        return card;
    }

    /**
     * Mencari item di dalam keranjang berdasarkan ID produk.
     * @param productId ID produk yang dicari.
     * @return Optional yang berisi CartItem jika ditemukan, atau kosong jika tidak.
     */
    private Optional<CartItem> findCartItem(String productId) {
        return cart.stream().filter(ci -> ci.getProductId().equals(productId)).findFirst();
    }

    /**
     * Menambahkan produk ke dalam keranjang dengan kuantitas awal 1.
     * @param product Produk yang akan ditambahkan.
     */
    private void addItemToCart(Product product) {
        if (product.getStock() > 0) {
            cart.add(new CartItem(product.getId(), 1, product.getPrice()));
        } else {
            AlertUtil.showInfo("Stok produk habis.");
        }
    }

    /**
     * Menambah kuantitas item yang sudah ada di keranjang sebanyak 1.
     * @param product Produk yang kuantitasnya akan ditambah.
     */
    private void increaseItemQuantity(Product product) {
        findCartItem(product.getId()).ifPresent(item -> {
            if (item.getQuantity() < product.getStock()) {
                item.setQuantity(item.getQuantity() + 1);
            } else {
                AlertUtil.showInfo("Jumlah di keranjang sudah mencapai batas stok.");
            }
        });
    }

    /**
     * Mengurangi kuantitas item yang sudah ada di keranjang sebanyak 1.
     * Jika kuantitas menjadi 0, item akan dihapus dari keranjang.
     * @param product Produk yang kuantitasnya akan dikurangi.
     */
    private void decreaseItemQuantity(Product product) {
        findCartItem(product.getId()).ifPresent(item -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
            } else {
                cart.remove(item);
            }
        });
    }
    
    /** Aksi saat tombol filter 'For You' diklik. */
    @FXML private void onForYou() { setActiveCategory(forYouBtn); showProducts(allProducts); }
    /** Aksi saat tombol filter 'SkinCare' diklik. */
    @FXML private void onSkinCare() { filterByCategory("SkinCare", skinCareBtn); }
    /** Aksi saat tombol filter 'BodyCare' diklik. */
    @FXML private void onBodyCare() { filterByCategory("BodyCare", bodyCareBtn); }
    /** Aksi saat tombol filter 'Hair Care' diklik. */
    @FXML private void onHairCare() { filterByCategory("Hair Care", hairCareBtn); }
    /** Aksi saat tombol filter 'Make Up' diklik. */
    @FXML private void onMakeUp() { filterByCategory("Make Up", makeUpBtn); }

    /**
     * Method helper untuk menyaring produk berdasarkan kategori dan memperbarui UI.
     * @param category Nama kategori untuk filter.
     * @param btn Tombol yang diklik, untuk diatur menjadi aktif.
     */
    private void filterByCategory(String category, Button btn) {
        setActiveCategory(btn);
        showProducts(allProducts.stream()
            .filter(p -> category.equalsIgnoreCase(p.getCategory()))
            .collect(Collectors.toList()));
    }

    /**
     * Mengatur style visual untuk tombol kategori yang aktif.
     * @param activeBtn Tombol yang akan diberi style aktif.
     */
    private void setActiveCategory(Button activeBtn) {
        forYouBtn.getStyleClass().setAll("soft-btn");
        skinCareBtn.getStyleClass().setAll("soft-btn");
        bodyCareBtn.getStyleClass().setAll("soft-btn");
        hairCareBtn.getStyleClass().setAll("soft-btn");
        makeUpBtn.getStyleClass().setAll("soft-btn");
        activeBtn.getStyleClass().setAll("category-btn-active");
    }

    /**
     * Membuka jendela keranjang belanja (cart.fxml) sebagai dialog modal.
     */
    @FXML
    private void openCart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/cart.fxml"));
            Parent root = loader.load();
            CartController cartController = loader.getController();
            cartController.setCart(cart);

            // Menambahkan aksi pada tombol checkout di dalam cart controller.
            cartController.getCheckoutBtn().setOnAction(e -> {
                Stage cartStage = (Stage) cartController.getCheckoutBtn().getScene().getWindow();
                cartStage.close();
                openCheckoutPage();
            });

            Stage stage = new Stage();
            stage.setTitle("Keranjang Belanja");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            // Me-refresh halaman utama saat jendela keranjang ditutup.
            stage.setOnHidden(e -> refreshProducts());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Membuka jendela checkout (checkout.fxml).
     * Method ini dipanggil setelah tombol checkout di keranjang diklik.
     */
    private void openCheckoutPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/checkout.fxml"));
            Parent root = loader.load();
            CheckoutController checkoutController = loader.getController();
            // Mengirim salinan keranjang ke halaman checkout.
            checkoutController.checkout(new ArrayList<>(cart), MainApp.currentUser.getId());
            
            Stage stage = new Stage();
            stage.setTitle("Checkout");
            stage.setScene(new Scene(root));
            
            // Mengosongkan keranjang dan me-refresh produk setelah checkout selesai.
            stage.setOnHidden(e -> {
                refreshProducts(); // 1. Update stok produk dari file
                cart.clear();      // 2. Kosongkan keranjang belanja
                refreshProducts(); // 3. Refresh sekali lagi untuk update tampilan tombol
            });

            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Membuka jendela profil pengguna (profile.fxml) sebagai dialog modal.
     */
    @FXML
    private void openProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/profile.fxml"));
            Parent root = loader.load();
            ProfileController controller = loader.getController();
            controller.setUser(MainApp.currentUser);
            Stage stage = new Stage();
            stage.setTitle("Profil Saya");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Membuka jendela detail untuk produk yang dipilih.
     * Metode ini mengirimkan data produk DAN referensi keranjang
     * ke ProductDetailController.
     * @param product Produk yang akan ditampilkan.
     */
    private void showProductDetail(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/product_detail.fxml"));
            Parent root = loader.load();
            
            ProductDetailController controller = loader.getController();
            // Mengirimkan objek produk dan referensi ke keranjang belanja saat ini.
            controller.initializeDetails(product, this.cart);
            
            Stage detailStage = new Stage();
            detailStage.setTitle("Detail Produk: " + product.getName());
            detailStage.setScene(new Scene(root));
            detailStage.initModality(Modality.APPLICATION_MODAL);
            
            // Me-refresh halaman utama saat jendela detail ditutup.
            detailStage.setOnHidden(e -> refreshProducts());
            
            detailStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Menangani proses logout pengguna.
     * Mengosongkan data pengguna saat ini dan kembali ke halaman login.
     */
    @FXML
    private void handleLogout() {
        try {
            MainApp.currentUser = null;
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("BEAUTRA Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}