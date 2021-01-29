package com.web.curation.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class FirebaseInvalidTokenException extends BadCredentialsException {
    public FirebaseInvalidTokenException(String msg) {
        super(msg);
    }
}
