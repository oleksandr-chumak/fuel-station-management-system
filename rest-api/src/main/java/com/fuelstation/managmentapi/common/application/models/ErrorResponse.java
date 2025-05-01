package com.fuelstation.managmentapi.common.application.models;

import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private Date timestamp;
    private String message;
    private String code;
    private HttpStatus status;
    
    public ErrorResponse(String message, String code, HttpStatus status) {
        this.timestamp = new Date();
        this.message = message;
        this.code = code;
        this.status = status;
    }
    
}
