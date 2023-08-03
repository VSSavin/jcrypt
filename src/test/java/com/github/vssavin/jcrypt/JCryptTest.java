package com.github.vssavin.jcrypt;

import com.github.vssavin.jcrypt.js.JsJCryptAES;
import com.github.vssavin.jcrypt.js.JsJCryptStub;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.UUID;

/**
 * @author vssavin on 02.08.2023
 */
public class JCryptTest {
    @Test
    public void aesEncryptDecryptTest(){
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
    public void stubEncryptDecryptTest(){
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