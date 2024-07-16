package com.itsol.mockup.security;

import com.itsol.mockup.security.jwt.JwtUser;
import com.itsol.mockup.utils.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @author Rin-DTS
 */
public class JwtGenerator {
	final String SECRET = "secretKey";
    public String generate(JwtUser jwtUser) {
        Claims claims = Jwts.claims()
                .setSubject(jwtUser.getUsername());
        claims.put("password", jwtUser.getPassword());
        claims.put("roles", jwtUser.getRoles());
        Date date = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + Constants.TIME_TOKEN_EXPIRE))
                .compact();
    }

}
