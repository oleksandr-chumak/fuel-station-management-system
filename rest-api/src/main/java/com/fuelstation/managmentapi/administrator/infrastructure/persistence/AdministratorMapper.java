package com.fuelstation.managmentapi.administrator.infrastructure.persistence;

import com.fuelstation.managmentapi.administrator.domain.Administrator;
import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.authentication.infrastructure.persistence.CredentialsEntity;

public class AdministratorMapper {
    public static Administrator toDomain(AdministratorEntity entity) {
        if (entity == null) return null;
        return new Administrator(
            entity.getId(),
            entity.getCredentials().getEmail(),
            entity.getCredentials().getId()
        );
    }

    public static AdministratorEntity toEntity(Administrator domain) {
        if (domain == null) return null;
        return new AdministratorEntity(
            domain.getId(),
            new CredentialsEntity(
                domain.getCredentialsId(), 
                domain.getEmail(),
                UserRole.Administrator,
                null,
                null,
                null,
                null
            )
        );
    }
}