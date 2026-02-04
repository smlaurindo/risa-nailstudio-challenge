package com.smlaurindo.risanailstudio.shared.utils;

import java.security.SecureRandom;

/**
 * Gerador de Request IDs no formato req_xxxxxxxxxxxx (12 caracteres).
 * Usa caracteres alfanuméricos minúsculos para ser amigável em logs e suporte.
 */
public final class RequestIdGenerator {

    private static final String PREFIX = "req_";
    private static final int ID_LENGTH = 12;
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom RANDOM = new SecureRandom();

    private RequestIdGenerator() {
        // Utility class
    }

    /**
     * Gera um requestId único no formato req_xxxxxxxxxxxx
     */
    public static String generate() {
        StringBuilder sb = new StringBuilder(PREFIX.length() + ID_LENGTH);
        sb.append(PREFIX);

        for (int i = 0; i < ID_LENGTH; i++) {
            int index = RANDOM.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(index));
        }

        return sb.toString();
    }
}

