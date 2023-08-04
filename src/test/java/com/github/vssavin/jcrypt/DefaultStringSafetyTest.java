package com.github.vssavin.jcrypt;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author vssavin on 04.08.2023
 */
public class DefaultStringSafetyTest {

    @Test
    public void clearStringTest() {
        StringSafety stringSafety = new DefaultStringSafety();
        String testString1 = "test";
        @SuppressWarnings("StringBufferReplaceableByString")
        String testString2 = new StringBuilder().append(testString1).toString();
        stringSafety.clearString(testString1);
        Assert.assertNotEquals(testString1, testString2);
    }
}
