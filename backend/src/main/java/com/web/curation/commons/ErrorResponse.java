package com.web.curation.commons;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {
    private final String message;
    private final String code;
}
