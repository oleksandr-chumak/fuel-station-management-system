package com.fuelstation.managmentapi.administrator.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAdministratorRepository extends JpaRepository<AdministratorEntity, Long> {
    @Query("SELECT a FROM AdministratorEntity a WHERE a.credentials.email = :email")
    Optional<AdministratorEntity> findByCredentialsEmail(@Param("email") String email);
}

