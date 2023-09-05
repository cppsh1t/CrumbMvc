package com.crumb.mvc.validation;

import com.crumb.mvc.anno.NotBlank;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class NotBlankValidator extends ParameterValidator{

    @Override
    Class<? extends Annotation> getTargetAnnoClass() {
        return NotBlank.class;
    }

    @Override
    boolean doValidate(Parameter param, Object value) {
        if (value instanceof CharSequence charSeq) {
            var str = charSeq.toString();
            return !str.isBlank();
        }
        return false;
     }
}
