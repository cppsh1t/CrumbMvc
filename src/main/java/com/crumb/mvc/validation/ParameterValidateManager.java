package com.crumb.mvc.validation;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class ParameterValidateManager {

    private static final List<ParameterValidator> validators = new ArrayList<>();

    static {
        validators.add(new ParamLengthValidator());
    }

    public static boolean validateParameter(Parameter param, Object value) {
        for(var validator : validators) {
            if (value == null) return false;
            if (!validator.validate(param, value)) return false;
        }
        return true;
    }
}
