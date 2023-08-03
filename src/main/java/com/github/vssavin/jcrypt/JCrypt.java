package com.github.vssavin.jcrypt;

/**
 * @author vssavin on 03.08.2023
 */
public interface JCrypt {
    String encrypt(String message, String key);
    String decrypt(String encrypted, String key);
}
