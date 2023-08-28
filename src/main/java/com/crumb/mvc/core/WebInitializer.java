package com.crumb.mvc.core;


import jakarta.servlet.*;

import java.util.Set;

public class WebInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext ctx) throws ServletException {
        ServletRegistration.Dynamic servlet = ctx.addServlet("servletContainer", new ContainerServlet());
        servlet.addMapping("/");
        servlet.setLoadOnStartup(0);

        FilterRegistration.Dynamic filter = ctx.addFilter("mainFilter", new MainFilter());
        filter.addMappingForUrlPatterns(null, true, "/*");
    }
}
