package com.github.vssavin.jcrypt.osplatform;

/**
 * @author vssavin on 03.08.2023
 */
public class PlatformEncryptionException extends RuntimeException {

    public PlatformEncryptionException(String message) {
        super(message);
    }

    public PlatformEncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
