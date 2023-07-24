package com.project.splitwise.exception;

import org.springframework.http.HttpStatus;

public class InvalidSplitException extends BaseException {

    public InvalidSplitException(String message, String... info) {
        super(message, info);
    }

    public InvalidSplitException(String message, HttpStatus httpStatus, String... info) {
        super(message, httpStatus, info);
    }

    public InvalidSplitException(String message, Exception exception,
                             HttpStatus httpStatus, String... info) {
        super(message, exception, httpStatus, info);
    }
}
