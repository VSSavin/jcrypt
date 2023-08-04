package com.github.vssavin.jcrypt.osplatform;

import com.github.vssavin.jcrypt.JKeyStorage;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

/**
 * @author vssavin on 03.08.2023
 */
class DefaultPlatformStorage implements JKeyStorage {
    private static final List<String> IGNORED_INTERFACES = new ArrayList<>();

    static {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("defaultPlatformStorage.conf");
            String ignoredString = bundle.getString("ignoredInterfaces");
            String[] splitted = ignoredString.split(",");
            if (splitted.length > 0) {
                for (String str : splitted) {
                    IGNORED_INTERFACES.add(str.trim());
                }
            }

        } catch (MissingResourceException ignore) {
            //ignore
        }
    }

    @Override
    public String getKey() {
        String key;
        try {
            Set<String> ids = getNetworkIds();
            if (!ids.isEmpty()) {
                StringBuilder all = new StringBuilder();
                for (String str: ids) {
                    all.append(str);
                }
                key = all.toString().replace("\u0000", "");
            } else {
                key = "01234567890";
            }
        } catch (Exception e) {
            throw new PlatformInitKeyException("Error while getting HARDWARE ADDRESSES!", e);
        }

        return key;
    }

    @Override
    public String getKey(String id) {
        throw new UnsupportedOperationException("Not implemented!");
    }

    private Set<String> getNetworkIds() throws SocketException {
        Enumeration<NetworkInterface> net = NetworkInterface.getNetworkInterfaces();
        Set<String> ids = new TreeSet<>();

        while (net.hasMoreElements()) {
            NetworkInterface element = net.nextElement();
            if (!isIgnoredInterface(element.getName())) {
                String macAddress = getMacAddress(element);
                if (!macAddress.isEmpty()) {
                    ids.add(macAddress);
                }
            }
        }

        return ids;
    }

    private String getMacAddress(NetworkInterface networkInterface) throws SocketException {
        byte[] mac = networkInterface.getHardwareAddress();
        StringBuilder sb = new StringBuilder();
        if (mac != null) {
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
        }
        return sb.toString();
    }

    private boolean isIgnoredInterface(String name) {
        boolean ignored = false;
        for (String ignoredInterface : IGNORED_INTERFACES) {
            if (name.contains(ignoredInterface)) {
                ignored = true;
                break;
            }
        }

        return ignored;
    }
}
