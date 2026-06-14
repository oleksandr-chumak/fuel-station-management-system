package com.fuelstation.managmentapi.fuelgrade.infrastructure.persistence.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AvailableFuelGradeId implements Serializable {

    private Long countryId;
    private Long fuelGradeId;
}
