package com.fuelstation.managmentapi.common.application;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final Instant timestamp;
    private final String message;
    private final String code;
    private final HttpStatus status;
    
    public ErrorResponse(String message, String code, HttpStatus status) {
        this.timestamp = Instant.now();
        this.message = message;
        this.code = code;
        this.status = status;
    }
    
}
