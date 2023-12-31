package com.crumb.mvc.core;

import com.alibaba.fastjson2.JSON;
import com.crumb.definition.BeanDefinition;
import com.crumb.mvc.util.StringUtil;
import com.crumb.util.ReflectUtil;
import com.crumb.web.*;
import com.crumb.mvc.util.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
public class MapEngine {

    public static Function<Object, String> jsonCaster = JSON::toJSONString;

    public static void setJsonCaster(Function<Object, String> caster) {
        jsonCaster = caster;
    }

    public static String getUrlRemainString(BeanDefinition def, String[] valueUnits) {
        var name = def.name;
        var nameUnits = StringUtil.splitUnitsWithSlash(name);
        int remainLength = valueUnits.length - nameUnits.length;
        if (remainLength == 0) {
            return "/";
        } else {
            var remainParts = Arrays.copyOfRange(valueUnits, valueUnits.length - remainLength, valueUnits.length);
            return String.join("", remainParts);
        }
    }

    public static boolean invokeMappingMethod(HttpServletRequest req, HttpServletResponse resp,
                                           Method method, Object target, EnhancedUrl url) {
        var produce = RequestType.getRequestAnnoProduce(method);
        if (method.getParameterCount() == 0) {
            var result = ReflectUtil.invokeMethod(method, target);
            handleResult(result, resp, produce);
        }

        else {
            var paramObjArr = method.getParameters();
            var paramArr = Arrays.stream(paramObjArr).map(p -> injectParam(p, req, resp, url)).toArray();
            boolean hasNull = Arrays.stream(paramArr).anyMatch(Objects::isNull);
            if (hasNull) return false;
            var result = ReflectUtil.invokeMethod(method, target, paramArr);
            handleResult(result, resp, produce);
        }
        return true;


    }

    private static Object injectParam(Parameter param, HttpServletRequest req, HttpServletResponse resp, EnhancedUrl url) {
        if (param.isAnnotationPresent(RequestParam.class)) {
            var anno = param.getAnnotation(RequestParam.class);
            var name = anno.value();
            var paramMap = url.getParamMap();
            var value = paramMap.get(name);
            if (value == null && !anno.required()) value = anno.defaultValue();
            return value;
        }

        if (param.isAnnotationPresent(CookieValue.class)) {
            var anno = param.getAnnotation(CookieValue.class);
            var name = anno.value();
            if (req.getCookies() == null) {
                return anno.required() ? null : anno.defaultValue();
            }

            var cookie = Arrays.stream(req.getCookies())
                    .filter(c -> c.getName().equals(name)).findFirst().orElse(null);
            if (cookie == null) {
                return anno.required() ? null : anno.defaultValue();
            } else {
                return cookie.getValue();
            }
        }

        if (param.isAnnotationPresent(SessionAttribute.class)) {
            var name = param.getAnnotation(SessionAttribute.class).value();
            return req.getSession().getAttribute(name);
        }

        if (param.isAnnotationPresent(PathVariable.class)) {
            var name = param.getAnnotation(PathVariable.class).value();
            var pathMap = url.getPathVarMap();
            return pathMap.get(name);
        }

        if (param.isAnnotationPresent(RequestHeader.class)) {
            var name = param.getAnnotation(RequestHeader.class).value();
            return req.getHeader(name);
        }

        if (param.getType() == HttpSession.class) {
            return  req.getSession();
        }

        if (param.getType() == HttpServletRequest.class) {
            return req;
        }

        if (param.getType() == HttpServletResponse.class) {
            return resp;
        }

        return null;
    }

    private static void handleResult(Object result, HttpServletResponse response, String produce) {
        if (result instanceof String str) {
            ServletUtil.writeResponse(response, str, produce);
        } else if (result instanceof InputStream inputStream) {
            try {
                if (!produce.isEmpty()) response.setContentType(produce);
                var outputStream = response.getOutputStream();
                IOUtils.copy(inputStream, outputStream);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        else {
            var str = jsonCaster.apply(result);
            ServletUtil.writeResponse(response, str, produce);
        }
    }

}
