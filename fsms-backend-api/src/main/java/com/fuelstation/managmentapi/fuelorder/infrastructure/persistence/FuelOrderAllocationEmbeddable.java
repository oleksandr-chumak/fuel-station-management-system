package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class FuelOrderAllocationEmbeddable {

    @Column(name = "fuel_tank_id", nullable = false)
    private long fuelTankId;

    @Column(name = "volume", nullable = false)
    private BigDecimal volume;
}
