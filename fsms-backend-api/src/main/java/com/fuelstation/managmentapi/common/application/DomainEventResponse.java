package com.fuelstation.managmentapi.common.application;

import com.fuelstation.managmentapi.authentication.application.UserResponse;
import lombok.Data;

import java.time.Instant;

@Data
public class DomainEventResponse {

    private final String type;
    private final Instant occurredAt;
    private final UserResponse performedBy;

}
