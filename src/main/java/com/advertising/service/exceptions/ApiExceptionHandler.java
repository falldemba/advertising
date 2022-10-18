package com.advertising.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = ApiRequestException.class)
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e){
        HttpStatus statusCode = e.getStatusCode();
        return new ResponseEntity<>(new ApiException(
                e.getMessage(),
                statusCode,
                LocalDateTime.now()
        ), statusCode);
    }
}
