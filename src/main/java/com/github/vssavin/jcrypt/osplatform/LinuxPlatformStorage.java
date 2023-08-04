package com.github.vssavin.jcrypt.osplatform;

import com.github.vssavin.jcrypt.JKeyStorage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author vssavin on 03.08.2023
 */
class LinuxPlatformStorage implements JKeyStorage {
    private static final String DEFAULT_KEY = "01234567890";

    @Override
    public String getKey() {
        String key = DEFAULT_KEY;
        try {
            String line;
            Process process = Runtime.getRuntime()
                    .exec("udevadm info --query=all --name=/dev/sda | grep ID_SERIAL_SHORT");
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = in.readLine()) != null) {
                if (line.toUpperCase().contains("ID_SERIAL_SHORT")) {
                    String[] strings = line.split("=");
                    key = strings[1];
                    break;
                }
            }
            in.close();
        } catch (Exception e) {
            throw new PlatformInitKeyException("Error while getting WindowsSecurity!", e);
        }
        return key;
    }

    @Override
    public String getKey(String id) {
        throw new UnsupportedOperationException("Not implemented!");
    }
}
