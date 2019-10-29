package com.svail.jwtshiro.shiro.realms;

import com.svail.jwtshiro.shiro.models.Account;
import com.svail.jwtshiro.shiro.services.IAccountProvider;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import java.util.regex.Pattern;

public class UsernameRealm extends AuthorizingRealm {

  private static Pattern cellPhonePattern = Pattern.compile("^(?:\\+?86)?1(?:3\\d{3}|5[^4\\D]\\d{2}|8\\d{3}|7(?:[35678]\\d{2}|4(?:0\\d|1[0-2]|9\\d))|9[189]\\d{2}|66\\d{2})\\d{6}$");
  private static Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
  private static Pattern userNamePattern = Pattern.compile("^[A-Za-z_@.]{6,10}$");

  private IAccountProvider accountProvider;

  @Override
  public String getName() {
   return "UsernameRealm";
  }

  public void setAccountProvider(IAccountProvider accountProvider) {
    this.accountProvider = accountProvider;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

    String principal = (String)token.getPrincipal();
    String password = (String)token.getCredentials();


    // Principal 可能是用户名，邮箱，手机号，在这里进行判断

    Account account = null;

    if (emailPattern.matcher(principal).matches()){
     // 手机号登陆
      account =  accountProvider.findUserByCellphone(principal);
    }else if (userNamePattern.matcher(principal).matches()){
      // 用户名登陆
      account = accountProvider.findUserByUsername(principal);
    }else if (cellPhonePattern.matcher(principal).matches()){
      // 手机号登陆
      account = accountProvider.findUserByCellphone(principal);

    }

    if (account == null) {
      //
      throw  new AuthenticationException("用户不存在");
    }

    String username = account.getUsername();
    String credentialsSalt = account.getCredentialsSalt();

    return new SimpleAuthenticationInfo(username,password,ByteSource.Util.bytes(credentialsSalt),getName());

  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    String username = (String)principals.getPrimaryPrincipal();
    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
    authorizationInfo.setRoles(accountProvider.findRoles(username));
    authorizationInfo.setStringPermissions(accountProvider.findPermissions(username));
    return authorizationInfo;
  }
}
