package com.crumb.mvc.core;

import com.crumb.mvc.security.FilterBlock;
import com.crumb.mvc.security.FreeBlock;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class MainFilter extends HttpFilter {

    private static final Set<FilterBlock> filterBlocks = new HashSet<>();
    private static final Set<FreeBlock> freeBlocks = new HashSet<>();

    public static void addFilterBlocks(FilterBlock... blocks) {
        filterBlocks.addAll(Arrays.asList(blocks));
    }

    public static void addFreeBlocks(FreeBlock... blocks) {
        freeBlocks.addAll(Arrays.asList(blocks));
    }


    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        var url = new EnhancedUrl(req);
        log.info("get requestUrl: {}", url.getRequestUrl());
        chain.doFilter(req, res);
    }
}
