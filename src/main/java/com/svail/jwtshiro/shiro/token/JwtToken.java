package com.svail.jwtshiro.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

public class JwtToken implements AuthenticationToken {

    private static final long serialVersionUID = 1832943548774576547L;

    private String host;// 客户IP
    private String jwt;

    public JwtToken(String host,String jwt){
        this.host = host;
        this.jwt = jwt;
    }

    @Override
    public Object getPrincipal() {
        return this.jwt;
    }

    @Override
    public Object getCredentials() {
        return Boolean.TRUE;
    }

}
