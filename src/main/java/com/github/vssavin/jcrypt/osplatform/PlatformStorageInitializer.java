package com.github.vssavin.jcrypt.osplatform;

import com.github.vssavin.jcrypt.JKeyStorage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author vssavin on 03.08.2023
 */
public final class PlatformStorageInitializer {
    private static final String WINDOWS_DRIVE_LETTER = "C";

    private PlatformStorageInitializer() {

    }

    public static JKeyStorage getPlatformStorage() {
        JKeyStorage platformSecurity;
        String usePlatformProperty = System.getProperty("security.usePlatform");
        if (usePlatformProperty != null) {
            platformSecurity = getPlatformStorage(usePlatformProperty);
        } else {
            String systemName = System.getProperty("os.name").toLowerCase();
            if (systemName.contains("lin")) {
                platformSecurity = new LinuxPlatformStorage();
            } else if (systemName.contains("win")) {
                platformSecurity = new WindowsPlatformStorage(WINDOWS_DRIVE_LETTER);
            } else {
                platformSecurity = new DefaultPlatformStorage();
            }
        }

        if (!(platformSecurity instanceof DefaultPlatformStorage) && platformSecurity.getPublicKey().isEmpty()) {
            platformSecurity = new DefaultPlatformStorage();
        }

        return platformSecurity;
    }

    private static JKeyStorage getPlatformStorage(String name) {
        try {
            List<Class<?>> classes = getClasses();
            for (Class<?> clazz : classes) {
                if (clazz.getSimpleName().toLowerCase().contains(name)) {
                    return createNewInstance(clazz);
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new PlatformInitException("Platform definition error!", e);
        }

        return new DefaultPlatformStorage();
    }

    private static JKeyStorage createNewInstance(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            return (JKeyStorage) constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            throw new PlatformInitException(
                    String.format("Creating new instance of class %s error!", clazz.getSimpleName()));
        }
    }

    private static List<Class<?>> getClasses()
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String packageName = PlatformStorageInitializer.class.getPackage().getName();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    assert !file.getName().contains(".");
                    classes.addAll(findClasses(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                }
            }
        }

        return classes;
    }
}
