package com.authen.security;

import com.authen.security.jwt.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtValidator {
    private final String secret = "secretKey";
    public JwtUser validate(String token) {
        JwtUser jwtUser = null;
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            jwtUser = new JwtUser();
            jwtUser.setUsername(claims.getSubject());
            jwtUser.setPassword(claims.get("password").toString());
            jwtUser.setRoles(claims.get("roles").toString());
        } catch (Exception e) {
            System.out.println(e);
        }
        return jwtUser;
    }
}
