package com.svail.jwtshiro.web.controllers;

import org.apache.shiro.authc.AuthenticationException;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ErrorController extends BasicErrorController {

  public ErrorController() {
    super(new DefaultErrorAttributes(), new ErrorProperties());
  }

  @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
    System.out.println("EEEEEEEEEEEE");
    Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
    HttpStatus status = getStatus(request);
    //自定义的错误信息类
    //status.value():错误代码，
    //body.get("message").toString()错误信息
    Map<String, Object> ret =  new HashMap<>();
    ret.put("asdfasdf",new AuthenticationException());
    //TokenException Filter抛出的自定义错误类
    if (!StringUtils.isEmpty((String) body.get("exception")) && body.get("exception").equals(AuthenticationException.class.getName())) {
      body.put("status", HttpStatus.FORBIDDEN.value());
      status = HttpStatus.FORBIDDEN;
    }
    return new ResponseEntity<Map<String, Object>>(ret, status);
  }

  @Override
  public String getErrorPath() {
    return "/error";
  }
}
