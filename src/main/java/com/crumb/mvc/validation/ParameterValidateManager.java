package com.crumb.mvc.validation;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class ParameterValidateManager {

    private static final List<ParameterValidator> validators = new ArrayList<>();

    static {
        validators.add(new LengthValidator());
        validators.add(new AssertTrueValidator());
        validators.add(new AssertFalseValidator());
        validators.add(new EmailValidator());
        validators.add(new PatternValidator());
        validators.add(new NotBlankValidator());
        validators.add(new MinValidator());
        validators.add(new MaxValidator());
        validators.add(new NotEmptyValidator());
        validators.add(new RangeValidator());
    }

    public static boolean validateParameter(Parameter param, Object value) {
        for(var validator : validators) {
            if (value == null) return false;
            if (!validator.validate(param, value)) return false;
        }
        return true;
    }
}
