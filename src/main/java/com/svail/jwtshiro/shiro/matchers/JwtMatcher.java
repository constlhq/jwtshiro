package com.svail.jwtshiro.shiro.matchers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.svail.jwtshiro.shiro.services.JwtAuthService;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.stereotype.Component;

@Component
public class JwtMatcher implements CredentialsMatcher {
    private JwtAuthService jwtAuthService;
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        return info.getCredentials()!=null;

    }
}
