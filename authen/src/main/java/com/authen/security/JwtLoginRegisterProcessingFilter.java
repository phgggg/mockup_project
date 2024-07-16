package com.authen.security;

import com.authen.security.login.JwtAuthenticationLogin;
import com.authen.security.register.JwtAuthenticationRegister;
import com.authen.utils.Constants;
import com.authen.domain.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtLoginRegisterProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private JwtAuthenticationLoginRegisterSuccessHandler successHandler;

    private JwtAuthenticationLoginRegisterFailureHandler failureHandler;

    private ObjectMapper objectMapper;

    private UserDetailsService userDetailsService;

    public JwtLoginRegisterProcessingFilter(RequestMatcher matcher,
                                            JwtAuthenticationLoginRegisterSuccessHandler successHandler,
                                            JwtAuthenticationLoginRegisterFailureHandler failureHandler,
                                            ObjectMapper mapper,
                                            UserDetailsService userDetailsService) {
        super(matcher);
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.objectMapper = mapper;
        this.userDetailsService = userDetailsService;

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

        LoginRequest loginRequest = objectMapper.readValue(httpServletRequest.getReader(), LoginRequest.class);

//        if (StringUtils.isBlank(loginRequest.getUsername()) || StringUtils.isBlank(loginRequest.getPassword())) {
//            throw new AuthenticationUsernamePasswordInvalidException("invalid param for login");
//        }


        if (httpServletRequest.getRequestURI().equals(Constants.ENPOINT_LOGIN)) {

            JwtAuthenticationLogin authenLogin = new JwtAuthenticationLogin(userDetailsService, loginRequest.getUsername(), loginRequest.getPassword());
            return getAuthenticationManager().authenticate(authenLogin);
        } else {
            if (loginRequest.getPassword().length() < Constants.MIN_LEHGTH_PASSWORD) {//Leght password must than 6 character
                throw new AuthenticationServiceException("Độ dài mật khẩu phải nhiều hơn 6 ký tự.");
            }
            JwtAuthenticationRegister register = new JwtAuthenticationRegister(
                    loginRequest.getUsername(), loginRequest.getPassword(),
                    loginRequest.getEmail(), loginRequest.getPhone(),
                    loginRequest.getFullName(),
                    loginRequest.getFacebookUrl(), loginRequest.getAddress());
            return getAuthenticationManager().authenticate(register);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }


}
