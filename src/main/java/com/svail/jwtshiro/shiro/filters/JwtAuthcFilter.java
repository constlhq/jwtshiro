package com.svail.jwtshiro.shiro.filters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.svail.jwtshiro.shiro.services.JwtAuthService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


public class JwtAuthcFilter extends JwtBaseFilter{

  private ObjectMapper objectMapper;

  private JwtAuthService jwtAuthService;

  public JwtAuthcFilter() {
  }

  public JwtAuthcFilter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws AuthenticationException {
    // 服务器无状态保存，一律失败
    return false;
  }

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws AuthenticationException {
    if(isJwtSubmission((HttpServletRequest) request)){
      AuthenticationToken jwtToken = createJwtToken((HttpServletRequest)request);
       getSubject(request, response).login(jwtToken);
      return true;
    }
    throw new AuthenticationException("String message");
  }

  @Override
  protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
    super.postHandle(request, response);
  }

}
