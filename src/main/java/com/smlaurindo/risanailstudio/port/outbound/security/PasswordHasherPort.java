package com.smlaurindo.risanailstudio.port.outbound.security;

public interface PasswordHasherPort {
    String hash(String rawPassword);
    boolean matches(String rawPassword, String hashedPassword);
}
