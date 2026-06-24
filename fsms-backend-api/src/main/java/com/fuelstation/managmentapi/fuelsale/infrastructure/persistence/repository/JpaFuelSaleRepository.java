package com.fuelstation.managmentapi.fuelsale.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.fuelsale.infrastructure.persistence.model.FuelSaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaFuelSaleRepository extends JpaRepository<FuelSaleEntity, Long> {

    @Query("SELECT s FROM FuelSaleEntity s WHERE s.fuelStationId = :stationId ORDER BY s.soldAt DESC")
    List<FuelSaleEntity> findByFuelStationId(@Param("stationId") long stationId);
}
