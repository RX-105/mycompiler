package io.n0sense.mycompiler.util;

import java.util.Locale;

public class I18nUtil {
    public static Locale getLocale() {
        String s = PropertiesUtil.getProperty("i18n.locale", "en_US");
        String[] arr = s.split("_");
        if (arr.length == 2)
            return new Locale(arr[0], arr[1]);
        else
            return Locale.US;
    }
}
