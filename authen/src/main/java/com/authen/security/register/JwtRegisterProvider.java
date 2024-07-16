package com.authen.security.register;

import com.authen.domain.entity.UserRolesEntity;
import com.authen.domain.entity.UsersEntity;
import com.authen.repository.UserRoleRepository;
import com.authen.repository.UsersRepository;
import com.authen.security.exception.AuthenticationUsernamePasswordInvalidException;
import com.authen.security.jwt.JwtAuthenticationToken;
import com.authen.security.jwt.JwtUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;


@Component
public class JwtRegisterProvider implements AuthenticationProvider {
    @Autowired
    private UsersRepository userRepo;

    @Autowired
    private UserRoleRepository roleRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationRegister register = (JwtAuthenticationRegister) authentication;
        JwtUserDetails jwtUserDetails = register.getPrincipal();
        UsersEntity userEntities = userRepo.findUsersEntityByUserName(jwtUserDetails.getUsername());
        if (userEntities != null) {
            throw new AuthenticationUsernamePasswordInvalidException("Người dùng này đã tồn tại.");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncode = passwordEncoder.encode(register.getCredentials());


        UsersEntity insert = new UsersEntity();
        insert.setUserName(register.getPrincipal().getUsername());
        insert.setPassWord(passwordEncode);

        insert.setEmail(register.getEmail());
        insert.setPhone(register.getPhone());
        insert.setFullName(register.getFullName());
        insert.setFacebookUrl(register.getFaceBookUrl());
        insert.setAddress(register.getAddress());
        insert.setLastLogin(new Timestamp(new Date().getTime()));
        UsersEntity result = userRepo.save(insert);
        if (result == null) {
            throw new AuthenticationServiceException("Can not insert user into database");
        }
        UserRolesEntity userRolesEntity = new UserRolesEntity();
        userRolesEntity.setRoleId(Long.valueOf(23));
        userRolesEntity.setUserId(result.getUserId());
        UserRolesEntity resultRole = roleRepository.save(userRolesEntity);
        if (resultRole == null) {
            throw new AuthenticationServiceException("Can not insert user role into database");
        }
        return new JwtAuthenticationToken(
                register.getPrincipal(),
                register.getCredentials(),
                jwtUserDetails.getToken());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(JwtAuthenticationRegister.class);
    }
}
