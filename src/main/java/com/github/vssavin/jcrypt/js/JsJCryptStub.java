package com.github.vssavin.jcrypt.js;

import com.github.vssavin.jcrypt.StringSafety;

/**
 * @author vssavin on 03.08.2023
 */
public class JsJCryptStub extends JsJCryptEngine {

    protected JsJCryptStub(StringSafety stringSafety) {
        super(stringSafety);
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
