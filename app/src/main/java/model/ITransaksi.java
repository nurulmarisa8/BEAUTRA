package model;

import java.util.List;

public interface ITransaksi {
    String getId();
    List<CartItem> getItems();
    double getTotal();
    String getStatus();
    String getTimestamp();
}
