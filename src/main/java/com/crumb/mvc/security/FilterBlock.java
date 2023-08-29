package com.crumb.mvc.security;


import com.crumb.mvc.util.StringUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;


public class FilterBlock {

    private final List<String> urlPatterns = new ArrayList<>();
    private final List<String> passRoles = new ArrayList<>();
    private Function<HttpServletRequest, UserDetail> func;

    public void addUrlPatterns(String... patterns) {
        urlPatterns.addAll(Arrays.asList(patterns));
    }

    public void addPassRoles(String... roles) {
        passRoles.addAll(Arrays.asList(roles));
    }

    public boolean checkUrl(HttpServletRequest request, String urlString) {
        for(var pattern : urlPatterns) {
            if (pattern.equals(urlString)) {
                return checkAuth(request);
            }

            if (StringUtil.getLastChar(pattern).equals("*") && urlString.length() >= pattern.length()) {
                var patternCharArr = pattern.toCharArray();
                var aheadPattern = String.valueOf(Arrays.copyOfRange(patternCharArr, 0, patternCharArr.length - 2));
                var urlCharArr = urlString.toCharArray();
                var aheadUrl = String.valueOf(Arrays.copyOfRange(urlCharArr, 0, patternCharArr.length - 2));
                if (aheadPattern.equals(aheadUrl)) return checkAuth(request);
            }

        }
        return true;
    }

    protected boolean checkAuth(HttpServletRequest request) {
        var userDetail = func.apply(request);
        if (userDetail == null) return false;
        return checkRole(userDetail.getRole());
    }

    protected boolean checkRole(String other) {
        for(var role : passRoles) {
            if (other.equals(role)) return true;
        }
        return false;
    }

    public void setAuthFunc(Function<HttpServletRequest, UserDetail> func) {
        this.func = func;
    }

}
