package com.fuelstation.managmentapi.common.application.models;

import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final Date timestamp;
    private final String message;
    private final String code;
    private final HttpStatus status;
    
    public ErrorResponse(String message, String code, HttpStatus status) {
        this.timestamp = new Date();
        this.message = message;
        this.code = code;
        this.status = status;
    }
    
}
