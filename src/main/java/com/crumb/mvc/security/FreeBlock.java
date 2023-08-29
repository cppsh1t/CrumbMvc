package com.crumb.mvc.security;

import com.crumb.mvc.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FreeBlock {
    private final List<String> passPatterns = new ArrayList<>();

    public void addPassPatterns(String... patterns) {
        passPatterns.addAll(Arrays.asList(patterns));
    }

    public boolean check(String urlString) {
        for(var pattern : passPatterns) {
            if (pattern.equals(urlString)) {
                return true;
            }

            if (StringUtil.getLastChar(pattern).equals("*") && urlString.length() >= pattern.length()) {
                var patternCharArr = pattern.toCharArray();
                var aheadPattern = String.valueOf(Arrays.copyOfRange(patternCharArr, 0, patternCharArr.length - 2));
                var urlCharArr = urlString.toCharArray();
                var aheadUrl = String.valueOf(Arrays.copyOfRange(urlCharArr, 0, patternCharArr.length - 2));
                if (aheadPattern.equals(aheadUrl)) return true;
            }

        }
        return false;
    }

}
