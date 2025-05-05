package com.fuelstation.managmentapi.common.application.exceptions;

import lombok.Getter;

@Getter
public abstract class NotFoundException extends RuntimeException {
    private final String code;

    protected NotFoundException(String message, String resource) {
        super(message);
        this.code = resource + ".NOT_FOUND";
    }

    

}