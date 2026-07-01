package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaFuelOrderRepository extends JpaRepository<FuelOrderEntity, Long> {
    @Query("SELECT f FROM FuelOrderEntity f WHERE f.fuelStationId = :fuelStationId ORDER BY f.createdAt DESC")
    List<FuelOrderEntity> findByFuelStationId(@Param("fuelStationId") Long fuelStationId);

    @Query("""
            SELECT DISTINCT f FROM FuelOrderEntity f
            JOIN f.allocations a
            WHERE a.fuelTankId IN :fuelTankIds
            AND f.fuelGradeId = :fuelGradeId
            AND f.fuelOrderStatusId = :statusId
        """)
    List<FuelOrderEntity> findByFuelTankIdsAndGradeAndStatus(
        @Param("fuelTankIds") List<Long> fuelTankIds,
        @Param("fuelGradeId") Long fuelGradeId,
        @Param("statusId") Long statusId
    );

    @Query("""
            SELECT DISTINCT f FROM FuelOrderEntity f
            JOIN f.allocations a
            WHERE a.fuelTankId = :fuelTankId
            AND f.fuelOrderStatusId = :statusId
        """)
    List<FuelOrderEntity> findByFuelTankIdAndStatus(
        @Param("fuelTankId") Long fuelTankId,
        @Param("statusId") Long statusId
    );

    @Query("""
            SELECT f FROM FuelOrderEntity f
            WHERE f.fuelStationId IN :fuelStationIds
            AND f.fuelOrderStatusId = :statusId
        """)
    List<FuelOrderEntity> findByFuelStationIdsAndStatus(
        @Param("fuelStationIds") List<Long> fuelStationIds,
        @Param("statusId") Long statusId
    );
}