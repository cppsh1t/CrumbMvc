package com.crumb.mvc.core;

import com.crumb.core.MainContainer;
import com.crumb.exception.BeanNotFoundException;
import com.crumb.mvc.security.FilterBlock;
import com.crumb.mvc.security.FilterBlockHolder;
import com.crumb.mvc.security.FreeBlock;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;

@Slf4j
public class MainFilter extends HttpFilter {


    private FreeBlock freeBlock;

    private final Set<FilterBlock> filterBlocks = new HashSet<>();

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            var filterBlock = MainContainer.getContainer().getBean(FilterBlock.class);
            filterBlocks.add(filterBlock);
        } catch (BeanNotFoundException e) {
            //do noting
        }

        try {
            var filterBlocksHolder = MainContainer.getContainer().getBean(FilterBlockHolder.class);
            filterBlocks.addAll(filterBlocksHolder.getFilterBlocks());
        } catch (BeanNotFoundException e) {
            //do noting
        }

        try {
            freeBlock = MainContainer.getContainer().getBean(FreeBlock.class);
        } catch (BeanNotFoundException e) {
            //do noting
        }

    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        var url = new EnhancedUrl(req);
        var str = url.getValueString();
        boolean free = false;

        if (freeBlock != null) {
            free = freeBlock.check(str);
        }


        if (!free) {
            for (var filterBlock : filterBlocks) {
                if (!filterBlock.checkUrl(req, str)) {
                    return;
                }
            }
        }
        chain.doFilter(req, res);

    }


}
