package com.authen.services.impl;

import com.authen.domain.entity.RoleEntity;
import com.authen.domain.entity.UsersEntity;
import com.authen.repository.UsersRepository;
import com.authen.security.jwt.JwtUserDetails;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private UsersRepository usersRepository;


    public CustomUserDetailsServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersEntity user = usersRepository.getUsersByUserName(username);

        List<RoleEntity> lst = user.getRoles();
        StringBuilder sp = new StringBuilder();
        lst.forEach(i -> {
            if(null != sp){
                sp.append(i.getName() + ", ");
            }else{
                sp.append(i.getName());
            }
        });

        if(user == null) {
            throw new UsernameNotFoundException(String.format("No valid user found with username '%s'.", username));
        }else {
            return new JwtUserDetails(
                    user.getUserName(),
                    user.getPassWord(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList(String.valueOf(sp))
            );
        }
    }
}
