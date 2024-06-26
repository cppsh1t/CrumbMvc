package com.crumb.mvc.core;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.crumb.definition.BeanDefinition;
import com.crumb.mvc.util.StringUtil;
import com.crumb.mvc.validation.ParameterValidateManager;
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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
public class MapEngine {

    private static Function<Object, String> jsonCaster = JSON::toJSONString;
    private static Function<String, String> stringCaster = str -> {
      var json = new JSONObject();
      json.put("msg", str);
      return json.toString();
    };

    public static void setJsonCaster(Function<Object, String> caster) {
        jsonCaster = caster;
    }

    public static void setStringCaster(Function<String, String> caster) {
        stringCaster = caster;
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
        Object value = null;

        if (param.isAnnotationPresent(RequestParam.class)) {
            var anno = param.getAnnotation(RequestParam.class);
            var name = anno.value();
            var paramMap = url.getParamMap();
            value = paramMap.get(name);
            if (value == null && !anno.required()) value = anno.defaultValue();
        }

        else if (param.isAnnotationPresent(CookieValue.class)) {
            var anno = param.getAnnotation(CookieValue.class);
            var name = anno.value();
            if (req.getCookies() == null) {
                value = anno.required() ? null : anno.defaultValue();
            } else {
                var cookie = Arrays.stream(req.getCookies())
                        .filter(c -> c.getName().equals(name)).findFirst().orElse(null);
                if (cookie == null) {
                    value = anno.required() ? null : anno.defaultValue();
                } else {
                    value = cookie.getValue();
                }
            }

        }

        else if (param.isAnnotationPresent(SessionAttribute.class)) {
            var name = param.getAnnotation(SessionAttribute.class).value();
            value = req.getSession().getAttribute(name);
        }

        else if (param.isAnnotationPresent(PathVariable.class)) {
            var name = param.getAnnotation(PathVariable.class).value();
            var pathMap = url.getPathVarMap();
            value = pathMap.get(name);
        }

        else if (param.isAnnotationPresent(RequestHeader.class)) {
            var name = param.getAnnotation(RequestHeader.class).value();
            value = req.getHeader(name);
        }

        else if (param.getType() == HttpSession.class) {
            value = req.getSession();
        }

        else if (param.getType() == HttpServletRequest.class) {
            value = req;
        }

        else if (param.getType() == HttpServletResponse.class) {
            value = resp;
        }

        if (value != null) {
            value = castString(param, value);
        }

        boolean legal = ParameterValidateManager.validateParameter(param, value);
        return legal ? value : null;
    }

    private static void handleResult(Object result, HttpServletResponse response, String produce) {
        if (result instanceof String str) {
            var jsonStr = stringCaster.apply(str);
            ServletUtil.writeResponse(response, jsonStr, produce);
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

    private static Object castString(Parameter param, Object obj) {
        String str = (String) obj;

        if (param.getType() == int.class || param.getType() == Integer.class) {
            return Integer.parseInt(str);
        }

        if (param.getType() == long.class || param.getType() == Long.class) {
            return Long.parseLong(str);
        }

        if (param.getType() == float.class || param.getType() == Float.class) {
            return Float.parseFloat(str);
        }

        if (param.getType() == double.class || param.getType() == Double.class) {
            return Double.parseDouble(str);
        }

        if (param.getType() == short.class || param.getType() == Short.class) {
            return Short.parseShort(str);
        }

        if (param.getType() == boolean.class || param.getType() == Boolean.class) {
            return Boolean.parseBoolean(str);
        }
        return obj;
    }

}
