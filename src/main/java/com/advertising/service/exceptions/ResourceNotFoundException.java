package com.advertising.service.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiRequestException {
    public ResourceNotFoundException(String message, HttpStatus statusCode) {
        super(message, statusCode);
    }
}
