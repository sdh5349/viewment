package com.web.curation.exceptions;

import com.web.curation.commons.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * com.web.curation.exceptions
 * GlobalExceptionController.java
 * @date    2021-01-28 오전 10:26
 * @author  김종성
 *
 * @변경이력
 **/

@ControllerAdvice
@Slf4j
public class GlobalExceptionController {

    @ExceptionHandler(UserDuplicateException.class)
    public ResponseEntity<?> handleUserDuplicateException(UserDuplicateException e){
        final String type = e.getEmail()==null?"nickname":"email";
        final String msg  = String.format("[ %s ]는 증복된 %s 입니다", e.getEmail()==null?e.getNickname():e.getEmail(), type);
        final String code = "duplicated.user.exception";
        ErrorResponse errorResponse = new ErrorResponse(msg, code);
        return ResponseEntity.badRequest().body(errorResponse);
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String, String> errorResponse = new HashMap<>();
        e.getBindingResult().getAllErrors()
                .forEach(error->errorResponse.put(((FieldError) error).getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e){
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), e.toString()));
    }

    @ExceptionHandler(value = ConstraintViolationException.class) // 유효성 검사 실패 시 발생하는 예외를 처리
    protected ResponseEntity<?> handleException(ConstraintViolationException exception) {
        final String msg = getResultMessage(exception.getConstraintViolations().iterator());
        final String code = "";
        ErrorResponse errorResponse = new ErrorResponse(msg, code);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception e){
        final ErrorResponse error = new ErrorResponse(e.getMessage(), e.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    protected String getResultMessage(final Iterator<ConstraintViolation<?>> violationIterator) {
        final StringBuilder resultMessageBuilder = new StringBuilder();
        while (violationIterator.hasNext() == true) {
            final ConstraintViolation<?> constraintViolation = violationIterator.next();
            resultMessageBuilder
                    .append("['")
                    .append(getPopertyName(constraintViolation.getPropertyPath().toString())) // 유효성 검사가 실패한 속성
                    .append("' is '")
                    .append(constraintViolation.getInvalidValue()) // 유효하지 않은 값
                    .append("'. ")
                    .append(constraintViolation.getMessage()) // 유효성 검사 실패 시 메시지
                    .append("]");

            if (violationIterator.hasNext() == true) {
                resultMessageBuilder.append(", ");
            }
        }

        return resultMessageBuilder.toString();
    }

    protected String getPopertyName(String propertyPath) {
        return propertyPath.substring(propertyPath.lastIndexOf('.') + 1); // 전체 속성 경로에서 속성 이름만 가져온다.
    }
}
