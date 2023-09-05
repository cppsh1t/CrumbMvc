package com.crumb.mvc.validation;

import com.crumb.mvc.anno.AssertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class AssertTrueValidator extends ParameterValidator {


    @Override
    Class<? extends Annotation> getTargetAnnoClass() {
        return AssertTrue.class;
    }

    @Override
    boolean doValidate(Parameter param, Object value) {
        if (value instanceof Boolean bValue) {
            return bValue;
        }

        return Boolean.parseBoolean(value.toString());
    }
}
