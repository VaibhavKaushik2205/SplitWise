package com.project.splitwise.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {

    public NotFoundException(String message, String... info) {
        super(message, info);
    }

    public NotFoundException(String message, HttpStatus httpStatus, String... info) {
        super(message, httpStatus, info);
    }

    public NotFoundException(String message, Exception exception,
        HttpStatus httpStatus, String... info) {
        super(message, exception, httpStatus, info);
    }

}
