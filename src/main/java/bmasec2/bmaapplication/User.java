package bmasec2.bmaapplication;

import java.util.Date;
import java.util.Map;


public abstract class User {

    protected String userId;
    protected String name;
    protected String email;
    protected String role;
    protected String password;
    protected Date lastLogin;
    protected String status;

    public User(String userId, String name, String email, String role, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
        this.status = "Active";
        this.lastLogin = new Date();
    }



    public boolean login(String username, String password) {
        if ((this.name.equals(username) || this.email.equals(username)) && this.password.equals(password)) {
            this.lastLogin = new Date();
            System.out.println(this.name + " logged in successfully.");
            return true;
        }
        return false;
    }


    public void logout() {
        System.out.println(this.name + " has logged out.");

    }


    public boolean updateProfile(Map<String, String> updateData) {
        if (updateData.containsKey("name")) {
            this.name = updateData.get("name");
        }
        if (updateData.containsKey("email")) {
            this.email = updateData.get("email");
        }
        System.out.println("Profile updated for " + this.name);
        return true;
    }


    public void resetPassword() {

        this.password = "defaultPassword123";
        System.out.println("Password has been reset for " + this.name);
    }


    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getRole() { return role; }
}
