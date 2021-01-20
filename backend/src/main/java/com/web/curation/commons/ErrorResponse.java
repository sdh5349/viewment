package com.web.curation.commons;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {
    private final String message;
    private final String code;
//    private List<FieldError> errors;
//
//    static class FieldError{
//        private String field;
//        private String value;
//        private String reason;
//    }
}
