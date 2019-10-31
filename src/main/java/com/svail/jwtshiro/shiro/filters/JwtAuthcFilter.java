package com.svail.jwtshiro.shiro.filters;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class JwtAuthcFilter extends JwtBaseFilter{
  @Override
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws AuthenticationException {
    Subject subject = getSubject(request,response);
    return subject!= null && subject.isAuthenticated();
  }

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws AuthenticationException {
    if(isJwtSubmission((HttpServletRequest) request)){
      AuthenticationToken token = createJwtToken((HttpServletRequest)request);
      Subject subject = getSubject(request, response);
      subject.login(token);
      return true;
    }
    return false;
  }
}
