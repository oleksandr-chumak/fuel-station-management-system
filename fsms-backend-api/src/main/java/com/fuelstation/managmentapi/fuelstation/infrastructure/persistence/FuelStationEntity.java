package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence;

import java.time.OffsetDateTime;
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
    @Column(name = "fuel_station_id")
    private Long fuelStationId;

    @Embedded
    private FuelStationAddressEmbeddable address;

    @ElementCollection()
    @CollectionTable(name = "fuel_station_fuel_prices", joinColumns = @JoinColumn(name = "fuel_station_id"))
    private List<FuelPriceEmbeddable> fuelPrices;

    @OneToMany(mappedBy = "fuelStation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FuelTankEntity> fuelTanks;
    
    @ElementCollection
    @CollectionTable(name = "fuel_station_managers", joinColumns = @JoinColumn(name = "fuel_station_id"))
    @Column(name = "manager_id", nullable = false)
    private List<Long> assignedManagers;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FuelStationStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
