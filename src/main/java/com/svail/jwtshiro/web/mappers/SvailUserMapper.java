package com.svail.jwtshiro.web.mappers;

import com.svail.jwtshiro.web.models.SvailUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SvailUserMapper {
  int insertUser(SvailUser svailUser);
  SvailUser findUserByUsername(String username);
  SvailUser findUserByEmail(String email);
  SvailUser findUserByCellphone(String cellphone);
  String findRoles(String username);
  String findPermissions(String username);
}
