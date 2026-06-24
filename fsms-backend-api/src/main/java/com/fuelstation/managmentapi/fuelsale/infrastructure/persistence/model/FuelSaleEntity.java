package com.fuelstation.managmentapi.fuelsale.infrastructure.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "fuel_sales")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FuelSaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fuel_sale_id")
    private Long fuelSaleId;

    @Column(name = "fuel_station_id", nullable = false)
    private Long fuelStationId;

    @Column(name = "fuel_tank_id", nullable = false)
    private Long fuelTankId;

    @Column(name = "fuel_grade_id", nullable = false)
    private long fuelGradeId;

    @Column(name = "volume", nullable = false)
    private BigDecimal volume;

    @Column(name = "price_per_liter", nullable = false)
    private BigDecimal pricePerLiter;

    @Column(name = "currency", nullable = false, length = 8)
    private String currency;

    @Column(name = "total_revenue", nullable = false)
    private BigDecimal totalRevenue;

    @Column(name = "sold_at", nullable = false)
    private OffsetDateTime soldAt;
}
