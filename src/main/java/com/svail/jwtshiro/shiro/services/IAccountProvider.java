package com.svail.jwtshiro.shiro.services;

import com.svail.jwtshiro.shiro.models.Account;

import java.util.Set;

public interface IAccountProvider {
  Account findUserByUsername(String username);
  Account findUserByEmail(String email);
  Account findUserByCellphone(String cellphone);
  Set<String> findRoles(String username);
  Set<String> findPermissions(String username);

}
