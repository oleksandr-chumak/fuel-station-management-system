package com.fuelstation.managmentapi.manager.infrastructure.persistence;

import jakarta.persistence.*;
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

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "manager_status_id", nullable = false)
    private Long managerStatusId;
}
