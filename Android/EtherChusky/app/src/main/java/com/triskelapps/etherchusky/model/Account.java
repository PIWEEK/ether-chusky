package com.triskelapps.etherchusky.model;

/**
 * Created by julio on 17/12/15.
 */
public class Account {

    private int id;
    private String name;
    private String email;
    private String password;
    private String address;

    public Account(String username, String pass) {
        this.name = username;
        this.password = pass;
    }

    public Account(String username, String email, String pass) {

        this.name = username;
        this.email = email;
        this.password = pass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public String toString() {
        return name;
    }
}
