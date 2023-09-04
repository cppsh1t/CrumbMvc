package com.crumb.mvc.validation;

import com.crumb.mvc.anno.Email;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class EmailValidator extends ParameterValidator{

    private static final String emailRegx = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Override
    Class<? extends Annotation> getTargetAnnoClass() {
        return Email.class;
    }

    @Override
    boolean doValidate(Parameter param, Object value) {
        if (value instanceof CharSequence charSqe) {
            String email = charSqe.toString();
            return email.matches(emailRegx);
        }
        return false;
    }
}
