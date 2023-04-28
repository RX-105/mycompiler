package io.n0sense.mycompiler.i18n;

import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18nTest {
    @Test
    void testI18n() {
        Locale locale_en_US = new Locale("en", "US");
        ResourceBundle bundle = ResourceBundle.getBundle("bundle", locale_en_US);
        System.out.println(bundle.getString("general_help"));
    }
}
