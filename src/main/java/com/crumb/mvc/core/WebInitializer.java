package com.crumb.mvc.core;


import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;

import java.util.Set;

public class WebInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext ctx) throws ServletException {
        ServletRegistration.Dynamic servlet = ctx.addServlet("servletContainer", new ContainerServlet());
        servlet.addMapping("/");
        servlet.setLoadOnStartup(0);
    }
}
