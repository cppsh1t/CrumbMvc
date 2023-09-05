package com.crumb.mvc.validation;

import com.crumb.mvc.anno.Max;
import com.crumb.mvc.anno.Min;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class MaxValidator extends ParameterValidator{

    @Override
    Class<? extends Annotation> getTargetAnnoClass() {
        return Max.class;
    }

    @Override
    boolean doValidate(Parameter param, Object value) {
        var maxLength = param.getAnnotation(Max.class).value();
        if (value instanceof Number number) {
            return number.longValue() <= maxLength;
        }
        if (value instanceof CharSequence charSeq) {
            return charSeq.length() <= maxLength;
        }

        return false;
    }
}
