package com.svail.jwtshiro.shiro.filters;

import com.svail.jwtshiro.shiro.token.JwtToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.stream.Stream;

public abstract class JwtBaseFilter  extends AccessControlFilter {

  protected boolean isJwtSubmission(@NotNull HttpServletRequest request) {
    String jwt  = request.getHeader("Authorization");
    return StringUtils.hasText(jwt);
  }

  protected AuthenticationToken createJwtToken(@NotNull HttpServletRequest request) {
    String host = request.getRemoteHost();
    String jwt  = request.getHeader("Authorization");
    return new JwtToken(host,jwt);
  }


  protected boolean checkRoles(Subject subject, Object mappedValue){
    String[] rolesArray = (String[]) mappedValue;
    if (rolesArray == null || rolesArray.length == 0) {
      return true;
    }
    return Stream.of(rolesArray).anyMatch(subject::hasRole);
  }

  protected boolean checkPerms(Subject subject, Object mappedValue){
    String[] perms = (String[]) mappedValue;
    boolean isPermitted = true;
    if (perms != null && perms.length > 0) {
      if (perms.length == 1) {
        if (!subject.isPermitted(perms[0])) {
          isPermitted = false;
        }
      } else {
        if (!subject.isPermittedAll(perms)) {
          isPermitted = false;
        }
      }
    }
    return isPermitted;
  }

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
    Subject subject = getSubject(request, response);
    if (null == subject || !subject.isAuthenticated()) {
      //未认证
      throw new AuthenticationException();
    } else {
      // 无权限
      throw new AuthorizationException();
    }
  }

}
