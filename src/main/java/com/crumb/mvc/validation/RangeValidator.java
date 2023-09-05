package com.crumb.mvc.validation;

import com.crumb.mvc.anno.Range;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class RangeValidator extends ParameterValidator{

    @Override
    Class<? extends Annotation> getTargetAnnoClass() {
        return Range.class;
    }

    @Override
    boolean doValidate(Parameter param, Object value) {
        var anno = param.getAnnotation(Range.class);
        var from = anno.from();
        var to = anno.to();
        if (value instanceof CharSequence charSeq) {
            var length = charSeq.length();
            return from <= length && length <= to;
        }

        if (value instanceof Number number) {
            var size = number.longValue();
            return from <= size && size <= to;
        }
        return false;
    }
}
