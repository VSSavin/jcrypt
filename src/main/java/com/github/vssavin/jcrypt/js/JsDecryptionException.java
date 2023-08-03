package com.github.vssavin.jcrypt.js;

/**
 * @author vssavin on 03.08.2023
 */
public class JsDecryptionException extends RuntimeException {
    public JsDecryptionException(String message) {
        super(message);
    }

    public JsDecryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
