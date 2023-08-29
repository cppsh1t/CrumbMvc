package com.crumb.mvc.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FreeBlock {
    private final List<String> passPatterns = new ArrayList<>();

    public void addPassPatterns(String... patterns) {
        passPatterns.addAll(Arrays.asList(patterns));
    }

    public boolean check(String[] urlUnits) {
        return false;
    }

}
