package model;

import java.util.List;

/**
 * Interface yang mendefinisikan sebuah "kontrak" untuk kelas-kelas transaksi.
 * Setiap kelas yang mengimplementasikan ITransaksi dijamin memiliki method-method
 * di bawah ini, sehingga memungkinkan penanganan objek transaksi yang seragam.
 */
public interface ITransaksi {
    /**
     * Mengembalikan ID unik dari transaksi.
     * @return String ID transaksi.
     */
    String getId();

    /**
     * Mengembalikan daftar item yang ada dalam transaksi.
     * @return List dari CartItem.
     */
    List<CartItem> getItems();

    /**
     * Mengembalikan total nilai moneter dari transaksi.
     * @return double total transaksi.
     */
    double getTotal();

    /**
     * Mengembalikan status saat ini dari transaksi (misal: "paid", "pending").
     * @return String status transaksi.
     */
    String getStatus();

    /**
     * Mengembalikan waktu kapan transaksi dibuat, dalam format String.
     * @return String timestamp transaksi.
     */
    String getTimestamp();
}