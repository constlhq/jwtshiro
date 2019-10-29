package com.svail.jwtshiro.shiro.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Data
@Service
@PropertySource("configuration.properties")
@ConfigurationProperties("jwtauthservice")
public class JwtAuthService {
    private  Long valid_period;
    private  String issuer;
    private  String secret;
    private  Algorithm algorithm;
    private  JWTVerifier jwtVerifier;


    @PostConstruct
    private void init(){
      this.algorithm = Algorithm.HMAC256(secret);
      this.jwtVerifier = JWT.require(algorithm).withIssuer(issuer).build();
    }



    public String issueJwt(String subject,String roles){
        String token = Strings.EMPTY;
        try {
            token = JWT.create()
                    .withIssuer(issuer)
                    .withSubject(subject)
                    .withClaim("roles",roles)
                    .withExpiresAt(Date.from(Instant.now().plusSeconds(valid_period))) // 24小时
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
          exception.printStackTrace();
        }
        return token;
    }

    public DecodedJWT decodeJwt(String token){

      DecodedJWT jwt = null ;
        try {

          JWTVerifier verifier = JWT.require(algorithm)
                  .withIssuer(issuer)
                  .build(); //Reusable verifier instance
          jwt = verifier.verify(token);
        } catch (JWTDecodeException exception){
            //Invalid token
        }
        return jwt;
    }
}
