package com.crumb.mvc.util;


import java.util.Arrays;

public class StringUtil {

    public static String[] splitUnitsWithSlash(String str) {
        return Arrays.stream(str.split("/"))
                .filter(s -> !s.isEmpty())
                .map(s -> "/" + s)
                .toArray(String[]::new);
    }

    public static String getLastChar(String str) {
        var arr = str.toCharArray();
        return String.valueOf(arr[arr.length - 1]);
    }

    public static String[] getUnits(String str) {
        return Arrays.stream(str.split("/")).filter(s -> !s.isEmpty()).toArray(String[]::new);
    }

}
