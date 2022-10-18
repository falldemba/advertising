package com.advertising.service.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceIsAlreadyExistException extends ApiRequestException{
    public ResourceIsAlreadyExistException(String message, HttpStatus statusCode) {
        super(message, statusCode);
    }
}
