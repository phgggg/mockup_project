package com.itsol.mockup.security.login;

import com.itsol.mockup.security.JwtGenerator;
import com.itsol.mockup.security.jwt.JwtUser;
import com.itsol.mockup.security.jwt.JwtUserDetails;
import com.itsol.mockup.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
public class JwtAuthenticationLogin extends AbstractAuthenticationToken {
    private JwtUserDetails jwtUserDetails;
    private String password;

    public JwtAuthenticationLogin(UserDetailsService userDetailsService, String username, String password) {
        super(null);
        this.password = password;
        JwtGenerator jwtGenerator = new JwtGenerator();
        JwtUser jwtUser = new JwtUser();
        jwtUser.setUsername(username);
        jwtUser.setPassword(password);
        UserDetails springSecurityUser = userDetailsService.loadUserByUsername(jwtUser.getUsername());
        jwtUser.setRoles(springSecurityUser.getAuthorities().stream().map(GrantedAuthority::toString).collect(Collectors.joining(",")));
        String token = jwtGenerator.generate(jwtUser);
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) springSecurityUser.getAuthorities();
//                AuthorityUtils.commaSeparatedStringToAuthorityList(jwtUser.getRoles());

        jwtUserDetails = new JwtUserDetails(
                jwtUser.getUsername(),
                jwtUser.getPassword(),
                token,
                authorities
        );
    }


    @Override
    public String getCredentials() {
        return password;
    }

    @Override
    public JwtUserDetails getPrincipal() {
        return jwtUserDetails;
    }
}
