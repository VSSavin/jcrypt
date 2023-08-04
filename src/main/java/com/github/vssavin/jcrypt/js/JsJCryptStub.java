package com.github.vssavin.jcrypt.js;

import com.github.vssavin.jcrypt.StringSafety;

/**
 * @author vssavin on 03.08.2023
 */
public class JsJCryptStub extends JsJCryptEngine {

    public JsJCryptStub(StringSafety stringSafety) {
        super(stringSafety);
    }

    public JsJCryptStub() {
        super();
    }

    @Override
    public String getEncryptMethodName() {
        return "encodeNOSECURE";
    }

    @Override
    public String getDecryptMethodName() {
        return "decodeNOSECURE";
    }
}
