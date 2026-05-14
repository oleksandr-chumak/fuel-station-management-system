package com.fuelstation.managmentapi.fuelpurchase.infrastructure.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "fuel_purchases")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FuelPurchaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fuel_purchase_id")
    private Long fuelPurchaseId;

    @Column(name = "fuel_order_id", nullable = false)
    private Long fuelOrderId;

    @Column(name = "fuel_station_id", nullable = false)
    private Long fuelStationId;

    @Column(name = "fuel_grade_id", nullable = false)
    private long fuelGradeId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "price_per_liter", nullable = false)
    private BigDecimal pricePerLiter;

    @Column(name = "currency", nullable = false, length = 8)
    private String currency;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "purchased_at", nullable = false)
    private OffsetDateTime purchasedAt;
}
