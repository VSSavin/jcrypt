package com.github.vssavin.jcrypt.osplatform;

/**
 * @author vssavin on 03.08.2023
 */
public class PlatformInitKeyException extends RuntimeException {

    public PlatformInitKeyException(String message) {
        super(message);
    }

    public PlatformInitKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
