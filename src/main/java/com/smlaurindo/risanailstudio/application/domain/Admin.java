package com.smlaurindo.risanailstudio.application.domain;

import java.util.UUID;

public class Admin {
    private String id;
    private String credentialsId;
    private String name;

    public Admin(String credentialsId) {
        this.id = UUID.randomUUID().toString();
        this.credentialsId = credentialsId;
    }

    public Admin(String id, String credentialsId, String name) {
        this.id = id;
        this.credentialsId = credentialsId;
        this.name = name;
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

    public void setName(String name) {
        this.name = name;
    }
}
