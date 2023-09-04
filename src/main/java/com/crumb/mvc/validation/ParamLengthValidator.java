package com.crumb.mvc.validation;

import com.crumb.mvc.anno.Length;

import java.lang.reflect.Parameter;

public class ParamLengthValidator implements ParameterValidator {


    @Override
    public boolean validate(Parameter param, Object value) {
        if (!param.isAnnotationPresent(Length.class)) return true;

        var length = param.getAnnotation(Length.class).value();
        String valueString = value.toString();
        return length > valueString.length();
    }
}
