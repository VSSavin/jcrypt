package com.github.vssavin.jcrypt;

/**
 * @author vssavin on 03.08.2023
 */
public interface JKeyStorage {
    String getPublicKey();
    String getPublicKey(String id);
    String getPrivateKey();
    String getPrivateKey(String id);
}
