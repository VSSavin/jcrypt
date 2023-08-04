package com.github.vssavin.jcrypt;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author vssavin on 03.08.2023
 */
public class DefaultStringSafety implements StringSafety {

    @Override
    public void clearString(String str) {
        if (str == null) {
            return;
        }

        try {
            Field stringChars = String.class.getDeclaredField("value");
            setFieldAccessible(stringChars);
            maskStringChars(stringChars, str);
        } catch (Exception e) {
            throw new IllegalStateException("String clearing error!", e);
        }
    }

    private static void setFieldAccessible(Field fieldAccessible) {
        try {
            fieldAccessible.setAccessible(true);
        } catch (Exception inaccessible) {
            String errorMessage = "setAccessible on string chars may throw an InaccessibleObjectException\n" +
                    "for some versions of JVM. To solve this problem add vm option:\n" +
                    "--add-opens java.base/java.lang=ALL-UNNAMED";
            throw new IllegalStateException(errorMessage, inaccessible);
        }
    }

    private static void maskStringChars(Field stringChars, String string) throws IllegalAccessException {
        try {
            char[] chars = (char[]) stringChars.get(string);
            Arrays.fill(chars, '*');
        } catch (ClassCastException castException) {
            byte[] bytes = (byte[]) stringChars.get(string);
            Arrays.fill(bytes, (byte) '*');
        }
    }
}
