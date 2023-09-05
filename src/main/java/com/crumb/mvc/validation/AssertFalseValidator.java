package com.crumb.mvc.validation;

import com.crumb.mvc.anno.AssertFalse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class AssertFalseValidator extends ParameterValidator{

    @Override
    protected Class<? extends Annotation> getTargetAnnoClass() {
        return AssertFalse.class;
    }

    @Override
    boolean doValidate(Parameter param, Object value) {
        if (value instanceof Boolean bValue) {
            return !bValue;
        }

        boolean castValue = Boolean.parseBoolean(value.toString());
        return !castValue;
    }
}
