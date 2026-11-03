package app.entities;


public class User {
    private int user_id;
    private String email;
    private String password;
    private boolean isAdmin;
    private double balance;

    public User(int user_id, String email, String password, boolean isAdmin, double balance) {
        this.user_id = user_id;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.balance = balance;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public double getBalance() {
        return balance;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
