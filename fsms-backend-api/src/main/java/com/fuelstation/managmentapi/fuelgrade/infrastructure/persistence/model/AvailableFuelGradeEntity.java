package com.fuelstation.managmentapi.fuelgrade.infrastructure.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "available_fuel_grades")
@IdClass(AvailableFuelGradeId.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AvailableFuelGradeEntity {

    @Id
    @Column(name = "country_id")
    private Long countryId;

    @Id
    @Column(name = "fuel_grade_id")
    private Long fuelGradeId;
}
