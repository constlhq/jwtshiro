package com.svail.jwtshiro.shiro.utils;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.springframework.stereotype.Component;

@Component
public class NoSessionSubjectFactory extends DefaultWebSubjectFactory {

  @Override
  public Subject createSubject(SubjectContext context) {
    context.setSessionCreationEnabled(false);
    return super.createSubject(context);
  }
}
