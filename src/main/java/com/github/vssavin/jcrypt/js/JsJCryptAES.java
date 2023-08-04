package com.github.vssavin.jcrypt.js;

import com.github.vssavin.jcrypt.DefaultStringSafety;
import com.github.vssavin.jcrypt.StringSafety;

import java.util.List;

/**
 * @author vssavin on 03.08.2023
 */
public class JsJCryptAES extends JsJCryptEngine {

    public JsJCryptAES(StringSafety stringSafety) {
        super(stringSafety);
    }

    public JsJCryptAES() {
        this(new DefaultStringSafety());
    }

    @Override
    public String getEncryptMethodName() {
        return "encodeAES";
    }

    @Override
    public String getDecryptMethodName() {
        return "decodeAES";
    }

    @Override
    protected List<String> getJavaScriptsListFromResources() {
        List<String> list = super.getJavaScriptsListFromResources();
        list.add("static/js/AES.js");
        return list;
    }
}
