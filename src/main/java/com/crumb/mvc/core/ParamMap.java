package com.crumb.mvc.core;

import java.util.HashMap;
import java.util.Map;

public class ParamMap {

    public static Map<String, String> makeMap(String input) {
        Map<String, String> resultMap = new HashMap<>();

        if (input == null || input.isEmpty()) {
            return resultMap;
        }

        String[] pairs = input.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];
                resultMap.put(key, value);
            } else {
                resultMap.put(pair, null);
            }
        }

        return resultMap;
    }
}
