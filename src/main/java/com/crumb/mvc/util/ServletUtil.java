package com.crumb.mvc.util;

import com.crumb.mvc.core.EnhancedUrl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;


@Slf4j
public class ServletUtil {
    private static final String regx = "\\{[a-zA-Z0-9]+\\}";


    public static void writeResponse(HttpServletResponse response, String str) {
        try {
            response.getWriter().write(str);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static boolean testParamArray(String[] urlMapString, String[] methodMapString) {
        if (methodMapString.length == 0) return true;
        for (var str : methodMapString) {
            var other = (Arrays.stream(urlMapString).filter(s -> s.equals(str))).findFirst().orElse(null);
            if (other == null) return false;
        }
        return true;
    }

    public static boolean testUrlString(String methodString, String urlRemainString, EnhancedUrl url) {
        if (methodString.equals(urlRemainString)) return true;

        String[] methodUnits = StringUtil.getUnits(methodString);
        String[] urlUnits = StringUtil.getUnits(urlRemainString);
        if (methodUnits.length != urlUnits.length) return false;

        for (int i = 0; i < methodUnits.length; i++) {
            if (methodUnits[i].matches(regx)) {
                var key = methodUnits[i].substring(1, methodUnits[i].length() - 1);
                var value = urlUnits[i];
                url.addPathVariable(key, value);
            } else if (!methodUnits[i].equals(urlUnits[i])) {
                return false;
            }
        }
        return true;
    }

}
