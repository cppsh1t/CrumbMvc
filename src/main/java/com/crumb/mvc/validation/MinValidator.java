package com.crumb.mvc.validation;

import com.crumb.mvc.anno.Min;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

@Slf4j
public class MinValidator extends ParameterValidator{
    @Override
    Class<? extends Annotation> getTargetAnnoClass() {
        return Min.class;
    }

    @Override
    boolean doValidate(Parameter param, Object value) {
        var minLength = param.getAnnotation(Min.class).value();
        if (value instanceof Number number) {
            return number.longValue() >= minLength;
        }
        if (value instanceof CharSequence charSeq) {
            return charSeq.length() >= minLength;
        }

        return false;
    }
}
