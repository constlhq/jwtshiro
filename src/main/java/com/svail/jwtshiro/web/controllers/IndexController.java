package com.svail.jwtshiro.web.controllers;

import com.svail.jwtshiro.web.models.SvailUser;
import com.svail.jwtshiro.web.services.PasswordService;
import com.svail.jwtshiro.web.services.SvailUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Map;

@Controller
public class IndexController {
  @Autowired
  SvailUserService svailUserService;

  @Autowired
  PasswordService passwordService;

  @GetMapping("/index")
  public String index(){
    return "index";
  }

  @GetMapping("/login")
  public String login(){
    return "login";
  }


  @GetMapping("/home")
  public String home(){
    return "home";
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
