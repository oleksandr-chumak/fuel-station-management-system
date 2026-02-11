package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column(name = "fuel_order_status_id", nullable = false)
    private Long fuelOrderStatusId;

    @Column(name = "fuel_grade_id", nullable = false)
    private Long fuelGradeId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "fuel_station_id", nullable = false)
    private long fuelStationId;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}