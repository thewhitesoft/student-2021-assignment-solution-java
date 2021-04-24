package com.thewhite.test.util;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Pattern;

public class Util {

    private static final Pattern uuidPattern = Pattern.compile(" [0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");

    public static String getNameWithoutUUID(String name) {
        var matcher = uuidPattern.matcher(name);
        if (!matcher.find()) return name;

        var group = matcher.group(0);
        return name.replace(group, "");
    }

    @SneakyThrows
    public static boolean compareContent(File first, File second) {
        boolean result = true;

        try (BufferedReader firstReader = new BufferedReader(new FileReader(first));
             BufferedReader secondReader = new BufferedReader(new FileReader(second))) {

            int char1;
            int char2;

            while ((char1 = firstReader.read()) != -1 && (char2 = secondReader.read()) != -1) {
                if (char1 != char2) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public static String getBasename(String name) {
        int indexExtensionSeparator = name.indexOf('.');
        return indexExtensionSeparator < 0 ? name : name.substring(0, indexExtensionSeparator);
    }

    public static String getExtension(String name) {
        int indexExtensionSeparator = name.indexOf('.');
        return indexExtensionSeparator < 0 ? "" : name.substring(indexExtensionSeparator);
    }
}
