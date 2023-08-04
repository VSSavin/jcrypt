package com.github.vssavin.jcrypt.keystorage;

import java.util.Date;

/**
 * @author vssavin on 04.08.2023
 */
class KeyParams {
    private static final int EXPIRATION_KEY_SECONDS = 60;
    private final String id;
    private final String publicKey;
    private final String privateKey;
    private final Date expiration;

    KeyParams(String id, String publicKey) {
        this.id = id;
        this.publicKey = publicKey;
        this.privateKey = publicKey;
        this.expiration = new Date(System.currentTimeMillis() + EXPIRATION_KEY_SECONDS * 1000L);
    }

    KeyParams(String id, String publicKey, String privateKey) {
        this.id = id;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.expiration = new Date(System.currentTimeMillis() + EXPIRATION_KEY_SECONDS * 1000L);
    }

    boolean isExpired() {
        return new Date().after(expiration);
    }

    String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        KeyParams keyParams = (KeyParams) o;

        if (!id.equals(keyParams.id)) {
            return false;
        }
        if (!publicKey.equals(keyParams.publicKey)) {
            return false;
        }
        return privateKey.equals(keyParams.privateKey);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + publicKey.hashCode();
        result = 31 * result + privateKey.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "KeyParams{" +
                "id='" + id + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", expiration=" + expiration +
                '}';
    }
}
