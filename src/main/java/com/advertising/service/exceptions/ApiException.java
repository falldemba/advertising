package com.advertising.service.exceptions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class ApiException {
    final String message;
    final HttpStatus httpStatus;
    final LocalDateTime timestamp;
}
