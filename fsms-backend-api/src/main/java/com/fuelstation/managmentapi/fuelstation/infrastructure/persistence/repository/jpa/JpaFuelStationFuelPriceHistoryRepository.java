package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.jpa;

import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.entity.FuelStationFuelPriceHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface JpaFuelStationFuelPriceHistoryRepository
        extends JpaRepository<FuelStationFuelPriceHistoryEntity, Long> {

    List<FuelStationFuelPriceHistoryEntity> findByFuelStationIdOrderByChangedAtDesc(Long fuelStationId);

    /**
     * Returns price history for a station, filtering consecutive duplicates per fuel grade
     * (only points where the price actually changed are returned).
     *
     * When {@code fromTime} is non-null, the result also includes a synthetic carry-in row
     * per grade (the last price that was effective at the window start) so the chart can draw
     * a horizontal step from the window start, and that carry-in row's timestamp is clamped
     * to {@code fromTime}.
     *
     * When {@code fromTime} is null, the full history is returned (still de-duplicated).
     */
    @Query(value = """
            WITH carry_in AS (
              SELECT DISTINCT ON (fuel_grade)
                history_id,
                fuel_station_id,
                fuel_grade,
                price_per_liter,
                currency,
                GREATEST(changed_at, COALESCE(CAST(:fromTime AS timestamptz), '-infinity'::timestamptz)) AS changed_at,
                changed_by
              FROM fuel_station_fuel_price_history
              WHERE fuel_station_id = :stationId
                AND changed_at < COALESCE(CAST(:fromTime AS timestamptz), '-infinity'::timestamptz)
              ORDER BY fuel_grade, changed_at DESC
            ),
            in_window AS (
              SELECT history_id, fuel_station_id, fuel_grade, price_per_liter, currency, changed_at, changed_by
              FROM fuel_station_fuel_price_history
              WHERE fuel_station_id = :stationId
                AND changed_at >= COALESCE(CAST(:fromTime AS timestamptz), '-infinity'::timestamptz)
            ),
            combined AS (
              SELECT * FROM carry_in
              UNION ALL
              SELECT * FROM in_window
            ),
            with_lag AS (
              SELECT
                history_id, fuel_station_id, fuel_grade, price_per_liter, currency, changed_at, changed_by,
                LAG(price_per_liter) OVER (PARTITION BY fuel_grade ORDER BY changed_at) AS prev_price
              FROM combined
            )
            SELECT history_id, fuel_station_id, fuel_grade, price_per_liter, currency, changed_at, changed_by
            FROM with_lag
            WHERE prev_price IS NULL OR prev_price <> price_per_liter
            ORDER BY changed_at ASC
            """, nativeQuery = true)
    List<FuelStationFuelPriceHistoryEntity> findRealPriceChanges(
            @Param("stationId") long stationId,
            @Param("fromTime") Instant fromTime
    );
}
