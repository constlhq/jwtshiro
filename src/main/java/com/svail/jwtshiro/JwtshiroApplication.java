package com.svail.jwtshiro;

import com.svail.jwtshiro.shiro.services.JwtAuthService;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Date;


//import org.postgresql.Driver
@SpringBootApplication
public class JwtshiroApplication {

  public static void main(String[] args) {
    SpringApplication.run(JwtshiroApplication.class, args);
  }

  @Autowired
  HashedCredentialsMatcher hashedCredentialsMatcher;



//  @Autowired
//  DataSource dataSource;
//  @Autowired
//  JwtAuthService jwtAuthService;

//  @Bean
//  public ApplicationRunner applicationRunner(){
//    return (arguments)->{
//      String token = jwtAuthService.issueJwt("lhq","admin,teacher");
//      System.out.println(token);
//      System.out.println(jwtAuthService.decodeJwt(token).getExpiresAt());
//
//    };
//  }


  @Bean ApplicationRunner applicationRunner(){
    return args->{ boolean login =   hashedCredentialsMatcher.doCredentialsMatch(new AuthenticationToken() {
                                                                 @Override
                                                                 public Object getPrincipal() {
                                                                   return "lhq";
                                                                 }

                                                                 @Override
                                                                 public Object getCredentials() {
                                                                   return "123456";
                                                                 }
                                                               },

            new SimpleAuthenticationInfo("lhq", "d4e4b817ef195b90f8c1b854e0960dabe867a89c958ace36f33280f5d84afdb3", ByteSource.Util.bytes("54ce9e415f72eef175d2bdf5ebf9f61a"), "UsernameRealm")
    );

    };
  }

}
