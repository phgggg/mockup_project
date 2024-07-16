package com.itsol.mockup.security.register;

import com.itsol.mockup.security.JwtGenerator;
import com.itsol.mockup.security.jwt.JwtUser;
import com.itsol.mockup.security.jwt.JwtUserDetails;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;

public class JwtAuthenticationRegister extends AbstractAuthenticationToken {
    private JwtUserDetails jwtUserDetails;
    private String password;
    private String email;
    private String phone;
    private String fullName;
    private String faceBookUrl;
    private String address;

    public JwtAuthenticationRegister(String username, String password, String email, String fullName, String phone, String faceBookUrl, String address) {
        super(null);
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.faceBookUrl = faceBookUrl;
        this.address = address;
        JwtGenerator jwtGenerator = new JwtGenerator();
        JwtUser jwtUser = new JwtUser();
        jwtUser.setUsername(username);
        jwtUser.setPassword(password);
        String token = jwtGenerator.generate(jwtUser);
        List<GrantedAuthority> authorities =
                AuthorityUtils.commaSeparatedStringToAuthorityList(jwtUser.getRoles());
        jwtUserDetails = new JwtUserDetails(jwtUser.getUsername(),
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

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFaceBookUrl() {
        return faceBookUrl;
    }

    public void setFaceBookUrl(String faceBookUrl) {
        this.faceBookUrl = faceBookUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
