package com.authen.security.login;

import com.authen.security.JwtGenerator;
import com.authen.security.jwt.JwtUser;
import com.authen.security.jwt.JwtUserDetails;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

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
