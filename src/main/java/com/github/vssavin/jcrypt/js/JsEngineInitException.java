package com.github.vssavin.jcrypt.js;

/**
 * @author vssavin on 03.08.2023
 */
public class JsEngineInitException extends RuntimeException {
    public JsEngineInitException(String message) {
        super(message);
    }

    public JsEngineInitException(String message, Throwable cause) {
        super(message, cause);
    }
}
