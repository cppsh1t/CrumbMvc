package com.crumb.mvc.util;


import java.util.Arrays;

public class StringUtil {

    public static String[] splitUnitsWithSlash(String str) {
        return Arrays.stream(str.split("/"))
                .filter(s -> !s.isEmpty())
                .map(s -> "/" + s)
                .toArray(String[]::new);
    }


}
