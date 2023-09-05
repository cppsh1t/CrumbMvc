package com;

import org.junit.jupiter.api.Test;



public class MainTest {


    @Test
    public void test() {
        var clazz = Foo.class;
        var method = clazz.getDeclaredMethods()[0];
        var param = method.getParameters()[0];
        System.out.println(param.getParameterizedType().getTypeName());
    }
}
