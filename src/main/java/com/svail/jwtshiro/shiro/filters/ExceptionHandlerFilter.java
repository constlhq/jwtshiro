package com.svail.jwtshiro.shiro.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.springframework.http.HttpStatus;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class ExceptionHandlerFilter extends OncePerRequestFilter {


  @Override
  protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      // custom error response class used across my project
//      ErrorResponse errorResponse = new ErrorResponse(e);
      ((HttpServletResponse)response).setStatus(HttpStatus.OK.value());
      response.getWriter().write(convertObjectToJson(
              new HashMap<String,String>(){{
                put("stauts","failed");
                put("msg",e.getMessage());
              }}
      ));
    }
  }


  public String convertObjectToJson(Object object) throws JsonProcessingException {
    if (object == null) {
      return null;
    }
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(object);
  }
}
