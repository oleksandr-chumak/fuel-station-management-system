package com.fuelstation.managmentapi.administrator.infrastructure.persistence;

import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.administrator.domain.Administrator;

@Component
public class AdministratorMapper {

    public Administrator toDomain(AdministratorEntity entity) {
        return new Administrator(entity.getCredentialsId());
    }

    public AdministratorEntity toEntity(Administrator domain) {
        return new AdministratorEntity(domain.getCredentialsId());
    }
}