package com.github.vssavin.jcrypt;

import com.github.vssavin.jcrypt.js.JavaJsJCryptRSA;
import com.github.vssavin.jcrypt.js.JsJCryptAES;
import com.github.vssavin.jcrypt.js.JsJCryptStub;
import com.github.vssavin.jcrypt.keystorage.AESKeyStorage;
import com.github.vssavin.jcrypt.keystorage.RSAKeyStorage;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

/**
 * @author vssavin on 04.08.2023
 */
public class JCryptTest {

    @Test
    public void aesShouldEqualsDecryptedAndSourceMessage() {
        JCrypt aes = new JsJCryptAES(str -> {
        });

        JKeyStorage keyStorage = new AESKeyStorage();

        String sourceMessage = "Test message";
        String publicKey = keyStorage.getPublicKey("id");
        String encrypted = aes.encrypt(sourceMessage, publicKey);
        Assert.assertNotEquals(encrypted, sourceMessage);

        String privateKey = keyStorage.getPrivateKey("id");
        String decrypted = aes.decrypt(encrypted, privateKey);
        Assert.assertNotEquals(encrypted, decrypted);
        Assert.assertEquals(sourceMessage, decrypted);
    }

    @Test
    public void rsaShouldEqualsDecryptedAndSourceMessage() {
        JCrypt rsa = new JavaJsJCryptRSA(str -> {
        });

        JKeyStorage keyStorage = new RSAKeyStorage();

        String sourceMessage = "Test message";
        String publicKey = keyStorage.getPublicKey("id");
        String encrypted = rsa.encrypt(sourceMessage, publicKey);
        Assert.assertNotEquals(encrypted, sourceMessage);

        String privateKey = keyStorage.getPrivateKey("id");
        String decrypted = rsa.decrypt(encrypted, privateKey);
        Assert.assertNotEquals(encrypted, decrypted);
        Assert.assertEquals(sourceMessage, decrypted);
    }

    @Test
    public void stubShouldEqualsEncryptedAndSourceMessage() {
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