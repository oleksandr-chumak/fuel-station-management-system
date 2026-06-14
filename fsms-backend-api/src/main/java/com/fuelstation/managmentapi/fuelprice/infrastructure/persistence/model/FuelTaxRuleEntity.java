package com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "fuel_tax_rules")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FuelTaxRuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tax_rule_id")
    private Long taxRuleId;

    @Column(name = "country_id", nullable = false)
    private Long countryId;

    @Column(name = "fuel_grade_id", nullable = false)
    private Long fuelGradeId;

    @Column(name = "tax_type", nullable = false, length = 50)
    private String taxType;

    @Column(name = "name_local", nullable = false, length = 100)
    private String nameLocal;

    @Column(name = "name_english", nullable = false, length = 100)
    private String nameEnglish;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    @Column(name = "value_type", nullable = false, length = 20)
    private String valueType;

    @Column(name = "amount", nullable = false, precision = 19, scale = 6)
    private BigDecimal amount;

    @Column(name = "currency", length = 8)
    private String currency;

    @Column(name = "unit", length = 50)
    private String unit;

    @Column(name = "effective_from", nullable = false)
    private OffsetDateTime effectiveFrom;

    @Column(name = "effective_to")
    private OffsetDateTime effectiveTo;
}
