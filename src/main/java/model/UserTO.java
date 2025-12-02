package model;

import java.io.Serializable;

public class UserTO implements Serializable {
    private String username;
    private String password;
    private String role; 
    private boolean newUser = true;

    public UserTO() {}

    public UserTO(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.newUser = false; 
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // ZMIANA GETTERA I SETTERA
    public boolean isNewUser() { return newUser; } 
    public void setNewUser(boolean newUser) { this.newUser = newUser; }
}