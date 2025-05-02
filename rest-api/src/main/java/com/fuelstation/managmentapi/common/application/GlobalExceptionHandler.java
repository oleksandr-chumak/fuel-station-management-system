package com.fuelstation.managmentapi.common.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fuelstation.managmentapi.common.application.models.ErrorResponseEntity;

@ControllerAdvice
public class GlobalExceptionHandler extends BaseExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle validation errors from @Valid annotations (400 Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseEntity> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        // Extract validation errors
        List<Map<String, String>> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> {
                    Map<String, String> errorDetails = new HashMap<>();
                    errorDetails.put("field", error.getField());
                    errorDetails.put("message", error.getDefaultMessage());
                    errorDetails.put("rejectedValue", String.valueOf(error.getRejectedValue()));
                    return errorDetails;
                })
                .collect(Collectors.toList());
        
        Map<String, Object> requestDetails = getRequestDetails(request);
        logger.warn("Validation failed: {} errors, Request details: {}", validationErrors.size(), requestDetails);
        
        ErrorResponseEntity errorResponse = new ErrorResponseEntity(
                validationErrors.toString(), 
                "VALIDATION_FAILED", 
                HttpStatus.BAD_REQUEST);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }    

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseEntity> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, WebRequest request) {
        
        Map<String, Object> requestDetails = getRequestDetails(request);
        logger.warn("Missing parameter: {}, Request details: {}", ex.getMessage(), requestDetails);
        
        Map<String, Object> details = new HashMap<>();
        details.put("parameterName", ex.getParameterName());
        details.put("parameterType", ex.getParameterType());
        
        ErrorResponseEntity errorResponse = new ErrorResponseEntity(
                details.toString(), 
                "MISSING_PARAMETER", 
                HttpStatus.BAD_REQUEST);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseEntity> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        
        Map<String, Object> requestDetails = getRequestDetails(request);
        logger.warn("Type mismatch: {}, Request details: {}", ex.getMessage(), requestDetails);
        
        Map<String, Object> details = new HashMap<>();
        details.put("paramName", ex.getName());
        details.put("providedValue", ex.getValue());
        details.put("requiredType", ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
        
        ErrorResponseEntity errorResponse = new ErrorResponseEntity(
                details.toString(), 
                "TYPE_MISMATCH", 
                HttpStatus.BAD_REQUEST);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponseEntity> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, WebRequest request) {
        
        Map<String, Object> requestDetails = getRequestDetails(request);
        logger.warn("Unsupported media type: {}, Request details: {}", ex.getMessage(), requestDetails);
        
        Map<String, Object> details = new HashMap<>();
        details.put("unsupportedMediaType", ex.getContentType() != null ? ex.getContentType().toString() : "unknown");
        details.put("supportedMediaTypes", ex.getSupportedMediaTypes());
        
        ErrorResponseEntity errorResponse = new ErrorResponseEntity(
                "Unsupported media type", 
                "UNSUPPORTED_MEDIA_TYPE", 
                HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
    }

    /**
     * Handle method not supported (405 Method Not Allowed)
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseEntity> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, WebRequest request) {
        
        Map<String, Object> requestDetails = getRequestDetails(request);
        logger.warn("Method not allowed: {}, Request details: {}", ex.getMessage(), requestDetails);
        
        Map<String, Object> details = new HashMap<>();
        details.put("unsupportedMethod", ex.getMethod());
        details.put("supportedMethods", ex.getSupportedHttpMethods());
        
        ErrorResponseEntity errorResponse = new ErrorResponseEntity(
                "Method not allowed", 
                "METHOD_NOT_ALLOWED", 
                HttpStatus.METHOD_NOT_ALLOWED);
        
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.ALLOW, ex.getSupportedHttpMethods().toString())
                .body(errorResponse);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseEntity> handleNoHandlerFoundException(
            NoHandlerFoundException ex, WebRequest request) {
        
        Map<String, Object> requestDetails = getRequestDetails(request);
        logger.warn("No handler found: {}, Request details: {}", ex.getMessage(), requestDetails);
        
        Map<String, Object> details = new HashMap<>();
        details.put("path", ex.getRequestURL());
        details.put("method", ex.getHttpMethod());
        
        ErrorResponseEntity errorResponse = new ErrorResponseEntity(
                "No handler found for this endpoint", 
                "ENDPOINT_NOT_FOUND", 
                HttpStatus.NOT_FOUND);
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    
    /**
     * Internal server error (500)
     */
    @ExceptionHandler(Exception.class)
    public ErrorResponseEntity handleGlobalException(
            Exception ex, WebRequest request) {
        
        logger.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
        
        return new ErrorResponseEntity("Internal server error", "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}