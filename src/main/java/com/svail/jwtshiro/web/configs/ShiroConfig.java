package com.svail.jwtshiro.web.configs;

import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;

@Configuration
public class ShiroConfig {
  @Bean public MethodInvokingFactoryBean methodInvokingFactoryBean(SecurityManager securityManager){
    MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
    methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
    methodInvokingFactoryBean.setArguments(securityManager);
    return methodInvokingFactoryBean;
  }
}
