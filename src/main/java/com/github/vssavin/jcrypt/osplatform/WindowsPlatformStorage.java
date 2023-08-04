package com.github.vssavin.jcrypt.osplatform;

import com.github.vssavin.jcrypt.JKeyStorage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author vssavin on 03.08.2023
 */
class WindowsPlatformStorage implements JKeyStorage {
    private static final String DEFAULT_KEY = "01234567890";
    private String driveLetter = "C";

    public WindowsPlatformStorage(String driveLetter) {
        this.driveLetter = driveLetter;
    }

    public WindowsPlatformStorage() {
    }

    @Override
    public String getPublicKey() {
        String key = DEFAULT_KEY;
        try {
            String line;
            Process process = Runtime.getRuntime()
                    .exec("cmd /c chcp 65001" + " && cmd /c vol " + driveLetter + ":");
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = in.readLine()) != null) {
                if (line.toLowerCase().contains("serial number")) {
                    String[] strings = line.split(" ");
                    key = strings[strings.length - 1];
                }
            }
            in.close();
        } catch (Exception e) {
            throw new PlatformInitException("Error while getting WindowsSecurity!", e);
        }
        return key;
    }

    @Override
    public String getPublicKey(String id) {
        throw new UnsupportedOperationException("Not implemented!");
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
