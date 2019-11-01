package com.svail.jwtshiro.web.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svail.jwtshiro.shiro.filters.JwtAuthcFilter;
import com.svail.jwtshiro.shiro.realms.UsernameRealm;
import com.svail.jwtshiro.shiro.services.IAccountProvider;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.HashingPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.Arrays;
import java.util.HashMap;

@Configuration
public class SpringConfiguration {
  @Bean
  public AuthorizingRealm userNameRealm(IAccountProvider accountProvider){
    UsernameRealm usernameRealm = new UsernameRealm();
    usernameRealm.setAccountProvider(accountProvider);
    usernameRealm.setCachingEnabled(false);
    usernameRealm.setCredentialsMatcher(hashedCredentialsMatcher());
    return usernameRealm;

  }

  @Bean
  public HashedCredentialsMatcher hashedCredentialsMatcher(){
    HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
    hashedCredentialsMatcher.setHashAlgorithmName("SHA-256");
    hashedCredentialsMatcher.setHashIterations(2);
    hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
    return hashedCredentialsMatcher;
  }


//  @Bean
//  public BeanPostProcessor lifecycleBeanPostProcessor() {
//    return new LifecycleBeanPostProcessor();
//  }



//  @Bean
//  public FilterManager filterManager(
//          ShiroAccountProvider accountProvider,
//          ShiroProperties shiroProperties,
//          ShiroAccountProvider shiroAccountProvider,
//          FilterChainConfig filterConfig,
//          CacheDelegator cacheDelegator,
//          MessageConfig messageConfig){
//    FilterManager filterManager = new FilterManager();
//    filterManager.setCacheDelegator(cacheDelegator);
//    filterManager.setAccountProvider(accountProvider);
//    filterManager.setCustomFilters(filterConfig.getFilters());
//    filterManager.setRulesProvider(filterConfig.getShiroFilteRulesProvider());
//    filterManager.setMessages(messageConfig);
//    filterManager.initFilters();
//    filterManager.initFilterChain();
//    return filterManager;
//  }



  @Bean
  public SessionDAO sessionDAO(){
    MemorySessionDAO sessionDAO = new MemorySessionDAO();
    return sessionDAO;
  }

  @Bean
  public DefaultWebSessionManager defaultWebSessionManager(){
    DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
    sessionManager.setSessionValidationSchedulerEnabled(false);
    return sessionManager;
  }

  @Bean
  public SubjectFactory noSessionSubjectFactory(){
    return new DefaultWebSubjectFactory(){
      @Override
      public Subject createSubject(SubjectContext context) {
        context.setSessionCreationEnabled(false);
        return super.createSubject(context);
      }
    };
  }


  @Bean
  public DefaultWebSecurityManager defaultWebSecurityManager(AuthorizingRealm usernameRealm){

    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
    securityManager.setRealm(usernameRealm);
    securityManager.setSubjectFactory(noSessionSubjectFactory());
    securityManager.setSessionManager(defaultWebSessionManager());
    ((DefaultSessionStorageEvaluator)((DefaultSubjectDAO)securityManager.getSubjectDAO()).getSessionStorageEvaluator()).setSessionStorageEnabled(false);
    return  securityManager;

  }

  @Bean
  public JwtAuthcFilter jwtAuthcFilter(ObjectMapper objectMapper){
    return new JwtAuthcFilter(objectMapper);
  }


//  @Bean
//  public SpringRedisCacheManger springRedisCacheManger(org.springframework.cache.CacheManager cacheManager, RedisTemplate redisTemplate){
//    SpringRedisCacheManger springRedisCacheManger = new SpringRedisCacheManger(cacheManager);
//    springRedisCacheManger.setRedisTemplate(redisTemplate);
//    return springRedisCacheManger;
//  }


//  @Bean
//  FormAuthenticationFilter formAuthenticationFilter(){
//    FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
//    formAuthenticationFilter.setUsernameParam("username");
//    formAuthenticationFilter.setPasswordParam("password");
//    formAuthenticationFilter.setLoginUrl("/login");
//    formAuthenticationFilter.setSuccessUrl("/home");
//    return formAuthenticationFilter;
//  }

  @Bean(name = "shiroFilter")
  public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,JwtAuthcFilter jwtAuthcFilter){
    ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
    shiroFilterFactoryBean.setSecurityManager(securityManager);
    shiroFilterFactoryBean.setLoginUrl("/login");
    shiroFilterFactoryBean.setSuccessUrl("/home");
    shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");
    shiroFilterFactoryBean.setFilters(new HashMap<String, Filter>(){{
      put("jwt",jwtAuthcFilter);
    }});

    shiroFilterFactoryBean.setFilterChainDefinitionMap(new HashMap<String,String>(){{
      put("/login","anon");
      put("/home","jwt");
      put("/index","anon");
      put("/signup","anon");
      put("/logout","logout");
//      put("/**", "authc");
    }});

    return  shiroFilterFactoryBean;
  }

  @Bean
  public FilterRegistrationBean shiroFilterBean(){
    FilterRegistrationBean filterRegistrationBean  =  new FilterRegistrationBean();
    DelegatingFilterProxy proxy = new DelegatingFilterProxy();
    proxy.setTargetFilterLifecycle(true);
    proxy.setTargetBeanName("shiroFilter");
    filterRegistrationBean.setFilter(proxy);
    filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST);
    filterRegistrationBean.setAsyncSupported(true);
    filterRegistrationBean.setOrder(0);
    filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
    return filterRegistrationBean;
  }
}
