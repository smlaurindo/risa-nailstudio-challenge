package com.smlaurindo.risanailstudio.application.domain;

public class Customer {
    private String id;
    private String name;
    private String email;
    private String password;
    private String photo;

    public Customer(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Customer(String id, String name, String email, String password, String photo) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.photo = photo;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoto() {
        return photo;
    }
}

