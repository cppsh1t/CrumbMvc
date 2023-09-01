package com.crumb.mvc.core;

import com.crumb.web.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public enum RequestType {
    GET,
    POST,
    PUT,
    DELETE;

    public static Class<? extends Annotation> getRequestAnno(RequestType requestType) {
        return switch (requestType) {
            case GET -> GetMapping.class;
            case POST -> PostMapping.class;
            case PUT -> PutMapping.class;
            case DELETE -> DeleteMapping.class;
        };
    }

    public static String[] getRequestAnnoParam(Class<? extends Annotation> clazz, Method method) {
        String[] params = null;
        if (clazz == GetMapping.class) {
            var anno = method.getAnnotation(GetMapping.class);
            if (anno != null) params = anno.params();
        } else if (clazz == PostMapping.class) {
            var anno = method.getAnnotation(PostMapping.class);
            if (anno != null) params = anno.params();
        } else if (clazz == DeleteMapping.class) {
            var anno = method.getAnnotation(DeleteMapping.class);
            if (anno != null) params = anno.params();
        } else if (clazz == PutMapping.class){
            var anno = method.getAnnotation(PutMapping.class);
            if (anno != null) params = anno.params();
        }

        return params != null ? params : method.getAnnotation(RequestMapping.class).params();
    }

    public static String getRequestAnnoValue(Class<? extends Annotation> clazz, Method method) {
        String value = null;

        if (clazz == GetMapping.class) {
            var anno = method.getAnnotation(GetMapping.class);
            if (anno != null) value = anno.value();
        } else if (clazz == PostMapping.class) {
            var anno = method.getAnnotation(PostMapping.class);
            if (anno != null) value = anno.value();
        } else if (clazz == DeleteMapping.class) {
            var anno = method.getAnnotation(DeleteMapping.class);
            if (anno != null) value = anno.value();
        } else if (clazz == PutMapping.class){
            var anno = method.getAnnotation(PutMapping.class);
            if (anno != null) value = anno.value();
        }

        return value != null ? value : method.getAnnotation(RequestMapping.class).value();
    }

    public static String getRequestAnnoProduce(Method method) {
        String produce = null;

        if (method.isAnnotationPresent(GetMapping.class)) {
            var anno = method.getAnnotation(GetMapping.class);
            if (anno != null) produce = anno.produces();
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            var anno = method.getAnnotation(PostMapping.class);
            if (anno != null) produce = anno.produces();
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            var anno = method.getAnnotation(DeleteMapping.class);
            if (anno != null) produce = anno.produces();
        } else if (method.isAnnotationPresent(PutMapping.class)){
            var anno = method.getAnnotation(PutMapping.class);
            if (anno != null) produce = anno.produces();
        }

        return produce != null ? produce : method.getAnnotation(RequestMapping.class).produces();
    }

}
