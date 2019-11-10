package com.svail.jwtshiro.web.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.svail.jwtshiro.shiro.services.JwtAuthService;
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
import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController {
  @Autowired
  SvailUserService svailUserService;

  @Autowired
  PasswordService passwordService;

  @Autowired
  JwtAuthService jwtAuthService;

  @GetMapping("/index")
  public String index(){
    return "index";
  }


  @ResponseBody
  @PostMapping("/login")
  public String login(@RequestBody Map<String,String> signupMap) throws AuthorizationException {
    String username = signupMap.get("username");
    String password = signupMap.get("password");

      UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username,password);
      SecurityUtils.getSubject().login(usernamePasswordToken);

      String roles = svailUserService.findUserByUsername(username).roles;

    return String.format("{\"token\":\"%s\"}", jwtAuthService.issueJwt(username,roles));
  }

  @ResponseBody
  @GetMapping("/apitest")
  public Object apitest(){
    return new HashMap<String,String>(){{put("status","ok");}};
  }


  @ResponseBody
  @PostMapping("/signup")
  public String signup(@RequestBody Map<String,String> signupMap){

    String username = signupMap.get("username");
    String password = signupMap.get("password");
    SvailUser svailUser = new SvailUser(username,"const.lhq@126.com","13651106676",password,"admin,superuser,coo","geocode,semantic");
    passwordService.encryptPassword(svailUser);
    return "创建用户: "+svailUserService.createUser(svailUser);

  }

}
