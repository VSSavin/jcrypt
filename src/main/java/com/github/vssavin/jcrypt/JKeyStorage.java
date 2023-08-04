package com.github.vssavin.jcrypt;

/**
 * @author vssavin on 03.08.2023
 */
public interface JKeyStorage {
    String getKey();
    String getKey(String id);
}
