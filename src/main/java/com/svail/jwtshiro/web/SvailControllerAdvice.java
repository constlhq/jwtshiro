package com.svail.jwtshiro.web;

import org.apache.shiro.authc.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

// 控制器异常通知类，统一拦截处理异常
@RestControllerAdvice
public class SvailControllerAdvice {
  @ExceptionHandler(AuthenticationException.class)
  public Object authenticationException(){
    return new HashMap<String,String>(){{
      put("status","failed");
      put("msg","AuthenticationException");
    }};
  }

}
