package com.github.vssavin.jcrypt.keystorage;

/**
 * @author vssavin on 04.08.2023
 */
public class KeyStorageInitializeException extends RuntimeException {

    public KeyStorageInitializeException(String message) {
        super(message);
    }

    public KeyStorageInitializeException(String message, Throwable cause) {
        super(message, cause);
    }
}
