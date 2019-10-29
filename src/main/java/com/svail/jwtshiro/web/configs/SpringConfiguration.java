package com.svail.jwtshiro.web.configs;

import com.svail.jwtshiro.shiro.realms.UsernameRealm;
import com.svail.jwtshiro.shiro.services.IAccountProvider;
import org.apache.shiro.realm.AuthorizingRealm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sun.security.krb5.Realm;

@Configuration
public class SpringConfiguration {
  @Bean
  public AuthorizingRealm userNameRealm(IAccountProvider accountProvider){
    UsernameRealm usernameRealm = new UsernameRealm();
    usernameRealm.setAccountProvider();
  }

}
