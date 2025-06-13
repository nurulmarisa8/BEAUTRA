package model;

import java.util.List;

public class Order implements ITransaksi {
    
    private String id;
    private String buyerId; 
    private List<CartItem> items;
    private double total;
    private String status;
    private String timestamp;
    private String paymentMethod;
    
    public Order() {}
    
    public Order(String id, String buyerId, List<CartItem> items, double total, String status, String timestamp, String paymentMethod) {
        this.id = id;
        this.buyerId = buyerId;
        this.items = items;
        this.total = total;
        this.status = status;
        this.timestamp = timestamp;
        this.paymentMethod = paymentMethod;
    }

    // --- METHOD DARI INTERFACE ITRANSAKSI ---

    @Override
    public String getId() {
        return id;
    }

    @Override
    public List<CartItem> getItems() {
        // Method ini sekarang ada kembali
        return items;
    }

    @Override
    public double getTotal() {
        return total;
    }
    
    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getTimestamp() {
        return timestamp;
    }
    
    // --- GETTER & SETTER LAINNYA ---

    public String getBuyerId() {
        return buyerId;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}