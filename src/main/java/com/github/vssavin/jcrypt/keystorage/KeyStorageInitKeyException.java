package com.github.vssavin.jcrypt.keystorage;

/**
 * @author vssavin on 04.08.2023
 */
public class KeyStorageInitKeyException extends RuntimeException {

    public KeyStorageInitKeyException(String message) {
        super(message);
    }

    public KeyStorageInitKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
