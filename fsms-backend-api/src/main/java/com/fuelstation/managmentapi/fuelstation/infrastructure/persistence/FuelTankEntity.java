package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fuel_station_fuel_tanks")
public class FuelTankEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fuelTankId;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_grade", nullable = false)
    private FuelGrade fuelGrade;

    @Column(name = "current_volume", nullable = false)
    private BigDecimal currentVolume;

    @Column(name = "max_capacity", nullable = false)
    private BigDecimal maxCapacity;

    @ManyToOne
    @JoinColumn(name = "fuel_station_id", nullable = false)
    private FuelStationEntity fuelStation;

    @Column(name = "last_refill_date")
    private OffsetDateTime lastRefillDate;
}