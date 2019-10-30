package com.svail.jwtshiro.web.models;

import com.svail.jwtshiro.shiro.models.Account;

public class SvailUser extends Account {
  public SvailUser(String username,String email,String cellphone,String password,String roles,String permissions) {
    this.username = username;
    this.email = email;
    this.cellphone = cellphone;
    this.password = password;
    this.roles = roles;
    this.permissions = permissions;
  }
}
