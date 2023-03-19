package com.project.splitwise.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Slf4j
public class BadRequestException extends BaseException {

    public BadRequestException(String message) {
        super(message);
        log.error(message);
    }

    public BadRequestException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public BadRequestException(String message, Exception e, HttpStatus httpStatus) {
        super(message, e, httpStatus);
    }
}
