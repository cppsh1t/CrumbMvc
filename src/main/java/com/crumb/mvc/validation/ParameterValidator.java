package com.crumb.mvc.validation;


import java.lang.reflect.Parameter;

public interface ParameterValidator {

    boolean validate(Parameter param, Object value);

}
