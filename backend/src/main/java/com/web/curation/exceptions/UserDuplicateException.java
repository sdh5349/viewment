package com.web.curation.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDuplicateException extends RuntimeException {
    private String email;
}
