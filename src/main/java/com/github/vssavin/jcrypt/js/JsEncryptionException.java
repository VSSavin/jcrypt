package com.github.vssavin.jcrypt.js;

/**
 * @author vssavin on 03.08.2023
 */
public class JsEncryptionException extends RuntimeException {
    public JsEncryptionException(String message) {
        super(message);
    }

    public JsEncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
