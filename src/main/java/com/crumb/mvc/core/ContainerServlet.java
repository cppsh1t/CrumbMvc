package com.crumb.mvc.core;

import com.crumb.core.Container;
import com.crumb.core.MainContainer;
import com.crumb.definition.BeanDefinition;
import com.crumb.mvc.util.StringUtil;
import com.crumb.web.*;
import com.crumb.mvc.util.ServletUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
public class ContainerServlet extends HttpServlet {

    private Container container;

    @Override
    public void init() throws ServletException {
        super.init();
        container = MainContainer.getContainer();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        log.debug("receive get request which url: {}", req.getRequestURL());
        mappingRequest(req, resp, RequestType.GET);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        log.debug("receive post request which url: {}", req.getRequestURL());
        mappingRequest(req, resp, RequestType.POST);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        log.debug("receive put request which url: {}", req.getRequestURL());
        mappingRequest(req, resp, RequestType.PUT);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        log.debug("receive delete request which url: {}", req.getRequestURL());
        mappingRequest(req, resp, RequestType.DELETE);
    }

    private void mappingRequest(HttpServletRequest req, HttpServletResponse resp, RequestType requestType) {
        var requestAnno = RequestType.getRequestAnno(requestType);
        resp.setCharacterEncoding("UTF-8");
        var originUrl = new EnhancedUrl(req);
        String[] valueSlashUnits = originUrl.getValueUnitsWithSlash();

        var defs = container.getBeanDefinition(d -> this.testBean(d, valueSlashUnits));
        if (defs.length == 0) {
            log.debug("unable to find the corresponding controller");
        } else {
            log.debug("found the corresponding controller: {}", Arrays.toString(defs));
        }

        for(var def : defs) {
            var urlRemainString = MapEngine.getUrlRemainString(def, valueSlashUnits);
            var bean = container.getBean(def.name);
            var methods = Arrays.stream(bean.getClass().getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(requestAnno) || m.isAnnotationPresent(RequestMapping.class))
                    .filter(m -> {
                        var methodParamKeys = RequestType.getRequestAnnoParam(requestAnno, m);
                        var urlParamKeys = originUrl.getParamMap().keySet().toArray(String[]::new);
                        var methodString = RequestType.getRequestAnnoValue(requestAnno, m);
                        return ServletUtil.testUrlString(methodString, urlRemainString, originUrl)
                                && ServletUtil.testParamArray(urlParamKeys, methodParamKeys);
                    }).collect(Collectors.toList());

            log.debug("found the handler function that matches the address mapping: {}", methods);
            for (var method : methods) {
                boolean success = MapEngine.invokeMappingMethod(req, resp, method, bean, originUrl);
                if (success) {
                    log.debug("function: {} process request successfully", method);
                    break;
                } else {
                    log.debug("function: {} failed to process the request", method);
                }
            }

        }
    }


    private boolean testBean(BeanDefinition definition, String[] valueParts) {
        String name = definition.name;
        var nameUnits = StringUtil.splitUnitsWithSlash(name);
        if (valueParts.length < nameUnits.length) return false;

        for(int i = 0; i < nameUnits.length; i++) {
            if (!valueParts[i].equals(nameUnits[i])) return false;
        }
        return true;
    }




}
