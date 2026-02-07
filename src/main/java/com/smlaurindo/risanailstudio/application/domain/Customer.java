package com.smlaurindo.risanailstudio.application.domain;

import java.util.UUID;

public class Customer {
    private String id;
    private String credentialsId;
    private String name;
    private String photo;

    public Customer(String credentialsId) {
        this.id = UUID.randomUUID().toString();
        this.credentialsId = credentialsId;
    }

    public Customer(String id, String credentialsId, String name, String photo) {
        this.id = id;
        this.credentialsId = credentialsId;
        this.name = name;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public String getCredentialsId() {
        return credentialsId;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setName(String name) {
        this.name = name;
    }
}

