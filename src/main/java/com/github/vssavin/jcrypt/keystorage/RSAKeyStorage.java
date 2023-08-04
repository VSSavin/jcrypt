package com.github.vssavin.jcrypt.keystorage;

import com.github.vssavin.jcrypt.JKeyStorage;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author vssavin on 04.08.2023
 */
public class RSAKeyStorage implements JKeyStorage {
    private static final Map<String, KeyParams> cache = new ConcurrentHashMap<>();

    private final KeyPairGenerator keyPairGenerator;

    public RSAKeyStorage() {
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new KeyStorageInitializeException("No such algorithm!", e);
        }
    }

    @Override
    public String getPublicKey() {
        throw new UnsupportedOperationException("Not implemented!");
    }

    @Override
    public String getPublicKey(String id) {
        KeyParams keyParams = cache.get(id);
        String publicKey;
        if (keyParams == null || keyParams.isExpired()) {
            try {
                keyPairGenerator.initialize(2048);
                KeyPair pair = keyPairGenerator.generateKeyPair();
                byte[] publicKeyBytes = pair.getPublic().getEncoded();
                byte[] privateKeyBytes = pair.getPrivate().getEncoded();
                publicKey = Base64.getEncoder().encodeToString(publicKeyBytes);
                String privateKey = Base64.getEncoder().encodeToString(privateKeyBytes);
                keyParams = new KeyParams(id, publicKey, privateKey);
                cache.put(id, keyParams);
            } catch (Exception e) {
                throw new KeyStorageInitKeyException("Getting key error!", e);
            }

        } else {
            publicKey = keyParams.getPublicKey();
        }
        return publicKey;
    }

    @Override
    public String getPrivateKey() {
        throw new UnsupportedOperationException("Not implemented!");
    }

    @Override
    public String getPrivateKey(String id) {
        String publicKey = getPublicKey(id);
        return getPrivateKeyByPublicKey(publicKey);
    }

    private String getPrivateKeyByPublicKey(String publicKey) {
        List<KeyParams> filtered = cache.values().stream()
                .filter(secureParams -> secureParams.getPublicKey().equals(publicKey)).collect(Collectors.toList());
        return !filtered.isEmpty() ? filtered.get(0).getPrivateKey() : "";
    }
}
