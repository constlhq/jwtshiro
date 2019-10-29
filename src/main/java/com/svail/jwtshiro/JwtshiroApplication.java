package com.svail.jwtshiro;

import com.svail.jwtshiro.shiro.services.JwtAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

//import org.postgresql.Driver
@SpringBootApplication
public class JwtshiroApplication {

  public static void main(String[] args) {
    SpringApplication.run(JwtshiroApplication.class, args);
  }


  @Autowired
  DataSource dataSource;
  @Autowired
  JwtAuthService jwtAuthService;

  @Bean
  public ApplicationRunner applicationRunner(){
    return (arguments)->{
      String token = jwtAuthService.issueJwt("lhq","admin,teacher");
      System.out.println(token);
      System.out.println(jwtAuthService.decodeJwt(token).getExpiresAt());

    };
  }

}
