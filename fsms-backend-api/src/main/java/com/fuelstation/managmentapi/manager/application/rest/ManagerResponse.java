package com.fuelstation.managmentapi.manager.application.rest;

import com.fuelstation.managmentapi.authentication.domain.UserStatus;
import com.fuelstation.managmentapi.manager.domain.Manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerResponse {
    private Long managerId;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private UserStatus status;

    public static ManagerResponse fromDomain(Manager manager) {
        ManagerResponse response = new ManagerResponse();
        response.setManagerId(manager.getManagerId());
        response.setFirstName(manager.getFirstName());
        response.setLastName(manager.getLastName());
        response.setFullName(manager.getFullName());
        response.setEmail(manager.getEmail());
        response.setStatus(manager.getStatus());
        return response;
    }
}