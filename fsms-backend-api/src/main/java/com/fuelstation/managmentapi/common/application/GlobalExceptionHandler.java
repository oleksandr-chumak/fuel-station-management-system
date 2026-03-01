package com.fuelstation.managmentapi.common.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fuelstation.managmentapi.common.domain.DomainException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
        logger.error("Unexpected exception: ", ex);
        var error = new ErrorResponse(
                "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        var error = new ErrorResponse(
                ex.getMessage(),
                ex.getCode(),
                ex.getHttpStatus()
        );
        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        var error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.FORBIDDEN.name(),
                HttpStatus.FORBIDDEN
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        var error = new ErrorResponse(
                "Invalid username or password",
                HttpStatus.UNAUTHORIZED.name(),
                HttpStatus.UNAUTHORIZED
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

}