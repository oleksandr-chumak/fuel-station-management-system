package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import java.time.LocalDate;

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fuel_orders")
public class FuelOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FuelOrderStatus status;

    @Enumerated(EnumType.STRING)
    private FuelGrade grade;

    @Column(name = "amount", nullable = false)
    private float amount;

    @Column(name = "fuel_station_id", nullable = false)
    private long fuelStationId;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;
}