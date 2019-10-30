package com.svail.jwtshiro.web.configs;

import com.svail.jwtshiro.shiro.realms.UsernameRealm;
import com.svail.jwtshiro.shiro.services.IAccountProvider;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.HashingPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
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
  public SimpleCookie simpleCookie(){
    SimpleCookie simpleCookie = new SimpleCookie("sid");
    simpleCookie.setMaxAge(18000000);
    simpleCookie.setHttpOnly(true);
    return simpleCookie;
  }

  @Bean
  public SessionDAO sessionDAO(){
    MemorySessionDAO sessionDAO = new MemorySessionDAO();
    return sessionDAO;
  }

  @Bean
  public DefaultWebSessionManager defaultWebSessionManager(){
    DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
    // ������session��֤
    sessionManager.setGlobalSessionTimeout(18000000);
    sessionManager.setSessionValidationSchedulerEnabled(false);
    sessionManager.setSessionIdCookie(simpleCookie());
    sessionManager.setSessionDAO(sessionDAO());
    sessionManager.setSessionIdCookieEnabled(true);
    return sessionManager;
  }


  @Bean
  public DefaultWebSecurityManager defaultWebSecurityManager(AuthorizingRealm usernameRealm){

    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
    // ModularRealmAuthenticator Ĭ��ʹ�� AtLeastOneSuccessfulStrategy ����,ֻ��һ�� jwtRealm �����Ĭ�ϼ��ɡ�
    // �Զ��� SubjectFactory��������session
    securityManager.setSessionManager(defaultWebSessionManager());
    securityManager.setRealm(usernameRealm);
    return  securityManager;

  }


//  @Bean
//  public SpringRedisCacheManger springRedisCacheManger(org.springframework.cache.CacheManager cacheManager, RedisTemplate redisTemplate){
//    SpringRedisCacheManger springRedisCacheManger = new SpringRedisCacheManger(cacheManager);
//    springRedisCacheManger.setRedisTemplate(redisTemplate);
//    return springRedisCacheManger;
//  }


  @Bean
  FormAuthenticationFilter formAuthenticationFilter(){
    FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
    formAuthenticationFilter.setUsernameParam("username");
    formAuthenticationFilter.setPasswordParam("password");
    formAuthenticationFilter.setLoginUrl("/login");
    formAuthenticationFilter.setSuccessUrl("/home");
    return formAuthenticationFilter;
  }

  @Bean(name = "shiroFilter")
  public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
    ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
    shiroFilterFactoryBean.setSecurityManager(securityManager);
    shiroFilterFactoryBean.setLoginUrl("/login");
    shiroFilterFactoryBean.setSuccessUrl("/home");
    shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");
    shiroFilterFactoryBean.setFilters(new HashMap<String, Filter>(){{
      put("authc",formAuthenticationFilter());
    }});

    shiroFilterFactoryBean.setFilterChainDefinitionMap(new HashMap<String,String>(){{
      put("/login","authc");
      put("/home","authc");
      put("/index","anon");
      put("/signup","anon");
      put("/unauthorized","anon");
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
