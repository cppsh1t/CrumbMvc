package com.crumb.mvc.validation;


import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public abstract class ParameterValidator {


    abstract Class<? extends Annotation> getTargetAnnoClass();

    abstract boolean doValidate(Parameter param, Object value);

    public final boolean validate(Parameter param, Object value) {
        if (!param.isAnnotationPresent(getTargetAnnoClass())) return true;
        return doValidate(param, value);
    }

}
