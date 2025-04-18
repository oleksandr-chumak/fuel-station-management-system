package com.fuelstation.managmentapi.manager.application.rest;

import com.fuelstation.managmentapi.manager.domain.Manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String status;
    private Long credentialsId;

    public static ManagerResponse fromDomain(Manager manager) {
        ManagerResponse response = new ManagerResponse();
        response.setId(manager.getId());
        response.setFirstName(manager.getFirstName());
        response.setLastName(manager.getLastName());
        response.setStatus(manager.getStatus().toString());
        response.setCredentialsId(manager.getCredentialsId());
        return response;
    }
}