package com.crumb.mvc.validation;

import com.crumb.mvc.anno.NotEmpty;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class NotEmptyValidator extends ParameterValidator{

    @Override
    Class<? extends Annotation> getTargetAnnoClass() {
        return NotEmpty.class;
    }

    @Override
    boolean doValidate(Parameter param, Object value) {
        if (value instanceof CharSequence charSeq) {
            return !charSeq.isEmpty();
        }
        return false;
    }
}
