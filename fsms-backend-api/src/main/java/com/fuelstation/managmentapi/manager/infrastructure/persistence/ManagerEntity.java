package com.fuelstation.managmentapi.manager.infrastructure.persistence;

import com.fuelstation.managmentapi.manager.domain.ManagerStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "managers")
public class ManagerEntity {
    @Id
    private Long credentialsId;
    
    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private ManagerStatus status;
}
