package com.github.vssavin.jcrypt.osplatform;

import com.github.vssavin.jcrypt.JCrypt;
import com.github.vssavin.jcrypt.JKeyStorage;
import org.junit.Assert;
import org.junit.Test;

public class OSPlatformCryptTest {

    @Test
    public void defaultPlatformStorageTest() {
        String message = "test message";
        JKeyStorage platformStorage = new DefaultPlatformStorage();
        String key = platformStorage.getPublicKey();
        JCrypt storageSecure = new OSPlatformCrypt();
        String encrypted = storageSecure.encrypt(message, key);
        String decrypted = storageSecure.decrypt(encrypted, key);

        Assert.assertEquals(message, decrypted);
    }

    @Test
    public void windowsPlatformStorageTest() {
        String message = "test message";
        JKeyStorage platformStorage;
        String key;
        try {
            platformStorage = new WindowsPlatformStorage();
            key = platformStorage.getPublicKey();
        } catch (PlatformInitException e) {
            platformStorage = new DefaultPlatformStorage();
            key = platformStorage.getPublicKey();
        }

        JCrypt storageSecure = new OSPlatformCrypt();
        String encrypted = storageSecure.encrypt(message, key);
        String decrypted = storageSecure.decrypt(encrypted, key);

        Assert.assertEquals(message, decrypted);
    }

    @Test
    public void linuxPlatformStorageTest() {
        String message = "test message";
        JKeyStorage platformStorage;
        String key;
        try {
            platformStorage = new LinuxPlatformStorage();
            key = platformStorage.getPublicKey();
        } catch (PlatformInitException e) {
            platformStorage = new DefaultPlatformStorage();
            key = platformStorage.getPublicKey();
        }

        JCrypt storageSecure = new OSPlatformCrypt();
        String encrypted = storageSecure.encrypt(message, key);
        String decrypted = storageSecure.decrypt(encrypted, key);

        Assert.assertEquals(message, decrypted);
    }

}
