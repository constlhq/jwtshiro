package com.svail.jwtshiro.web;

import org.apache.shiro.authc.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// �������쳣֪ͨ�࣬ͳһ���ش����쳣
@RestControllerAdvice
public class SvailControllerAdvice {
  @ExceptionHandler(AuthenticationException.class)
  public String authenticationException(){
    return "��֤ʧ��";
  }

}
