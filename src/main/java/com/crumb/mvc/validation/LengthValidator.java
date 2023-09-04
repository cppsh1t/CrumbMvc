package com.crumb.mvc.validation;

import com.crumb.mvc.anno.Length;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class LengthValidator extends ParameterValidator {

    @Override
    protected Class<? extends Annotation> getTargetAnnoClass() {
        return Length.class;
    }

    @Override
    boolean doValidate(Parameter param, Object value) {
        if (value instanceof CharSequence charSeq) {
            var anno = param.getAnnotation(Length.class);
            var lengthMin = anno.min();
            var lengthMax = anno.max();
            int valueLength = charSeq.length();

            return valueLength >= lengthMin && valueLength <= lengthMax;
        }

        return false;
    }
}
