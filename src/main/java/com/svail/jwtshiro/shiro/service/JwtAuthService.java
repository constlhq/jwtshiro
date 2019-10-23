package com.svail.jwtshiro.shiro.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service

public class JwtAuthService {
    private final static Long EXPIRE_PERIOD = 3600L*24; // 有效期24小时
    private final static String ISSUER = "SVAIL";
    private final static Algorithm ALGORITHM = Algorithm.HMAC256("your_secret");
    private final static JWTVerifier VERIFIER = JWT.require(ALGORITHM).withIssuer(ISSUER).build();

    public String issueJwt(String subject,String roles){
        try {

            String token = JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(subject)
                    .withClaim("roles",roles)
                    .withExpiresAt(Date.from(Instant.now().plusSeconds(EXPIRE_PERIOD))) // 24小时
                    .sign(ALGORITHM);
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
        }
        return
    }

    public DecodedJWT decodeJwt(String token){

        try {

            DecodedJWT jwt = verifier.verify(token);
        } catch (JWTDecodeException exception){
            //Invalid token
            return null;
        }
    }
}
