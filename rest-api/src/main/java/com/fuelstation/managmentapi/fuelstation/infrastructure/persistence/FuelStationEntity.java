package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence;

import java.time.LocalDate;
import java.util.List;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fuel_stations")
public class FuelStationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private FuelStationAddressEmbeddable address;

    @ElementCollection
    private List<FuelPriceEmbeddable> fuelPrices;

    @OneToMany(mappedBy = "fuelStation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FuelTankEntity> fuelTanks;
    
    @ElementCollection
    @CollectionTable(name = "fuel_station_managers", joinColumns = @JoinColumn(name = "fuel_station_id"))
    @Column(name = "manager_id")
    private List<Long> assignedManagers;

    @Enumerated(EnumType.STRING)
    private FuelStationStatus status;

    @Column(name = "create_at", nullable = false)
    private LocalDate createdAt;
}
