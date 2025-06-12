package controller;

import beautra.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CartItem;
import model.Order;
import model.Product;
import service.OrderService;
import service.ProductService;
import util.AlertUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class SellerDashboardController {

    //======================================================================
    // 1. DEKLARASI FXML DAN SERVICE
    //======================================================================

    @FXML private ListView<Product> productList;
    @FXML private TableView<OrderRow> orderTable;
    @FXML private Button logoutBtn;

    @FXML private TableColumn<OrderRow, String> orderIdCol;
    @FXML private TableColumn<OrderRow, String> productNameCol;
    @FXML private TableColumn<OrderRow, Integer> qtyCol;
    @FXML private TableColumn<OrderRow, Double> totalCol;
    @FXML private TableColumn<OrderRow, String> statusCol;

    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();
    
    private static File lastOpenedDirectory;

    //======================================================================
    // 2. INISIALISASI
    //======================================================================

    @FXML
    public void initialize() {
        productList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);
                if (empty || product == null || product.getName() == null) {
                    setText(null);
                } else {
                    setText(product.getName() + " | Stok: " + product.getStock() + " | Rp" + product.getPrice());
                }
            }
        });

        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadSellerProducts();
        loadOrdersForSeller();
    }

    //======================================================================
    // 3. LOGIKA MEMUAT DATA
    //======================================================================

    private void loadSellerProducts() {
        if (MainApp.currentUser == null) return;
        List<Product> sellerProducts = productService.getAllProducts().stream()
                .filter(p -> MainApp.currentUser.getId().equals(p.getSellerId()))
                .collect(Collectors.toList());
        productList.setItems(FXCollections.observableArrayList(sellerProducts));
    }

    private void loadOrdersForSeller() {
        ObservableList<OrderRow> data = FXCollections.observableArrayList();
        if (MainApp.currentUser == null) return;
        String currentSellerId = MainApp.currentUser.getId();
        List<Order> allOrders = orderService.getAllOrders();

        for (Order order : allOrders) {
            for (CartItem item : order.getItems()) {
                Product product = productService.getProductById(item.getProductId());
                if (product != null && currentSellerId.equals(product.getSellerId())) {
                    data.add(new OrderRow(
                            order.getId(),
                            product.getName(),
                            item.getQuantity(),
                            item.getPrice() * item.getQuantity(),
                            order.getStatus()
                    ));
                }
            }
        }
        orderTable.setItems(data);
    }
    
    //======================================================================
    // 4. DIALOG TAMBAH & EDIT
    //======================================================================

    @FXML
    public void showAddProductDialog() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Tambah Produk Baru");
        dialog.setHeaderText("Masukkan detail produk yang akan ditambahkan.");

        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/seller-style.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("custom-dialog");

        ButtonType saveButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
    
        Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.getStyleClass().add("dialog-save-button");
        Button cancelButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.getStyleClass().add("dialog-cancel-button");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 100, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Nama Produk");
        nameField.getStyleClass().add("form-input");

        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll("SkinCare", "BodyCare", "Hair Care", "Make Up");
        categoryComboBox.setPromptText("Pilih Kategori");
        categoryComboBox.getStyleClass().add("form-input");
        
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Deskripsi Produk");
        descriptionArea.setWrapText(true);
        descriptionArea.getStyleClass().add("form-input");

        TextField priceField = new TextField();
        priceField.setPromptText("Harga");
        priceField.getStyleClass().add("form-input");

        TextField stockField = new TextField();
        stockField.setPromptText("Stok");
        stockField.getStyleClass().add("form-input");

        Button chooseImageBtn = new Button("Pilih Gambar...");
        chooseImageBtn.getStyleClass().add("edit-button");
        Label imagePathLabel = new Label("Belum ada gambar dipilih.");
        HBox imageBox = new HBox(10, chooseImageBtn, imagePathLabel);
        imageBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        
        if (lastOpenedDirectory != null) {
            fileChooser.setInitialDirectory(lastOpenedDirectory);
        }

        chooseImageBtn.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(dialog.getOwner());
            if (selectedFile != null) {
                lastOpenedDirectory = selectedFile.getParentFile();
                String imagePath = "/images/" + selectedFile.getName();
                imagePathLabel.setText(imagePath);
            }
        });

        grid.add(new Label("Nama:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Kategori:"), 0, 1);
        grid.add(categoryComboBox, 1, 1);
        grid.add(new Label("Deskripsi:"), 0, 2);
        grid.add(descriptionArea, 1, 2);
        grid.add(new Label("Harga:"), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(new Label("Stok:"), 0, 4);
        grid.add(stockField, 1, 4);
        grid.add(new Label("Gambar:"), 0, 5);
        grid.add(imageBox, 1, 5);

        dialog.getDialogPane().setContent(grid);

        // --- PERUBAHAN UTAMA UNTUK VALIDASI ---
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                
                // 1. Validasi semua input tidak boleh kosong
                if (nameField.getText().trim().isEmpty() ||
                    descriptionArea.getText().trim().isEmpty() ||
                    priceField.getText().trim().isEmpty() ||
                    stockField.getText().trim().isEmpty()) {
                    AlertUtil.showInfo("Semua kolom wajib diisi!");
                    return null; // Mencegah dialog tertutup
                }

                // 2. Validasi spesifik untuk kategori
                String category = categoryComboBox.getValue();
                if (category == null || category.trim().isEmpty()) {
                    AlertUtil.showInfo("Kategori wajib dipilih!");
                    return null; // Mencegah dialog tertutup
                }
                
                try {
                    String id = "P-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                    return new Product(
                        id, nameField.getText(), category, "", 
                        descriptionArea.getText(), Double.parseDouble(priceField.getText()), 
                        Integer.parseInt(stockField.getText()), imagePathLabel.getText(), 
                        MainApp.currentUser.getId()
                    );
                } catch (NumberFormatException e) {
                    AlertUtil.showInfo("Input harga atau stok tidak valid! Harap masukkan angka.");
                    return null;
                }
            }
            return null;
        });

        Optional<Product> result = dialog.showAndWait();
        result.ifPresent(product -> {
            productService.addProduct(product);
            loadSellerProducts();
        });
    }

    @FXML
    public void showEditProductDialog() {
        Product selectedProduct = productList.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            AlertUtil.showInfo("Pilih produk yang ingin diedit!");
            return;
        }

        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Edit Produk");
        dialog.setHeaderText("Edit detail produk.");

        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/seller-style.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("custom-dialog");

        ButtonType saveButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.getStyleClass().add("dialog-save-button");
        Button cancelButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.getStyleClass().add("dialog-cancel-button");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 100, 10, 10));

        TextField nameField = new TextField(selectedProduct.getName());
        nameField.getStyleClass().add("form-input");
        
        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll("SkinCare", "BodyCare", "Hair Care", "Make Up");
        categoryComboBox.setValue(selectedProduct.getCategory());
        categoryComboBox.getStyleClass().add("form-input");
        
        TextArea descriptionArea = new TextArea(selectedProduct.getDescription());
        descriptionArea.setWrapText(true);
        descriptionArea.getStyleClass().add("form-input");
        
        TextField priceField = new TextField(String.valueOf(selectedProduct.getPrice()));
        priceField.getStyleClass().add("form-input");
        
        TextField stockField = new TextField(String.valueOf(selectedProduct.getStock()));
        stockField.getStyleClass().add("form-input");
        
        Button chooseImageBtn = new Button("Pilih Gambar...");
        chooseImageBtn.getStyleClass().add("edit-button");
        Label imagePathLabel = new Label(selectedProduct.getImage() != null ? selectedProduct.getImage() : "Belum ada gambar.");
        HBox imageBox = new HBox(10, chooseImageBtn, imagePathLabel);
        imageBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        
        if (lastOpenedDirectory != null) {
            fileChooser.setInitialDirectory(lastOpenedDirectory);
        }

        chooseImageBtn.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(dialog.getOwner());
            if (selectedFile != null) {
                lastOpenedDirectory = selectedFile.getParentFile();
                String imagePath = "/images/" + selectedFile.getName();
                imagePathLabel.setText(imagePath);
            }
        });

        grid.add(new Label("Nama:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Kategori:"), 0, 1);
        grid.add(categoryComboBox, 1, 1);
        grid.add(new Label("Deskripsi:"), 0, 2);
        grid.add(descriptionArea, 1, 2);
        grid.add(new Label("Harga:"), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(new Label("Stok:"), 0, 4);
        grid.add(stockField, 1, 4);
        grid.add(new Label("Gambar:"), 0, 5);
        grid.add(imageBox, 1, 5);

        dialog.getDialogPane().setContent(grid);

        // --- PERUBAHAN UTAMA UNTUK VALIDASI ---
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {

                // 1. Validasi semua input tidak boleh kosong
                if (nameField.getText().trim().isEmpty() ||
                    descriptionArea.getText().trim().isEmpty() ||
                    priceField.getText().trim().isEmpty() ||
                    stockField.getText().trim().isEmpty()) {
                    AlertUtil.showInfo("Semua kolom wajib diisi!");
                    return null; // Mencegah dialog tertutup
                }

                // 2. Validasi spesifik untuk kategori
                String category = categoryComboBox.getValue();
                if (category == null || category.trim().isEmpty()) {
                    AlertUtil.showInfo("Kategori wajib dipilih!");
                    return null; // Mencegah dialog tertutup
                }

                try {
                    selectedProduct.setName(nameField.getText());
                    selectedProduct.setCategory(category);
                    selectedProduct.setBrand("");
                    selectedProduct.setDescription(descriptionArea.getText());
                    selectedProduct.setPrice(Double.parseDouble(priceField.getText()));
                    selectedProduct.setStock(Integer.parseInt(stockField.getText()));
                    selectedProduct.setImage(imagePathLabel.getText());
                    return selectedProduct;
                } catch (NumberFormatException e) {
                     AlertUtil.showInfo("Input harga atau stok tidak valid! Harap masukkan angka.");
                     return null;
                }
            }
            return null;
        });
        
        Optional<Product> result = dialog.showAndWait();
        result.ifPresent(updatedProduct -> {
            productService.updateProduct(updatedProduct.getId(), updatedProduct);
            loadSellerProducts();
            AlertUtil.showInfo("Produk berhasil diupdate!");
        });
    }

    //======================================================================
    // 5. METODE LAINNYA
    //======================================================================

    @FXML
    public void deleteProduct() {
        Product selectedProduct = productList.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            AlertUtil.showInfo("Pilih produk yang ingin dihapus.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Anda yakin ingin menghapus produk " + selectedProduct.getName() + "?", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                productService.deleteProduct(selectedProduct.getId());
                loadSellerProducts();
                AlertUtil.showInfo("Produk berhasil dihapus.");
            }
        });
    }

    @FXML
    private void handleLogout() {
        MainApp.currentUser = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("BEAUTRA");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class OrderRow {
        private final String orderId, productName, status;
        private final int qty;
        private final double total;

        public OrderRow(String orderId, String productName, int qty, double total, String status) {
            this.orderId = orderId;
            this.productName = productName;
            this.status = status;
            this.qty = qty;
            this.total = total;
        }

        public String getOrderId() { return orderId; }
        public String getProductName() { return productName; }
        public String getStatus() { return status; }
        public int getQty() { return qty; }
        public double getTotal() { return total; }
    }
}