package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence;

import java.time.LocalDate;

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
@Table(name = "fuel_tanks")
public class FuelTankEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FuelGrade fuelGrade;

    @Column(name = "current_volume", nullable = false)
    private float currentVolume;

    @Column(name = "max_capacity", nullable = false)
    private float maxCapacity;

    @ManyToOne
    @JoinColumn(name = "fuel_station_id")
    private FuelStationEntity fuelStation;

    @Column(name = "last_refill_date", nullable = true)
    private LocalDate lastRefillDate;
}