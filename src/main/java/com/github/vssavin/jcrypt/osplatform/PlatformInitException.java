package com.github.vssavin.jcrypt.osplatform;

/**
 * @author vssavin on 03.08.2023
 */
public class PlatformInitException extends RuntimeException {

    public PlatformInitException(String message) {
        super(message);
    }

    public PlatformInitException(String message, Throwable cause) {
        super(message, cause);
    }
}
