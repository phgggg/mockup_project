package com.authen.security;

import com.authen.security.jwt.JwtUser;
import com.authen.utils.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

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
