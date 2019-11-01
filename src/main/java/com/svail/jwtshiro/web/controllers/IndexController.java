package com.svail.jwtshiro.web.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.svail.jwtshiro.web.models.SvailUser;
import com.svail.jwtshiro.web.services.PasswordService;
import com.svail.jwtshiro.web.services.SvailUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
public class IndexController {
  @Autowired
  SvailUserService svailUserService;

  @Autowired
  PasswordService passwordService;

  @GetMapping("/index")
  public String index(){
    return "index";
  }

  @PostMapping("/login")
  public String login(@RequestBody Map<String,String> signupMap) throws AuthorizationException {
    String username = signupMap.get("username");
    String password = signupMap.get("password");

      UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username,password);
      SecurityUtils.getSubject().login(usernamePasswordToken);

    return "登陆成功";
  }


  @GetMapping("/home")
  public String home(){
    return "home";
  }


  @PostMapping("/signup")
  public String signup(@RequestBody Map<String,String> signupMap){

    String username = signupMap.get("username");
    String password = signupMap.get("password");
    SvailUser svailUser = new SvailUser(username,"const.lhq@126.com","13651106676",password,"admin,superuser,coo","geocode,semantic");
    passwordService.encryptPassword(svailUser);
    return "创建用户: "+svailUserService.createUser(svailUser);

  }

}
