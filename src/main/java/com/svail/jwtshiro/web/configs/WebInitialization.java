package com.svail.jwtshiro.web.configs;


import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.*;
import java.util.EnumSet;


public class WebInitialization extends AbstractAnnotationConfigDispatcherServletInitializer {
  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class[]{SpringConfiguration.class};
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    return null;
  }

  @Override
  protected String[] getServletMappings() {
    return new String[]{"/"};
  }

  @Override
  public void onStartup(ServletContext container) throws ServletException {

    //config shiro filter
    FilterRegistration.Dynamic filterRegistration = container.addFilter("shiroFilter", DelegatingFilterProxy.class);
    filterRegistration.setInitParameter("targetFilterLifecycle", "true");
    filterRegistration.addMappingForUrlPatterns(
            EnumSet.of(DispatcherType.REQUEST,DispatcherType.FORWARD),
            false, "/*");//≈‰÷√mapping

    super.onStartup(container);
  }
}
