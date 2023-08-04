package com.github.vssavin.jcrypt.osplatform;

/**
 * @author vssavin on 03.08.2023
 */
public class PlatformDecryptionException extends RuntimeException {

    public PlatformDecryptionException(String message) {
        super(message);
    }

    public PlatformDecryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
