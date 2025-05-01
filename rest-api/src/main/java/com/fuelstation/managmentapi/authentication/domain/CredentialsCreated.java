package com.fuelstation.managmentapi.authentication.domain;

import com.fuelstation.managmentapi.common.domain.DomainEvent;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CredentialsCreated implements DomainEvent {
    private long credentialsId;
    private String email;
    private String plainPassword;
    private UserRole role; 
}
