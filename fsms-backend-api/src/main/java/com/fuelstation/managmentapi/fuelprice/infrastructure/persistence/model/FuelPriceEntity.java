package com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.model;

import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.common.domain.FuelUnit;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name="fuel_prices")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FuelPriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fuelPriceId;

    @Column(name = "fuel_grade_id", nullable = false)
    private Long fuelGradeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false)
    private FuelUnit unit;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private CurrencyCode currencyCode;

    @Column(name = "source", nullable = false)
    private String source;

    @Column(name = "fetched_at", nullable = false)
    private OffsetDateTime fetchedAt;
}
