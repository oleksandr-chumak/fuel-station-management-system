package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "fuel_station_fuel_price_history")
@Data
@NoArgsConstructor
public class FuelStationFuelPriceHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @Column(name = "fuel_station_id", nullable = false)
    private Long fuelStationId;

    @Column(name = "fuel_grade", nullable = false, length = 50)
    private String fuelGrade;

    @Column(name = "price_per_liter", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerLiter;

    @Column(name = "currency", nullable = false, length = 8)
    private String currency;

    @Column(name = "changed_at", nullable = false)
    private OffsetDateTime changedAt;

    @Column(name = "changed_by")
    private Long changedBy;

}
