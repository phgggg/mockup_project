package com.authen.security.login;

import com.authen.domain.entity.UsersEntity;
import com.authen.repository.UsersRepository;
import com.authen.security.jwt.JwtAuthenticationToken;
import com.authen.security.jwt.JwtUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

@Component
public class JwtAuthenticationLoginProvider implements AuthenticationProvider {
    @Autowired
    private UsersRepository userRepo;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationLogin authenLogin = (JwtAuthenticationLogin) authentication;
        JwtUserDetails jwtUserDetails = authenLogin.getPrincipal();
        UsersEntity userEntity = null;

        userEntity = userRepo.findUsersEntityByUserName(jwtUserDetails.getUsername());
        if (userEntity == null) {
            throw new UsernameNotFoundException("Người dùng này không tồn tại. Mật khẩu hoặc passowrd bị sai.");
        }
        if (!new BCryptPasswordEncoder().matches(authenLogin.getCredentials(), userEntity.getPassWord())) {
            throw new AuthenticationServiceException("Mật khẩu sai.");
        }
        userEntity.setLastLogin(new Timestamp(new Date().getTime()));
//        userEntity.setToken(authenLogin.getPrincipal().getToken());
        userRepo.save(userEntity);
        return new JwtAuthenticationToken(
                authenLogin.getPrincipal(),
                authenLogin.getCredentials(),
                jwtUserDetails.getToken());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(
                JwtAuthenticationLogin.class);
    }
}
