package com.fuelstation.managmentapi.fuelstation.application.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignManagerRequest {
    private long managerId;
}