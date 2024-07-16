package com.itsol.mockup.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CREATED)
public class JwtInvalid extends BadCredentialsException {
    public JwtInvalid(String msg) {
        super(msg);
    }

    public JwtInvalid(String msg, Throwable t) {
        super(msg, t);
    }
}
