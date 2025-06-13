package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Kelas utilitas yang menyediakan method-method pembantu untuk membaca dan
 * menulis data ke file dalam format JSON, menggunakan library GSON.
 */
public class JsonUtil {
    // Instance GSON dibuat sekali dan digunakan kembali.
    // .setPrettyPrinting() memastikan file JSON yang ditulis rapi dan mudah dibaca.
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Membaca data dari sebuah file JSON dan mengubahnya menjadi List objek Java.
     * Menggunakan generic <T> agar bisa digunakan untuk berbagai tipe data (User, Product, dll).
     * @param path Lokasi file JSON yang akan dibaca.
     * @param clazz Kelas dari objek yang ada di dalam list (misal: User.class).
     * @param <T> Tipe generik dari objek.
     * @return List yang berisi objek-objek dari file JSON. Mengembalikan list kosong jika file tidak ada atau terjadi error.
     */
    public static <T> List<T> readJson(String path, Class<T> clazz) {
        try (Reader reader = new FileReader(path)) {
            // Membuat tipe data spesifik untuk List<T> (misal: List<User>)
            // agar GSON tahu cara mengubah JSON array menjadi list objek yang benar.
            Type listType = TypeToken.getParameterized(List.class, clazz).getType();
            return gson.fromJson(reader, listType);
        } catch (Exception e) {
            // Jika terjadi error (misal: file tidak ditemukan), kembalikan list kosong
            // untuk mencegah aplikasi crash.
            return new ArrayList<>();
        }
    }

    /**
     * Menulis sebuah List objek Java ke dalam file dengan format JSON.
     * @param path Lokasi file JSON yang akan ditulis.
     * @param data List objek yang akan diubah menjadi JSON.
     * @param <T> Tipe generik dari objek.
     */
    public static <T> void writeJson(String path, List<T> data) {
        try (Writer writer = new FileWriter(path)) {
            // Mengubah list objek Java menjadi string JSON dan menuliskannya ke file.
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}