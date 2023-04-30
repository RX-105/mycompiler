package io.n0sense.mycompiler.util;

import org.junit.jupiter.api.Test;

public class PropertiesUtilTest {
    @Test
    void testGetProperty() {
        String s = PropertiesUtil.getProperty("i18n.locale");
        System.out.println(s);
    }
}
