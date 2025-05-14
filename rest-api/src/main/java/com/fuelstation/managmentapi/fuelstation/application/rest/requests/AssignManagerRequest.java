package com.fuelstation.managmentapi.fuelstation.application.rest.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignManagerRequest {

    @NotNull(message = "Manager id must not be null")
    private long managerId;

}