package com.web.curation.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserNotFoundException extends RuntimeException {
    private final String value = "사용자가 존재하지 않습니다.";
}
