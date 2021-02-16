package com.web.curation.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TypeNotDefineException extends RuntimeException {

    private String type;
}
