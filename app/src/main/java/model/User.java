package model;

public class User {
    private String id;
    private String fullname;
    private String email;
    private String phone;
    private String password;
    private String gender;
    private String address;
    private String role;

    public User(String id, String fullname, String email, String phone, String password, String gender, String address, String role) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.address = address;
        this.role = role;
    }

    public String getId() { return id; }
    public String getFullname() { return fullname; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
    public String getRole() { return role; }


    public void setId(String id) { this.id = id; }
}

    

