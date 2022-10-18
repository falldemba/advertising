package com.advertising.service.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public abstract class ApiRequestException extends RuntimeException{
    private HttpStatus statusCode;
    public ApiRequestException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
