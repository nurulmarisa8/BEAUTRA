package service;

import model.User;
import util.JsonUtil;

import java.util.List;
import java.util.UUID;

public class AuthService {
    private static final String FILE_PATH = "src/main/resources/data/users.json";

    public User login(String email, String password) {
        List<User> users = JsonUtil.readJson(FILE_PATH, User.class);
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public void register(String fullname, String email, String phone, String password, String gender, String address, String role) {
        List<User> users = JsonUtil.readJson(FILE_PATH, User.class);

        // Cek jika email sudah terdaftar
        boolean exists = users.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
        if (exists) {
            System.out.println("Email sudah digunakan.");
            return;
        }

        String id = "USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        User newUser = new User(id, fullname, email, phone, password, gender, address, role.toLowerCase());
        users.add(newUser);

        JsonUtil.writeJson(FILE_PATH, users);
        System.out.println("Registrasi berhasil untuk " + fullname);
    }

    public List<User> getAllUsers() {
        return JsonUtil.readJson(FILE_PATH, User.class);
    }

    public void deleteUser(int index) {
        List<User> users = getAllUsers();
        users.remove(index);
        JsonUtil.writeJson(FILE_PATH, users);
    }

    public User getUserById(String userId) {
    List<User> users = getAllUsers();
    return users.stream()
            .filter(user -> user.getId().equals(userId))
            .findFirst()
            .orElse(null);
}
}
