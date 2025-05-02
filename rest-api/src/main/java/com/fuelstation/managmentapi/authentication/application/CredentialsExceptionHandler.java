package com.fuelstation.managmentapi.authentication.application;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.fuelstation.managmentapi.authentication.domain.exceptions.CredentialsAlreadyExistsException;
import com.fuelstation.managmentapi.authentication.domain.exceptions.CredentialsDomainException;
import com.fuelstation.managmentapi.common.application.BaseExceptionHandler;
import com.fuelstation.managmentapi.common.application.models.ErrorResponseEntity;

@ControllerAdvice
public class CredentialsExceptionHandler extends BaseExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CredentialsExceptionHandler.class);

    @ExceptionHandler({
        CredentialsAlreadyExistsException.class
    })
    public ErrorResponseEntity handleConflict(CredentialsDomainException ex) {
        return ErrorResponseEntity.fromDomain(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ UsernameNotFoundException.class})
    public ErrorResponseEntity handleUsernameNotFoundException(
            UsernameNotFoundException ex, WebRequest request) {
        
        logger.error("UsernameNotFoundException occurred: {}", ex.getMessage());

        return new ErrorResponseEntity("Unauthorized", "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseEntity> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        
        Map<String, Object> requestDetails = getRequestDetails(request);
        logger.warn("Access denied: {}, Request details: {}", ex.getMessage(), requestDetails);
        
        ErrorResponseEntity errorResponse = new ErrorResponseEntity(
                "You don't have permission to access this resource", 
                "ACCESS_DENIED", 
                HttpStatus.FORBIDDEN);
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
    
}
