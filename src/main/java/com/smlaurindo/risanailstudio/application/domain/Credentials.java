package com.smlaurindo.risanailstudio.application.domain;

import java.util.UUID;

public class Credentials {
    private String id;
    private String email;
    private String passwordHash;
    private Role role;

    public Credentials(String email, String passwordHash, Role role) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public Credentials(String id, String email, String passwordHash, Role role) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }
}
