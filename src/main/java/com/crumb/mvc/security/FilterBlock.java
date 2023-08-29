package com.crumb.mvc.security;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterBlock {

    private final List<String> urlPatterns = new ArrayList<>();
    private final List<String> passRoles = new ArrayList<>();

    public void addUrlPatterns(String... patterns) {
        urlPatterns.addAll(Arrays.asList(patterns));
    }

    public void addPassRoles(String... roles) {
        passRoles.addAll(Arrays.asList(roles));
    }

    public boolean checkUrl(HttpServletRequest request, String[] urlUnits) {
        return false;
    }

}
