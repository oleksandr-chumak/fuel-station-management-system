package com.fuelstation.managmentapi.manager.infrastructure.persistence;

import java.util.List;

import com.fuelstation.managmentapi.authentication.infrastructure.persistence.CredentialsEntity;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationEntity;
import com.fuelstation.managmentapi.manager.domain.ManagerStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private ManagerStatus status;

    @ManyToMany(mappedBy = "assignedManagers")
    private List<FuelStationEntity> fuelStationsAssignedTo;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "credentials_id", referencedColumnName = "id")
    private CredentialsEntity credentials;
}
