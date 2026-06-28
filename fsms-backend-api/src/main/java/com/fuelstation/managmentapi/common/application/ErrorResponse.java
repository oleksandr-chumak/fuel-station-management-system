package com.fuelstation.managmentapi.common.application;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final Instant timestamp;
    private final String message;
    private final String code;
    private final HttpStatus status;
    private final Map<String, Object> details;

    public ErrorResponse(String message, String code, HttpStatus status) {
        this(message, code, status, Map.of());
    }

    public ErrorResponse(String message, String code, HttpStatus status, Map<String, Object> details) {
        this.timestamp = Instant.now();
        this.message = message;
        this.code = code;
        this.status = status;
        this.details = details == null ? Map.of() : Map.copyOf(details);
    }

}
