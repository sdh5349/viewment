package com.web.curation.exceptions;

import com.web.curation.commons.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * com.web.curation.exceptions
 * GlobalExceptionController.java
 * @date    2021-01-28 오전 10:26
 * @author  김종성
 *
 * @변경이력
 **/

@ControllerAdvice
public class GlobalExceptionController {


    @ExceptionHandler(UserDuplicateException.class)
    public ResponseEntity<?> handleUserDuplicateException(UserDuplicateException e){
        final String type = e.getEmail()==null?"nickname":"email";
        final String msg  = String.format("[ %s ]는 증복된 %s 입니다", e.getEmail()==null?e.getNickname():e.getEmail(), type);
        final String code = "duplicated.user.exception";
        ErrorResponse errorResponse = new ErrorResponse(msg, code);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?>  handleUserNotFoundException(UserNotFoundException e){
        final String msg  = e.getValue();
        final String code = "nonexistence.user.exception";
        ErrorResponse errorResponse = new ErrorResponse(msg, code);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<?>  handleElementNotFoundException(ElementNotFoundException e){
        final String msg  = String.format("[%s]의 [%s]가 존재하지 않습니다.", e.getElementType(), e.getValue());
        final String code = "nonexistence.element.exception";
        ErrorResponse errorResponse = new ErrorResponse(msg, code);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}
