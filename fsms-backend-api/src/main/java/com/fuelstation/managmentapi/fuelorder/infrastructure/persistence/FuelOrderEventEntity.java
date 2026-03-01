package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;

@Entity
@Table(name = "fuel_order_events")
@Data
@NoArgsConstructor
public class FuelOrderEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "fuel_order_id", nullable = false)
    private Long fuelOrderId;

    @Column(name = "fuel_station_id", nullable = false)
    private Long fuelStationId;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(name = "occurred_at", nullable = false)
    private OffsetDateTime occurredAt;

    @Column(name = "performed_by")
    private Long performedBy;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", columnDefinition = "jsonb")
    private Map<String, Object> payload;

}
