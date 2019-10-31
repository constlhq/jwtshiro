package com.svail.jwtshiro.shiro.filters;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class JwtRoleFilter extends JwtBaseFilter {
  @Override
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws AuthorizationException {
    Subject subject = getSubject(request, response);
    if ((null == subject || !subject.isAuthenticated()) && isJwtSubmission((HttpServletRequest) request)) {
      AuthenticationToken token = createJwtToken((HttpServletRequest) request);
      subject = getSubject(request, response);
      subject.login(token);
      return this.checkRoles(subject,mappedValue);

    }
    return false;
  }
}
