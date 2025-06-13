package controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import model.CartItem;
import model.Product;
import service.ProductService;

import java.util.ArrayList;
import java.util.List;

public class CartController {
    @FXML private VBox cartBox;
    @FXML private Label cartTotalValue;
    @FXML private Button checkoutBtn;

    private final List<CartItem> cart = new ArrayList<>();
    private final ProductService productService = new ProductService();

    /**
     * Menginisialisasi atau memperbarui data keranjang (cart) dari controller lain.
     * Method ini membuat salinan dari item keranjang yang diterima untuk memastikan
     * data di controller ini terisolasi, lalu memanggil updateCartBox() untuk
     * me-render ulang tampilan UI.
     * @param cartData List CartItem yang akan ditampilkan.
     */
    public void setCart(List<CartItem> cartData) {
        cart.clear();
        for (CartItem ci : cartData) {
            cart.add(new CartItem(ci.getProductId(), ci.getQuantity(), ci.getPrice()));
        }
        updateCartBox();
    }
    
    /**
     * Merender ulang seluruh tampilan visual dari keranjang belanja.
     * Method ini akan membersihkan tampilan lama, lalu menampilkan pesan jika keranjang kosong
     * atau membuat ulang setiap baris item produk (gambar, nama, harga, tombol kuantitas)
     * jika keranjang berisi item. Method ini juga menghitung dan menampilkan total harga.
     */
    private void updateCartBox() {
        cartBox.getChildren().clear();
        double total = 0;

        // Jika keranjang kosong, tampilkan pesan dan nonaktifkan tombol checkout.
        if (cart.isEmpty()) {
            Label emptyLabel = new Label("Keranjang Anda masih kosong.");
            emptyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #888888;");
            cartBox.getChildren().add(emptyLabel);
            cartBox.setAlignment(Pos.CENTER);
            cartTotalValue.setText("Rp0");
            checkoutBtn.setDisable(true); 
            return;
        }

        // Jika keranjang tidak kosong, aktifkan tombol checkout dan atur alignment.
        checkoutBtn.setDisable(false);
        cartBox.setAlignment(Pos.TOP_LEFT); 

        // Loop melalui setiap item di keranjang untuk membuat baris tampilannya.
        for (CartItem ci : cart) {
            Product p = productService.getProductById(ci.getProductId());

            HBox row = new HBox(15);
            row.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 15; -fx-border-color: #eeeeee; -fx-border-width: 1; -fx-border-radius: 12;");
            row.setAlignment(Pos.CENTER_LEFT);

            ImageView img = new ImageView();
            img.setFitWidth(70);
            img.setFitHeight(70);
            
            // Memuat gambar produk, atau gambar default jika gagal atau tidak ada.
            if (p != null && p.getImage() != null && !p.getImage().isEmpty()) {
                try {
                    String imagePath = p.getImage().startsWith("/") ? p.getImage() : "/" + p.getImage();
                    img.setImage(new Image(getClass().getResourceAsStream(imagePath)));
                } catch (Exception e) {
                    img.setImage(new Image(getClass().getResourceAsStream("/images/default-product.png")));
                }
            } else {
                 img.setImage(new Image(getClass().getResourceAsStream("/images/default-product.png")));
            }
            // Membuat gambar memiliki sudut melengkung.
            Rectangle clip = new Rectangle(70, 70);
            clip.setArcWidth(10);
            clip.setArcHeight(10);
            img.setClip(clip);

            // Membuat VBox untuk menampung nama dan harga produk.
            VBox info = new VBox(5);
            Label name = new Label(p != null ? p.getName() : "Produk tidak ditemukan");
            name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");
            name.setWrapText(true);

            Label price = new Label("Rp" + String.format("%,.0f", ci.getPrice()));
            price.setStyle("-fx-font-size: 14px; -fx-text-fill: #ff6666;");
            info.getChildren().addAll(name, price);

            // Region digunakan untuk mendorong kontrol kuantitas ke sisi kanan.
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);

            // Membuat kontrol kuantitas (tombol -, jumlah, tombol +).
            HBox qtyBox = new HBox(10);
            qtyBox.setAlignment(Pos.CENTER);
            Button minus = new Button("−");
            Label qty = new Label(String.valueOf(ci.getQuantity()));
            Button plus = new Button("+");

            String buttonStyle = "-fx-background-color: #f0f0f0; -fx-text-fill: #333; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 50; -fx-min-width: 30px; -fx-min-height: 30px; -fx-cursor: hand;";
            minus.setStyle(buttonStyle);
            plus.setStyle(buttonStyle);
            
            qty.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333333;");

            // Aksi untuk tombol minus: mengurangi kuantitas atau menghapus item.
            minus.setOnAction(e -> {
                if (ci.getQuantity() > 1) {
                    ci.setQuantity(ci.getQuantity() - 1);
                } else {
                    cart.remove(ci);
                }
                updateCartBox();
            });
            // Aksi untuk tombol plus: menambah kuantitas jika stok masih ada.
            plus.setOnAction(e -> {
                if (p != null && ci.getQuantity() < p.getStock()) {
                    ci.setQuantity(ci.getQuantity() + 1);
                    updateCartBox();
                }
            });
            qtyBox.getChildren().addAll(minus, qty, plus);

            row.getChildren().addAll(img, info, region, qtyBox);
            cartBox.getChildren().add(row);

            total += ci.getPrice() * ci.getQuantity();
        }
        cartTotalValue.setText("Rp" + String.format("%,.0f", total));
    }

    /**
     * Mengembalikan list item yang saat ini ada di dalam keranjang.
     * Method ini digunakan untuk mengirim data keranjang ke halaman checkout.
     * @return List dari CartItem.
     */
    public List<CartItem> getCart() {
        return cart;
    }

    /**
     * Mengembalikan instance dari tombol checkout.
     * Method ini memungkinkan controller lain (seperti HomeController) untuk
     * mengakses tombol ini dan menambahkan event handler, misalnya untuk membuka
     * halaman checkout.
     * @return Button checkout.
     */
    public Button getCheckoutBtn() {
        return checkoutBtn;
    }
}