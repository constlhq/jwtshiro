package com.svail.jwtshiro.shiro.realms;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.svail.jwtshiro.shiro.services.JwtAuthService;
import com.svail.jwtshiro.shiro.token.JwtToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

import java.util.HashSet;
import java.util.Set;

public class JwtRealm extends AuthorizingRealm {

    private JwtAuthService jwtAuthService;

    public void setJwtAuthService(JwtAuthService jwtAuthService) {
        this.jwtAuthService = jwtAuthService;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        JwtToken jwtToken = (JwtToken)token;

        if (jwtAuthService.decodeJwt((String)jwtToken.getPrincipal())==null)
            throw new AuthenticationException();

        return new SimpleAuthenticationInfo(jwtToken, jwtToken.getCredentials(), getName());
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        JwtToken principal = (JwtToken)principals.getPrimaryPrincipal();

        if (principal instanceof JwtToken ){
            System.out.println("IN$$$$$$$$$$$");
            JwtToken jwtToken = (JwtToken)principals.getPrimaryPrincipal();
            DecodedJWT decodedJWT =  jwtAuthService.decodeJwt((String)jwtToken.getPrincipal());
            Set<String> roles = new HashSet<>(decodedJWT.getClaim("roles").asList(String.class));
            Set<String> permissions = new HashSet<>(decodedJWT.getClaim("roles").asList(String.class));
            SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
            authorizationInfo.setRoles(roles);
            authorizationInfo.setStringPermissions(permissions);
            return authorizationInfo;
        }
        return null;
    }

}
