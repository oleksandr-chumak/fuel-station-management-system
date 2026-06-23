package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "fuel_tank_volume_history")
@Data
@NoArgsConstructor
public class FuelTankVolumeHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @Column(name = "fuel_station_id", nullable = false)
    private Long fuelStationId;

    @Column(name = "fuel_tank_id", nullable = false)
    private Long fuelTankId;

    @Column(name = "fuel_grade", nullable = false, length = 50)
    private String fuelGrade;

    @Column(name = "old_volume", nullable = false, precision = 12, scale = 3)
    private BigDecimal oldVolume;

    @Column(name = "new_volume", nullable = false, precision = 12, scale = 3)
    private BigDecimal newVolume;

    @Column(name = "reason", nullable = false, length = 20)
    private String reason;

    @Column(name = "changed_at", nullable = false)
    private OffsetDateTime changedAt;

    @Column(name = "changed_by")
    private Long changedBy;

}
