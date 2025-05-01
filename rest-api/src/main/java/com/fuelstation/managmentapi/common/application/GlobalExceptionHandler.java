package com.fuelstation.managmentapi.common.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.fuelstation.managmentapi.common.application.models.ErrorResponseEntity;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    // Handle UsernameNotFoundException - 401 Unauthorized
    @ExceptionHandler(UsernameNotFoundException.class)
    public ErrorResponseEntity handleUsernameNotFoundException(
            UsernameNotFoundException ex, WebRequest request) {
        
        logger.error("UsernameNotFoundException occurred: {}", ex.getMessage());

        return new ErrorResponseEntity("Unauthorized", "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
    }
    
    // Handle general exceptions - 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ErrorResponseEntity handleGlobalException(
            Exception ex, WebRequest request) {
        
        logger.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
        
        return new ErrorResponseEntity("Internal server error", "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}