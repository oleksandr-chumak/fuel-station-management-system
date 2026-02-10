package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

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
    @Column(name = "fuel_order_id")
    private Long fuelOrderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FuelOrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_grade", nullable = false)
    private FuelGrade grade;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "fuel_station_id", nullable = false)
    private long fuelStationId;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}