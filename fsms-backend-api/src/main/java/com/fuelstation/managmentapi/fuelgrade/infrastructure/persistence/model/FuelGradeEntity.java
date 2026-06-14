package com.fuelstation.managmentapi.fuelgrade.infrastructure.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fuel_grades")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FuelGradeEntity {

    @Id
    @Column(name = "fuel_grade_id")
    private Long fuelGradeId;

    @Column(name = "fuel_grade_name", nullable = false, length = 50)
    private String fuelGradeName;
}
