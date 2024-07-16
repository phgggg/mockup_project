package com.itsol.mockup.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itsol.mockup.security.jwt.JwtAuthenticationToken;
import com.itsol.mockup.web.dto.response.LoginRegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Rin-DTS
 */

@Component
public class JwtAuthenticationLoginRegisterSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String tokenString = authenticationToken.getToken();
//        Map<String, String> map = new HashMap<>();
//        map.put(RentConstant.TOKEN_NAME, tokenString);
        objectMapper.writeValue(httpServletResponse.getWriter(), new LoginRegisterResponse(tokenString));
        clearAuthenticationAttributes(request);
    }
}