package com.svail.jwtshiro.shiro.models;


import lombok.Data;

@Data
public class Account {
  public String username;
  public String email;
  public String cellphone;
  public String password;
  public String roles;
  public String permissions;
  public String credentialsSalt;

}
