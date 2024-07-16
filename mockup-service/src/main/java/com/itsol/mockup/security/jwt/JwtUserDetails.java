package com.itsol.mockup.security.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author Rin-DTS
 */
public class JwtUserDetails implements UserDetails {
    private String username;
    private String token;
    private String email;
    private String phone;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtUserDetails(String username, String password, String token, List<GrantedAuthority> grantedAuthorities) {
        this.username = username;
        this.token = token;
        this.password = password;
        this.authorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getToken() {
        return token;
    }

}
