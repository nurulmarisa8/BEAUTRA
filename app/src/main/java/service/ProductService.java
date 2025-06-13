package service;

import model.Product;
import util.JsonUtil;
import java.util.List;

public class ProductService {
    private static final String FILE_PATH = "src/main/resources/data/products.json";

    public List<Product> getAllProducts() {
        return JsonUtil.readJson(FILE_PATH, Product.class);
    }

    public void addProduct(Product product) {
        List<Product> products = getAllProducts();
        products.add(product);
        JsonUtil.writeJson(FILE_PATH, products);
    }

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



    public Product getProductById(String productId) {
        List<Product> products = getAllProducts();
        return products.stream()
            .filter(p -> p.getId().equals(productId))
            .findFirst()
            .orElse(null);
    }

    public void updateProduct(String i2, Product product) {
        List<Product> products = getAllProducts();
    for (int i = 0; i < products.size(); i++) {
        if (products.get(i).getId().equals(product.getId())) {
            products.set(i, product); // Update produk
            JsonUtil.writeJson(FILE_PATH, products); // Simpan perubahan ke file
            return;
        }
    }
        
    }

    public List<Product> searchProducts(String keyword) {
        return getAllProducts().stream()
            .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()))
            .toList();
    }

    public List<Product> filterByCategory(String category) {
        return getAllProducts().stream()
            .filter(p -> p.getCategory().equalsIgnoreCase(category))
            .toList();
    }

    
}

    
