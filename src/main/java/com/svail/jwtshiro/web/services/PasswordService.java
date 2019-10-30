package com.svail.jwtshiro.web.services;

import com.svail.jwtshiro.web.models.SvailUser;
import lombok.Data;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Service;

@Data
@Service
public class PasswordService {
  private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
  private String algorithmName = "SHA-256";
  private int hashIterations = 2;


  public void encryptPassword(SvailUser user) {
    user.setCredentialsSalt (randomNumberGenerator.nextBytes().toHex());
    String newPassword = new SimpleHash(
            algorithmName,
            user.getPassword(),
            ByteSource.Util.bytes(user.getCredentialsSalt()),
            hashIterations).toHex();

    user.setPassword(newPassword);
  }
}
