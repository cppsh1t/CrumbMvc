package com.crumb.mvc.validation;

import com.crumb.mvc.anno.Pattern;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class PatternValidator extends ParameterValidator{

    @Override
    Class<? extends Annotation> getTargetAnnoClass() {
        return Pattern.class;
    }

    @Override
    boolean doValidate(Parameter param, Object value) {
        if (value instanceof CharSequence charSeq) {
            var regx = param.getAnnotation(Pattern.class).value();
            var str = charSeq.toString();
            return str.matches(regx);
        }
        return false;
    }
}
