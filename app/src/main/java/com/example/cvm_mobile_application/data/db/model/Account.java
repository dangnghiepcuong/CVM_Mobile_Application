package com.example.cvm_mobile_application.data.db.model;

public class Account {
    private String username;
    private String password;
    private int role;
    private int status;
    private String user_id;

    public Account() {
    }

    public Account(String username, String password, int role, int status, String user_id) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
