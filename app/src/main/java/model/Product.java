package model;

public class Product {
    private String id, name, category, brand, description, image, sellerId;
    private double price;
    private int stock;

    public Product(String id, String name, String category, String brand, String description, double price, int stock, String image, String sellerId) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.image = image;
        this.sellerId = sellerId;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getBrand() { return brand; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getImage() { return image; }
    public String getSellerId() { return sellerId; }
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
    public void setImage(String image) { this.image = image; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId;}
}
