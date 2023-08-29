package com.crumb.mvc.core;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class EnhancedUrl {

    private final String requestUrl;
    private final String queryString;
    private Map<String, String> pathVarMap = new ConcurrentHashMap<>();

    public EnhancedUrl(HttpServletRequest request) {
        this.requestUrl = request.getRequestURL().toString();
        this.queryString = request.getQueryString();
    }

    public String[] getValueUnitsWithSlash() {
        String[] killHead = requestUrl.split("//")[1].split("/");
        return Arrays.stream(Arrays.copyOfRange(killHead, 1, killHead.length))
                .map(s -> "/" + s).toArray(String[]::new);
    }

    public String getValueString() {
        String[] killHead = requestUrl.split("//")[1].split("/");
        return Arrays.stream(Arrays.copyOfRange(killHead, 1, killHead.length))
                .map(s -> "/" + s).collect(Collectors.joining());
    }

    public Map<String, String> getParamMap() {
        return ParamMap.makeMap(queryString);
    }


    public Map<String, String> getPathVarMap() {
        return pathVarMap;
    }

    public void addPathVariable(String key, String value) {
        pathVarMap.put(key,value);
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public String getQueryString() {
        return queryString;
    }

    public boolean hasQueryString() {
        return queryString != null;
    }

    @Override
    public String toString() {
        return "requestUrl: " + requestUrl + ", queryString: " + queryString;
    }
}
