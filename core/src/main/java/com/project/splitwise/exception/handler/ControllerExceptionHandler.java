package com.project.splitwise.exception.handler;

import com.project.splitwise.exception.BaseException;
import com.project.splitwise.exception.errors.ErrorCategory;
import com.project.splitwise.exception.errors.ErrorDescription;
import com.project.splitwise.exception.errors.ErrorType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({BaseException.class})
    @ResponseBody
    public ResponseEntity<?> handleBaseExceptions(final BaseException ex, WebRequest request) {
        log.error("Failed  to process request : {}, error : {}", request, ExceptionUtils.getStackTrace(ex));
        ErrorDescription errorDescription = ErrorDescription.builder()
            .errorCategory(ErrorCategory.VALIDATION)
            .errorType(ErrorType.SYSTEM_INSERTION_FAILURE)
            .errorMsg(ex.getMessage()).build();
        return new ResponseEntity<>(errorDescription, getStatus(ex));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers,
        HttpStatus status, WebRequest request) {

        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> String.format("%s %s", error.getField(), error.getDefaultMessage()))
            .collect(Collectors.toList());

        ErrorDescription errorDescription = ErrorDescription.builder()
            .errorCategory(ErrorCategory.VALIDATION)
            .errorMsg(errors.toString()).build();
        log.error("Required request params is not present, errorDescription:{}", errorDescription);
        return new ResponseEntity<>(errorDescription, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(FeignException.class)
//    public ResponseEntity<ErrorDescription> handleFeignExceptions(final FeignException ex,
//        WebRequest request) {
//        log.error("Feign Client threw error : {} for request : {}", ExceptionUtils.getStackTrace(ex), request);
//
//        ErrorDescription errorDescription = ErrorDescription.builder()
//            .errorCategory(ErrorCategory.VALIDATION)
//            .errorMsg(ex.contentUTF8()).build();
//        return new ResponseEntity<>(errorDescription, HttpStatus.valueOf(ex.status()));
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDescription> handleInternalServerError(final Exception ex,
        WebRequest request) {
        log.error("Failed  to process request : {}, error : {}", request, ExceptionUtils.getStackTrace(ex));

        ErrorDescription errorDescription = ErrorDescription.builder()
            .errorCategory(ErrorCategory.SYSTEM)
            .errorType(ErrorType.UNEXPECTED_FAILURE)
            .errorMsg(ex.getMessage()).build();
        return new ResponseEntity<>(errorDescription, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpStatus getStatus(BaseException exception) {
        return Optional.ofNullable(exception.getHttpStatus())
            .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}