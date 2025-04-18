package com.fuelstation.managmentapi.manager.application.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateManagerRequest {
    private String firstName;
    private String lastName;
    private String email;
}