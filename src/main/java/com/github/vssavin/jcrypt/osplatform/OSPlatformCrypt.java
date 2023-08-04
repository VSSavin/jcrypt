package com.github.vssavin.jcrypt.osplatform;

import com.github.vssavin.jcrypt.JCrypt;
import com.github.vssavin.jcrypt.JKeyStorage;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author vssavin on 03.08.2023
 */
public class OSPlatformCrypt implements JCrypt {
    private  static final int GCM_IV_LENGTH = 12;
    private final SecureRandom secureRandom = new SecureRandom();

    private SecretKeySpec secretKey;

    private static final String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";

    public OSPlatformCrypt() {
        prepare();
    }

    @Override
    public String decrypt(String encoded) {
        return decode(encoded);
    }

    @Override
    public String encrypt(String message) {
        return encode(message);
    }

    @Override
    public String decrypt(String encoded, String key) {
        return decode(encoded, key);
    }

    @Override
    public String encrypt(String message, String key) {
        return encode(message, key);
    }

    private void prepare() {
        JKeyStorage platformStorage = PlatformStorageInitializer.getPlatformStorage();
        String key = platformStorage.getPublicKey();
        if (key.isEmpty()) {
            String message = String.format("Error while getting platform security key [%s]", platformStorage);
            throw new IllegalStateException(message);
        } else {
            setKey(key);
        }
    }

    private void setKey(String myKey) {
        try {
            if (myKey.length() < 16) {
                StringBuilder builder = new StringBuilder(myKey);
                while (builder.length() < 16) {
                    builder.append("0");
                }
                myKey = builder.toString();
            }

            byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            synchronized (sha) {
                key = sha.digest(key);
            }

            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            throw new PlatformInitKeyException("Setting key error!", e);
        }
    }

    private String encode(String strToEncrypt) {
        String result;
        try {
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            byte[] iv = new byte[GCM_IV_LENGTH];
            secureRandom.nextBytes(iv);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
            synchronized (cipher) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
                byte[] cipherText = cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8));
                ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
                byteBuffer.put(iv);
                byteBuffer.put(cipherText);
                result = Base64.getEncoder().encodeToString(byteBuffer.array());
            }
        } catch (Exception e) {
            throw new PlatformEncryptionException("Encryption error!", e);
        }
        return result;
    }

    private String decode(String strToDecrypt) {
        String result;
        try {
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            byte[] base64Decoded = Base64.getDecoder().decode(strToDecrypt.getBytes(StandardCharsets.ISO_8859_1));
            AlgorithmParameterSpec gcmIv = new GCMParameterSpec(128, base64Decoded, 0, GCM_IV_LENGTH);
            synchronized (cipher) {
                cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmIv);
                result = new String(cipher.doFinal(base64Decoded, GCM_IV_LENGTH,
                        base64Decoded.length - GCM_IV_LENGTH));
            }
        } catch (Exception e) {
            throw new PlatformDecryptionException("Decryption error!", e);
        }
        return result;
    }

    private String encode(String strToEncrypt, String key) {
        String result;
        try {
            setKey(key);
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            byte[] iv = new byte[GCM_IV_LENGTH];
            secureRandom.nextBytes(iv);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
            synchronized (cipher) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
                byte[] cipherText = cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8));
                ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
                byteBuffer.put(iv);
                byteBuffer.put(cipherText);
                result = Base64.getEncoder().encodeToString(byteBuffer.array());
            }
        } catch (Exception e) {
            throw new PlatformEncryptionException("Encryption error!", e);
        }
        return result;
    }

    private String decode(String strToDecrypt, String key) {
        String result;
        try {
            setKey(key);
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            byte[] base64Decoded = Base64.getDecoder().decode(strToDecrypt.getBytes(StandardCharsets.ISO_8859_1));
            AlgorithmParameterSpec gcmIv = new GCMParameterSpec(128, base64Decoded, 0, GCM_IV_LENGTH);

            synchronized (cipher) {
                cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmIv);
                result = new String(cipher.doFinal(base64Decoded, GCM_IV_LENGTH,
                        base64Decoded.length - GCM_IV_LENGTH));
            }
        } catch (Exception e) {
            throw new PlatformDecryptionException("Decryption error!", e);
        }
        return result;
    }
}
