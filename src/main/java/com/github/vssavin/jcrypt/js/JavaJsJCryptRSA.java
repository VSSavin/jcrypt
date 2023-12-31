package com.github.vssavin.jcrypt.js;

import com.github.vssavin.jcrypt.DefaultStringSafety;
import com.github.vssavin.jcrypt.StringSafety;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.StringReader;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Collectors;

/**
 * @author vssavin on 03.08.2023
 */
public class JavaJsJCryptRSA extends JsJCryptEngine {
    private static final String ENCRYPT_METHOD_NAME = "encodeRSA";
    private static final String DECRYPT_METHOD_NAME = "decodeRSA";
    private final Cipher rsaCipher;
    private final KeyFactory keyFactory;

    public JavaJsJCryptRSA(StringSafety stringSafety) {
        super(stringSafety);
        addPhantomScript("static/js/jsencrypt.min.js");
        KeyFactory keyFactory1;
        Cipher rsaCipher1;

        try {
            //Using "RSA" algorithm instead of "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING"
            //because there is no way to use it in "jsencrypt.js"
            rsaCipher1 = Cipher.getInstance("RSA");

        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalArgumentException("No such algorithm or padding!", e);
        }

        rsaCipher = rsaCipher1;

        try {
            keyFactory1 = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm!", e);
        }

        keyFactory = keyFactory1;
    }

    public JavaJsJCryptRSA() {
        this(new DefaultStringSafety());
    }

    @Override
    public String encrypt(String message, String key) {
        if (key == null) {
            return message;
        }
        key = normalizeKey(key);

        String encrypted;
        try {
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(key.getBytes()));
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            byte[] encryptedBytes;
            synchronized (rsaCipher) {
                rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
                encryptedBytes = rsaCipher.doFinal(message.getBytes());
            }
            encrypted = Base64.getEncoder().encodeToString(encryptedBytes);
            Arrays.fill(encryptedBytes, (byte) 0);

        } catch (Exception e) {
            throw new JsEncryptionException("Encryption error!", e);
        }

        return encrypted;
    }

    @Override
    public String decrypt(String encrypted, String key) {
        String decrypted;
        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(key.getBytes());
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
            byte[] base64 = Base64.getDecoder().decode(encrypted.getBytes());
            byte[] decryptedBytes;
            synchronized (rsaCipher) {
                rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
                decryptedBytes = rsaCipher.doFinal(base64);
            }
            Arrays.fill(base64, (byte) 0);
            decrypted = new String(decryptedBytes);
            Arrays.fill(decryptedBytes, (byte) 0);
        } catch (InvalidKeySpecException | InvalidKeyException |
                BadPaddingException | IllegalBlockSizeException e) {
            throw new JsDecryptionException("Decryption error!", e);
        }

        return decrypted;
    }

    @Override
    public String getEncryptMethodName() {
        return ENCRYPT_METHOD_NAME;
    }

    @Override
    public String getDecryptMethodName() {
        return DECRYPT_METHOD_NAME;
    }

    private String normalizeKey(String key) {
        if (key != null) {
            BufferedReader reader = new BufferedReader(new StringReader(key));
            key = reader.lines().filter(s ->
                            !s.trim().toLowerCase().startsWith("--") && !s.trim().toLowerCase().startsWith("comment"))
                    .collect(Collectors.joining());
        }

        return key;
    }
}
