package com.svail.jwtshiro.shiro.models;


import lombok.Data;

@Data
public class Account {
  private String username;
  private String email;
  private String cellphone;
  private String password;
  private String credentialsSalt;

}
