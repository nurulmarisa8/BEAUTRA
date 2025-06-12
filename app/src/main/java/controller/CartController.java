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
    // cartTotalLabel tidak lagi diperlukan di FXML baru, jadi bisa diabaikan
    @FXML private Label cartTotalValue;
    @FXML private Button checkoutBtn;

    private final List<CartItem> cart = new ArrayList<>();
    private final ProductService productService = new ProductService();

    public void setCart(List<CartItem> cartData) {
        cart.clear();
        for (CartItem ci : cartData) {
            cart.add(new CartItem(ci.getProductId(), ci.getQuantity(), ci.getPrice()));
        }
        updateCartBox();
    }
    
    // METODE INI TELAH DIPERBARUI DENGAN PERBAIKAN
    private void updateCartBox() {
        cartBox.getChildren().clear();
        double total = 0;

        // ===================================================================
        // PERBAIKAN UTAMA ADA DI SINI
        // ===================================================================
        if (cart.isEmpty()) {
            Label emptyLabel = new Label("Keranjang Anda masih kosong.");
            emptyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #888888;");
            cartBox.getChildren().add(emptyLabel);
            cartBox.setAlignment(Pos.CENTER);
            cartTotalValue.setText("Rp0");
            checkoutBtn.setDisable(true); // Menonaktifkan tombol jika keranjang kosong
            return;
        }

        // Jika sampai di sini, artinya keranjang tidak kosong, maka tombol diaktifkan
        checkoutBtn.setDisable(false);
        cartBox.setAlignment(Pos.TOP_LEFT); // Kembalikan alignment jika tidak kosong

        for (CartItem ci : cart) {
            // Menggunakan getProductById agar lebih efisien
            Product p = productService.getProductById(ci.getProductId());

            HBox row = new HBox(15);
            row.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 15; -fx-border-color: #eeeeee; -fx-border-width: 1; -fx-border-radius: 12;");
            row.setAlignment(Pos.CENTER_LEFT);

            ImageView img = new ImageView();
            img.setFitWidth(70);
            img.setFitHeight(70);
            
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
            Rectangle clip = new Rectangle(70, 70);
            clip.setArcWidth(10);
            clip.setArcHeight(10);
            img.setClip(clip);

            VBox info = new VBox(5);
            Label name = new Label(p != null ? p.getName() : "Produk tidak ditemukan");
            name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");
            name.setWrapText(true);

            Label price = new Label("Rp" + String.format("%,.0f", ci.getPrice()));
            price.setStyle("-fx-font-size: 14px; -fx-text-fill: #ff6666;");
            info.getChildren().addAll(name, price);

            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);

            HBox qtyBox = new HBox(10);
            qtyBox.setAlignment(Pos.CENTER);
            Button minus = new Button("−");
            Label qty = new Label(String.valueOf(ci.getQuantity()));
            Button plus = new Button("+");

            String buttonStyle = "-fx-background-color: #f0f0f0; -fx-text-fill: #333; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 50; -fx-min-width: 30px; -fx-min-height: 30px; -fx-cursor: hand;";
            minus.setStyle(buttonStyle);
            plus.setStyle(buttonStyle);
            
            // Perbaikan untuk memastikan teks jumlah terlihat
            qty.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333333;");

            minus.setOnAction(e -> {
                if (ci.getQuantity() > 1) {
                    ci.setQuantity(ci.getQuantity() - 1);
                } else {
                    cart.remove(ci);
                }
                updateCartBox();
            });
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

    public List<CartItem> getCart() {
        return cart;
    }

    public Button getCheckoutBtn() {
        return checkoutBtn;
    }
}