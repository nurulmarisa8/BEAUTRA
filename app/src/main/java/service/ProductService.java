package service;

import model.Product;
import util.JsonUtil;
import java.util.List;

/**
 * Kelas service yang menangani semua logika bisnis terkait Produk.
 * Ini mencakup operasi dasar CRUD (Create, Read, Update, Delete)
 * serta fungsionalitas pencarian dan penyaringan.
 */
public class ProductService {
    private static final String FILE_PATH = "src/main/resources/data/products.json";

    /**
     * Mengambil daftar semua produk dari file JSON.
     * @return List dari semua objek Product.
     */
    public List<Product> getAllProducts() {
        return JsonUtil.readJson(FILE_PATH, Product.class);
    }

    /**
     * Menambahkan satu produk baru ke dalam file JSON.
     * @param product Objek produk yang akan ditambahkan.
     */
    public void addProduct(Product product) {
        List<Product> products = getAllProducts();
        products.add(product);
        JsonUtil.writeJson(FILE_PATH, products);
    }

    /**
     * Menghapus produk berdasarkan ID-nya.
     * @param selectedProduct ID dari produk yang akan dihapus.
     */
    public void deleteProduct(String selectedProduct) {
        List<Product> products = getAllProducts();
        Product productToDelete = products.stream()
                .filter(product -> product.getId().equals(selectedProduct))
                .findFirst()
                .orElse(null);

        if (productToDelete != null) {
            products.remove(productToDelete);
            JsonUtil.writeJson(FILE_PATH, products);  
        } else {
            System.out.println("Product not found.");
        }
    }

    /**
     * Mencari dan mengembalikan satu produk berdasarkan ID uniknya.
     * @param productId ID produk yang ingin dicari.
     * @return Objek Product jika ditemukan, atau null jika tidak.
     */
    public Product getProductById(String productId) {
        List<Product> products = getAllProducts();
        return products.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Memperbarui data produk yang sudah ada.
     * Method ini mencari produk berdasarkan ID dan menggantinya dengan data baru.
     * @param product Objek produk dengan data yang sudah diperbarui.
     */
    public void updateProduct(Product product) {
        List<Product> products = getAllProducts();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(product.getId())) {
                products.set(i, product); // Update produk
                JsonUtil.writeJson(FILE_PATH, products); // Simpan perubahan ke file
                return;
            }
        }
    }
}