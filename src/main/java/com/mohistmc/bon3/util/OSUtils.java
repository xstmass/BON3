//MrPyro 2019
package com.mohistmc.bon3.util;

import java.util.Locale;

public class OSUtils {
    private static final String OSProp = System.getProperty("os.name").toLowerCase(Locale.ROOT);
    private static OS current = null;

    public static OS getOS() {
        if (current == null) {
            if (OSProp.contains("linux")) {
                current = OS.Linux;
            } else if (OSProp.contains("mac")) {
                current = OS.Mac;
            } else {
                current = OS.Windows;
            }
        }
        return current;
    }

    public enum OS {
        Windows,
        Mac,
        Linux
    }
}
