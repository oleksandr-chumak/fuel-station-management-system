package com.fuelstation.managmentapi.common.application;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fuelstation.managmentapi.common.domain.DomainException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

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
                ex.getHttpStatus(),
                ex.getDetails()
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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        List<Map<String, Object>> fieldErrors = new ArrayList<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("field", fe.getField());
            entry.put("message", fe.getDefaultMessage());
            entry.put("rejectedValue", fe.getRejectedValue());
            fieldErrors.add(entry);
        }
        ex.getBindingResult().getGlobalErrors().forEach(ge -> {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("field", ge.getObjectName());
            entry.put("message", ge.getDefaultMessage());
            fieldErrors.add(entry);
        });
        var error = new ErrorResponse(
                "Validation failed",
                HttpStatus.BAD_REQUEST.name(),
                HttpStatus.BAD_REQUEST,
                Map.of("errors", fieldErrors)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        String mostSpecific = ex.getMostSpecificCause() != null
                ? ex.getMostSpecificCause().getMessage()
                : ex.getMessage();
        var error = new ErrorResponse(
                "Malformed request body",
                HttpStatus.BAD_REQUEST.name(),
                HttpStatus.BAD_REQUEST,
                Map.of("reason", mostSpecific == null ? "" : mostSpecific)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        List<Map<String, Object>> violations = new ArrayList<>();
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("field", v.getPropertyPath().toString());
            entry.put("message", v.getMessage());
            entry.put("rejectedValue", v.getInvalidValue());
            violations.add(entry);
        }
        var error = new ErrorResponse(
                "Validation failed",
                HttpStatus.BAD_REQUEST.name(),
                HttpStatus.BAD_REQUEST,
                Map.of("errors", violations)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        List<Map<String, Object>> errors = new ArrayList<>();
        ex.getAllErrors().forEach(err -> {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("message", err.getDefaultMessage());
            errors.add(entry);
        });
        var error = new ErrorResponse(
                "Validation failed",
                HttpStatus.BAD_REQUEST.name(),
                HttpStatus.BAD_REQUEST,
                Map.of("errors", errors)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("field", ex.getName());
        entry.put("message", "Invalid value for parameter '" + ex.getName() + "'"
                + (ex.getRequiredType() != null ? ", expected type " + ex.getRequiredType().getSimpleName() : ""));
        entry.put("rejectedValue", ex.getValue());
        var error = new ErrorResponse(
                "Validation failed",
                HttpStatus.BAD_REQUEST.name(),
                HttpStatus.BAD_REQUEST,
                Map.of("errors", List.of(entry))
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
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