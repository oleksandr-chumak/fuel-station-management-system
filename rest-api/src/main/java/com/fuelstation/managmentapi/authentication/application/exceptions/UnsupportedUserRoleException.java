package com.fuelstation.managmentapi.authentication.application.exceptions;

import com.fuelstation.managmentapi.authentication.domain.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
public class UnsupportedUserRoleException extends RuntimeException {

    public UnsupportedUserRoleException(UserRole userRole) {
        super("Unsupported user role: " + userRole.toString());
    }

}
