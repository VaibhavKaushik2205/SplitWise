package com.project.splitwise.exception;

import org.springframework.http.HttpStatus;


public class BaseException extends RuntimeException {

    private Exception exception;
    private String[] info;
    private HttpStatus httpStatus;

    protected BaseException(String message, String... info) {
        super(message);
        this.exception = null;
        this.info = info == null ? new String[0] : info;
    }

    protected BaseException(String message, HttpStatus httpStatus, String... info) {
        this(message, info);
        this.httpStatus = httpStatus;
    }

    protected BaseException(String message, Exception exception, HttpStatus httpStatus,
        String... info) {
        super(message, exception);
        this.exception = exception;
        this.httpStatus = httpStatus;
    }

    public String[] getInfo() {
        return info;
    }

    public Exception getException() {
        return exception;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
