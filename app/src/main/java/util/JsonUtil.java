package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static <T> List<T> readJson(String path, Class<T> clazz) {
        try (Reader reader = new FileReader(path)) {
            Type listType = TypeToken.getParameterized(List.class, clazz).getType();
            return gson.fromJson(reader, listType);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static <T> void writeJson(String path, List<T> data) {
        try (Writer writer = new FileWriter(path)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
