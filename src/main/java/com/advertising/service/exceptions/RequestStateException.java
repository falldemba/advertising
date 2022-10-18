package com.advertising.service.exceptions;

import org.springframework.http.HttpStatus;

public class RequestStateException extends ApiRequestException{
    public RequestStateException(String message, HttpStatus statusCode) {
        super(message, statusCode);
    }
}
