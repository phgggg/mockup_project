package com.itsol.mockup.security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private String token;
    public JwtAuthenticationToken(String token) {
        super(null, null);
        this.token = token;
    }
    public JwtAuthenticationToken(JwtUserDetails principal, String credentials, String token) {
        super(principal, credentials);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
