package com.github.vssavin.jcrypt.keystorage;

import com.github.vssavin.jcrypt.JKeyStorage;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vssavin on 04.08.2023
 */
public class AESKeyStorage implements JKeyStorage {
    private static final int DEFAULT_EXPIRATION_KEY_SECONDS = 60;
    private static final Map<String, KeyParams> cache = new ConcurrentHashMap<>();

    private final int expirationKeySeconds;

    public AESKeyStorage() {
        this.expirationKeySeconds = DEFAULT_EXPIRATION_KEY_SECONDS;
    }

    public AESKeyStorage(int expirationKeySeconds) {
        this.expirationKeySeconds = expirationKeySeconds;
    }

    @Override
    public String getPublicKey() {
        throw new UnsupportedOperationException("Not implemented!");
    }

    @Override
    public String getPublicKey(String id) {
        KeyParams keyParams = cache.get(id);
        String uuid;
        if (keyParams == null || keyParams.isExpired()) {
            uuid = UUID.randomUUID().toString().replace("-", "");
            keyParams = new KeyParams(id, uuid, expirationKeySeconds);
            cache.put(id, keyParams);
        } else {
            uuid = keyParams.getPublicKey();
        }
        return uuid;
    }

    @Override
    public String getPrivateKey() {
        return getPublicKey();
    }

    @Override
    public String getPrivateKey(String id) {
        return getPublicKey(id);
    }
}
