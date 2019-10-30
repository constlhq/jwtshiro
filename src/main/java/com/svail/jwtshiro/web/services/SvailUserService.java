package com.svail.jwtshiro.web.services;

import com.svail.jwtshiro.shiro.models.Account;
import com.svail.jwtshiro.shiro.services.IAccountProvider;
import com.svail.jwtshiro.web.mappers.SvailUserMapper;
import com.svail.jwtshiro.web.models.SvailUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SvailUserService implements IAccountProvider {

  @Autowired
  SvailUserMapper svailUserMapper;

  @Override
  public SvailUser findUserByUsername(String username) {
   return svailUserMapper.findUserByUsername(username);
  }

  @Override
  public SvailUser findUserByEmail(String email) {
    return svailUserMapper.findUserByEmail(email);
  }

  @Override
  public SvailUser findUserByCellphone(String cellphone) {
    return svailUserMapper.findUserByCellphone(cellphone);
  }

  @Override
  public Set<String> findRoles(String username) {
    return Arrays.stream(svailUserMapper.findRoles(username).split(",")).collect(Collectors.toSet());
  }

  @Override
  public Set<String> findPermissions(String username) {
    return Arrays.stream(svailUserMapper.findPermissions(username).split(",")).collect(Collectors.toSet());
  }

  public int createUser(SvailUser svailUser){
    return  svailUserMapper.insertUser(svailUser);
  }

}
