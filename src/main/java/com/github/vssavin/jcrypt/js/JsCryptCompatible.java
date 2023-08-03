package com.github.vssavin.jcrypt.js;

import java.util.List;

/**
 * @author vssavin on 03.08.2023
 */
public interface JsCryptCompatible {
    String getEncryptMethodName();
    String getDecryptMethodName();
    List<String> getScriptsList();
}
