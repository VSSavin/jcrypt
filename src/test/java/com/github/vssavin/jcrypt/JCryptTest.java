package com.github.vssavin.jcrypt;

import com.github.vssavin.jcrypt.js.JavaJsJCryptRSA;
import com.github.vssavin.jcrypt.js.JsJCryptAES;
import com.github.vssavin.jcrypt.js.JsJCryptStub;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

/**
 * @author vssavin on 02.08.2023
 */
public class JCryptTest {

    @Test
    public void aesEncryptDecryptTest() {
        JCrypt aes = new JsJCryptAES(str -> {
        });

        String sourceMessage = "Test message";
        String key = UUID.randomUUID().toString().replace("-", "");
        String encrypted = aes.encrypt(sourceMessage, key);
        Assert.assertNotEquals(encrypted, sourceMessage);

        String decrypted = aes.decrypt(encrypted, key);
        Assert.assertNotEquals(encrypted, decrypted);
        Assert.assertEquals(sourceMessage, decrypted);
    }

    @Test
    public void rsaEncryptDecryptTest() throws NoSuchAlgorithmException {
        JCrypt rsa = new JavaJsJCryptRSA(str -> {
        });

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair pair = keyPairGenerator.generateKeyPair();
        byte[] publicKeyBytes = pair.getPublic().getEncoded();
        byte[] privateKeyBytes = pair.getPrivate().getEncoded();

        String sourceMessage = "Test message";
        String key = Base64.getEncoder().encodeToString(publicKeyBytes);
        String encrypted = rsa.encrypt(sourceMessage, key);
        Assert.assertNotEquals(encrypted, sourceMessage);

        key = Base64.getEncoder().encodeToString(privateKeyBytes);
        String decrypted = rsa.decrypt(encrypted, key);
        Assert.assertNotEquals(encrypted, decrypted);
        Assert.assertEquals(sourceMessage, decrypted);
    }

    @Test
    public void stubEncryptDecryptTest() {
        JCrypt stub = new JsJCryptStub(str -> {
        });

        String sourceMessage = "Test message";
        String key = UUID.randomUUID().toString().replace("-", "");
        String encrypted = stub.encrypt(sourceMessage, key);
        Assert.assertEquals(encrypted, sourceMessage);

        String decrypted = stub.decrypt(encrypted, key);
        Assert.assertEquals(encrypted, decrypted);
        Assert.assertEquals(sourceMessage, decrypted);
    }
}